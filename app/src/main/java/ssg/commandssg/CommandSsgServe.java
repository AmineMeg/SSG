package ssg.commandssg;

import static ssg.commandssg.CommandSsgBuild.TEMPLATES;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;
import ssg.Generated;
import ssg.buildpage.BuildPage;
import ssg.buildsite.BuildSite;
import ssg.config.SiteStructureVariable;
import ssg.dependencymanager.DependencyManager;
import ssg.exceptions.BadDependencyTomlFormatException;
import ssg.exceptions.HttpConfigurationException;
import ssg.httpserver.MyHttpServer;
import ssg.ioc.Container;

/**
 * CommandSsgServe class.
 */
@SuppressFBWarnings
@Generated
@SuppressWarnings({"PMD.UnusedPrivateField", "PMD.ImmutableField",
    "PMD.AvoidCatchingGenericException", "PMD.LawOfDemeter", "PMD.GuardLogStatement",
    "PMD.RedundantFieldInitializer",
    "PMD.AvoidThrowingRawExceptionTypes",
    "PMD.AvoidInstantiatingObjectsInLoops",
    "PMD.CognitiveComplexity",
    "PMD.CyclomaticComplexity",
    "PMD.NPathComplexity"})
@CommandLine.Command(name = "serve", mixinStandardHelpOptions = true)
public class CommandSsgServe implements Runnable {

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
     * Jobs.
     */
    @CommandLine.Option(names = {"--jobs"},
            description = "number of threads for parallel computing."
                    + "Default is number of available cores.")
    private int jobs = Runtime.getRuntime().availableProcessors();

    /**
     * Input Directory.
     */
    @CommandLine.Option(names = {"--input-dir"},
            description = "input directory, default is ./")
    private String inputDir = "./";

    /**
     * Input Directory.
     */
    @CommandLine.Option(names = {"--port"},
            description = "input port, default is 8080")
    private String port = "8080";

    /**
     * Rebuild all.
     * */
    @CommandLine.Option(names = {"--rebuild-all"},
            description = "specify if everything should "
                    + "be rebuild or only the file "
                    + "that were modified of whose dependencies were modified")
    private boolean rebuildAll = false;


    /**
     * List of .html files expected as output.
     */
    @CommandLine.Parameters(arity = "0..*", description = "at least one expected .html output file")
    private List<String> files;

    /**
     * Output Directory getter.
     *
     * @return output directory.
     */
    public String getOutputDir() {
        return this.outputDir;
    }


    /**
     * Files at index getter.
     *
     * @param index index of files you want to access.
     * @return path at specific index expected as output.
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

        logger.info("CommandSsgServe : ssg serve subcommand called");

        //CREATING OUTPUT DIR IF IT DOES NOT EXISTS
        createOutputDir();
        outputDir = Utils.addBackSlashes(outputDir);
        inputDir = Utils.addBackSlashes(inputDir);

        if (rebuildAll) {
            try {
                FileUtils.deleteDirectory(new File(outputDir));
            } catch (IOException e) {
                logger.error("CommandSsgServe : Error while deleting dependency file : ", e);
            }
        }

        DependencyManager dependencyManager =  DependencyManager.getInstance(outputDir, inputDir
                + TEMPLATES);

        if (Files.exists(Path.of(dependencyManager.getOutputDirectory()
                + SiteStructureVariable.DEPENDENCIES_FILE))) {
            logger.info("resolveDependencies() : reading dependencies from file");
            try {
                dependencyManager.readDependenciesFromFile();
            } catch (IOException | BadDependencyTomlFormatException e) {
                logger.error("resolveDependencies() : error while reading dependencies from file");
            }
        }

        logger.info("CommandSsgBuild : ssg build subcommand called");

        List<Callable<Void>> tasks = new ArrayList<>();

        //FILES TO TRANSLATE WERE SPECIFIED SO WE CALL BUILD PAGE ON EACH OF THEM
        if (files != null) {
            //GETTING BUILD PAGE INSTANCE
            BuildPage buildPageInstance = Container.container.getInstance(BuildPage.class);
            for (String file : files) {
                Callable<Void> task = () -> {
                    runningBuildPageOnFile(buildPageInstance, file);
                    return null;
                };
                tasks.add(task);
            }
            logger.info("run(): Launching executor service");
            ExecutorService executorService = Executors.newFixedThreadPool(jobs);
            try {
                executorService.invokeAll(tasks);
                List<Future<Void>> results = executorService.invokeAll(tasks);
                for (Future<Void> futur : results) {
                    futur.get();
                }
            } catch (Exception e) {
                logger.error("run() : executorService was interrupted,"
                        + " shutting down executor service", e);
                executorService.shutdown();
            } finally {
                executorService.shutdown();
            }

        } else {
            BuildSite buildSiteInstance = Container.container.getInstance(BuildSite.class);
            buildSiteInstance.setJobs(jobs);
            runningBuildSiteOnDirectory(buildSiteInstance);
        }

        try {
            dependencyManager.writeDependenciesInFile();
        } catch (IOException e) {
            logger.error("CommandSsgBuild :  Error while writing dependency file,"
                    + "you probably built two diffrent"
                    + " websites on the same output Dorectory", e);
        }

        launchServer();
    }

    private void launchServer() {
        MyHttpServer myHttpServer = new MyHttpServer();
        try {
            myHttpServer.startServer(port, outputDir, inputDir);
        } catch (HttpConfigurationException | IOException e) {
            logger.error("There was a problem with command serve", e);
        }
    }

    private void createOutputDir() {
        try {
            Files.createDirectories(Path.of(outputDir));
            logger.info("CommandSsgBuild : " + outputDir + " directory created ");
        } catch (FileAlreadyExistsException e) {
            logger.info("CommandSsgBuild : " + outputDir + " already exists, no action required");
        } catch (IOException e) {
            logger.error("CommandSsgBuild : There was a when we create directories", e);
        }
    }

    private void runningBuildSiteOnDirectory(BuildSite buildSiteInstance) {
        try {
            buildSiteInstance.createWebSite(inputDir, outputDir);
            logger.info(inputDir + " translated in " + outputDir);
        } catch (Exception e) {
            logger.error("There was a problem with command serve", e);
        }
    }

    private void runningBuildPageOnFile(BuildPage buildPageInstance, String file) {
        try {
            buildPageInstance.run(file, outputDir);
            logger.info(file + " translated in " + outputDir);
        } catch (Exception e) {
            logger.error("There was a problem for the command serve", e);
        }
    }
}
