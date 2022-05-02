package ssg.templatehandler;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.hubspot.jinjava.Jinjava;
import com.hubspot.jinjava.JinjavaConfig;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.interpret.RenderResult;
import com.hubspot.jinjava.lib.fn.ELFunctionDefinition;
import com.hubspot.jinjava.lib.fn.InjectedContextFunctionProxy;
import com.hubspot.jinjava.loader.ResourceLocator;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ssg.exceptions.TemplateProcesserException;
import ssg.filereader.FileReader;

/**
 * Process a template interpreting nested Jinja code.
 */
public class TemplateProcesserImplementation implements TemplateProcesser {

    /**
     * Log4J Logger.
     */
    private static final Logger logger = LogManager.getLogger();

    /**
     * FileReader reads files from disk.
     */
    @Inject @Named("FileReader")
    private FileReader reader;

    /**
     * Jinjava builder.
     */
    private final JinjavaConfig.Builder builder = JinjavaConfig
            .newBuilder()
            .withFailOnUnknownTokens(true);

    /**
     * Content directory relatively to templates directory.
     */
    private static final String CONTENT_DIR = "/../content";

    /**
     * Check wether a file or directory is under a given directory.
     *
     * @param child the file or directory to consider as the child.
     * @param parent file or directory to consider as the parent.
     * @return true if parent is a directory and child is under. False otherwise.
     */
    @SuppressWarnings("PMD.LawOfDemeter")
    private static boolean isInside(String child, String parent) {
        Path childAbsolutePath = Path.of(child).toAbsolutePath().normalize();
        Path parentAbsolutePath = Path.of(parent).toAbsolutePath().normalize();

        return childAbsolutePath.startsWith(parentAbsolutePath);
    }

    /**
     * Inner class ListFilesFunction.
     *
     * <p>Holds function list_file(String, boolean) to be added to jinjava primitives functions.</p>
     */
    public static class ListFilesFunction {

        /**
         * Base directory.
         *
         * <p>list_files(directory, bool) will list files inside baseDir/directory.</p>
         */
        private final String baseDir;

        /**
         * Constructor must set baseDir.
         *
         * @param baseDir base directory.
         */
        public ListFilesFunction(String baseDir) {
            this.baseDir = Path.of(baseDir).normalize().toString();
        }

        /**
         * List files inside a given directory.
         *
         * @param directory directory to list.
         * @param rec set to true for recursive listing.
         * @return list of files inside given directory.
         * @throws TemplateProcesserException when there's any problem.
         */
        @SuppressFBWarnings // false positive for potential path traversal vulnerability
        @SuppressWarnings({"PMD.GuardLogStatement", "PMD.LawOfDemeter"})
        public List<String> listFiles(String directory, boolean rec)
                throws TemplateProcesserException {
            File relativeDir =
                    isInside(directory, baseDir)
                            ? new File(directory) : new File(baseDir, directory);
            List<String> res = new ArrayList<String>();

            if (rec) {
                File[] directories = relativeDir.listFiles(File::isDirectory);
                if (directories == null) {
                    logger.error("{} doesn't denote a directory or an I/O error occured",
                            relativeDir.toString());
                    return res;
                }

                for (File dir : directories) {
                    res.addAll(listFiles(dir.toString(), true));
                }
            }

            File[] files = relativeDir.listFiles(File::isFile);
            if (files == null) {
                logger.error("{} doesn't denote a directory or an I/O error occured",
                        relativeDir.toString());
                return res;
            }

            for (File file : files) {
                res.add(file.toString().replace(baseDir + "/", ""));
            }
            return res;
        }

        /**
         * get list_files(String. boolean) function definition.
         *
         * @param baseDir reference directory such that calls to list_files("example", bool)
         *     will refer to baseDir/example.
         * @return list_files(String, boolean) function definition.
         * @throws TemplateProcesserException when there's any problem.
         */
        public static synchronized ELFunctionDefinition getFunctionDefinition(String baseDir)
                throws TemplateProcesserException {
            try {
                Method method = ListFilesFunction.class.getDeclaredMethod(
                        "listFiles", String.class, boolean.class);
                ListFilesFunction instance = new ListFilesFunction(baseDir);
                return InjectedContextFunctionProxy.defineProxy("", "list_files", method, instance);
            } catch (NoSuchMethodException e) {
                logger.error("internal error: unable to get list_files(String,boolean) definition");
                throw new TemplateProcesserException(e);
            }
        }
    }

    /**
     * Inner class LocalFilesLocator implements FileLocator.
     *
     * <p>A Jinjava FileLocator allows jinjava to interact with the file system.
     * It is required when processing templates that holds tag instructions such as
     * {% include 'template/foo.html' %} or {% extends 'bar.html' %}
     *
     * This LocalFilesLocator restrain access to baseDirectory's files and subdirectories
     * to prevent a user from adding code such as {% include '/etc/password' %} </p>
     */
    private class LocalFilesLocator implements ResourceLocator {

        /**
         * base directory for relative paths.
         * this ResourceLocator will prevent reading files that are above this base directory.
         */
        private final String baseDirectory;

        /**
         * Constructor must set baseDirectory.
         */
        public LocalFilesLocator(String baseDirectory) {
            this.baseDirectory = baseDirectory;
        }

        @Override
        @SuppressWarnings("PMD.LawOfDemeter")
        @SuppressFBWarnings // potential path traversal are taken care of.
        public String getString(String name, Charset encoding, JinjavaInterpreter interpreter)
                throws IOException {
            File file = new File(this.baseDirectory, name);
            if (!isInside(file.toString(), this.baseDirectory)) {
                throw new FileNotFoundException(
                        "Cannot include %s : file is above working directory %s"
                                .formatted(file.toString(), this.baseDirectory));
            }
            return reader.read(file.toString());
        }
    }

    /**
     * Process an HTML template with context into HTML.
     *
     * @param template source template.
     * @param context map of objects to replace.
     * @return the processed template.
     */
    @Override
    @SuppressWarnings("PMD.LawOfDemeter")
    public String process(String template, Map<String, Object> context, String templatesDir)
            throws TemplateProcesserException {

        Jinjava jinjava = new Jinjava(builder.build());
        jinjava.setResourceLocator(new LocalFilesLocator(templatesDir));
        jinjava.getGlobalContext().registerFunction(
                ListFilesFunction.getFunctionDefinition(templatesDir + CONTENT_DIR));

        RenderResult result = jinjava.renderForResult(template, context);

        if (result.hasErrors()) {
            String errorMessage = result.getErrors().get(0).getMessage();
            logger.error("process(): {}", errorMessage);
            throw new TemplateProcesserException(errorMessage);
        }
        return result.getOutput();
    }
}
