package ssg.commandssg;

import com.google.inject.Guice;
import com.google.inject.Injector;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;
import ssg.buildpage.BuildPage;
import ssg.buildpage.BuildPageImplementation;
import ssg.modules.BuildPageModule;

/**
 * CommandSsgBuild class.
 */
@SuppressWarnings({"PMD.UnusedPrivateField", "PMD.ImmutableField",
    "PMD.AvoidCatchingGenericException"})
@CommandLine.Command(name = "build")
public class CommandSsgBuild implements Runnable {

    /**
     * Logger.
     */
    private static final Logger logger = LogManager.getLogger();

    /**
     * Output Directory.
     */
    @CommandLine.Option(names = {"--output-dir"},
            description = "output directory, default is _output/")
    private String outputDir = "_output/";


    /**
     * List of .html files expected as output.
     */
    @CommandLine.Parameters(arity = "1..*", description = "at least one expected .html output file")
    private List<String> files;

    /**
     * Output Directory getter.
     *
     * @return output directory
     */
    public String getOutputDir() {
        return this.outputDir;
    }


    /**
     * Files at index getter.
     *
     * @param index index of files you want to access
     * @return path at specific index expected as output
     */
    public String getFile(int index) {
        return files.get(index);
    }

    /**
     * Runs the command.
     */
    @Override
    @SuppressWarnings("PMD.GuardLogStatement")
    public void run() {

        Injector buildPageInjector = Guice.createInjector(new BuildPageModule());
        BuildPage buildPageInstance = buildPageInjector.getInstance(BuildPageImplementation.class);

        logger.info("ssg build subcommand called");

        try {
            Files.createDirectories(Path.of(outputDir));
        } catch (IOException e) {
            logger.error("There was a when we create directories", e);
        }

        for (String file : files) {
            try {
                buildPageInstance.run(file, outputDir);
                logger.info(file + " translated in " + outputDir);
            } catch (Exception e) {
                logger.error("There was a problem for the command build", e);
            }
        }

    }
}