package ssg.commandssg;

import com.google.inject.Guice;
import com.google.inject.Injector;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;
import ssg.buildpage.BuildPage;
import ssg.buildsite.BuildSite;
import ssg.modules.BuildPageModule;
import ssg.modules.BuildSiteModule;

/**
 * CommandSsgBuild class.
 */
@SuppressWarnings({"PMD.UnusedPrivateField", "PMD.ImmutableField",
    "PMD.AvoidCatchingGenericException", "PMD.LawOfDemeter"})
@CommandLine.Command(name = "build", mixinStandardHelpOptions = true)
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
     * Input Directory.
     */
    @CommandLine.Option(names = {"--input-dir"},
            description = "input directory, default is ./")
    private String inputDir = "./";


    /**
     * List of .html files expected as output.
     */
    @CommandLine.Parameters(arity = "0..*", description = "at least one expected .html output file")
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

        logger.info("CommandSsgBuild : ssg build subcommand called");

        //CREATING OUTPUTDIR IF IT DOES NOT EXISTS
        try {
            Files.createDirectories(Path.of(outputDir));
            logger.info("CommandSsgBuild : " + outputDir + " directory created ");
        } catch (FileAlreadyExistsException e) {
            logger.info("CommandSsgBuild : " + outputDir + " already exists, no action required");
        } catch (IOException e) {
            logger.error("CommandSsgBuild : There was a when we create directories", e);
        }


        //FILES TO TRANSLATE WERE SPECIFIED SO WE CALL BUILD PAGE ON EACH OF THEM
        if (files != null) {

            //GETTING BUILD PAGE INSTANCE
            Injector buildPageInjector = Guice.createInjector(new BuildPageModule());
            BuildPage buildPageInstance = buildPageInjector.getInstance(BuildPage.class);

            for (String file : files) {
                try {
                    buildPageInstance.run(file, outputDir);
                    logger.info(file + " translated in " + outputDir);
                } catch (Exception e) {
                    logger.error("There was a problem for the command build", e);
                }
            }

        } else {
            Injector buildSiteInjector = Guice.createInjector(new BuildPageModule(),
                    new BuildSiteModule());
            BuildSite buildSiteInstance = buildSiteInjector.getInstance(BuildSite.class);
            try {
                buildSiteInstance.createWebSite(inputDir, outputDir);
                logger.info(inputDir + " translated in " + outputDir);
            } catch (Exception e) {
                logger.error("There was a problem with command build", e);
            }
        }
    }
}