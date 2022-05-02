package ssg.buildsite;

import static ssg.config.SiteStructureVariable.CONTENTS;
import static ssg.config.SiteStructureVariable.INDEX_MD;
import static ssg.config.SiteStructureVariable.RAW_CONTENTS;
import static ssg.config.SiteStructureVariable.RAW_STATIC;
import static ssg.config.SiteStructureVariable.SITE_TOML;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ssg.buildpage.BuildPage;
import ssg.exceptions.NotConvertibleException;
import ssg.filereader.FileReader;
import ssg.parsertoml.ParserToml;
import ssg.staticfilehandler.InterfaceStaticFileHandler;
import ssg.tomlvaluetypewrapper.TomlValueTypeWrapper;

/**
 * Website builder.
 */
@SuppressWarnings({"PMD.LawOfDemeter", "PMD.AvoidCatchingGenericException",
    "PMD.GuardLogStatement", "PMD.SignatureDeclareThrowsException",
    "PMD.UnusedPrivateField", "PMD.ExcessiveImports",
    "PMD.AvoidInstantiatingObjectsInLoops"})
@SuppressFBWarnings
public class BuildSiteImplementation implements BuildSite {

    /**
     * Log4J Logger.
     */
    public static final Logger logger = LogManager.getLogger();

    /**
     * configuration of the website being built.
     */
    private Map<String, TomlValueTypeWrapper> config;

    /**
     * Number of jobs.
     */
    private int jobs = 1;

    /**
     * BuildPage dependency.
     */
    @Inject @Named("BuildPage")
    private BuildPage buildPage;

    /**
     * FileReader dependency.
     */
    @Inject @Named("FileReader")
    private FileReader fileReader;

    /**
     * StaticFileHandlerApache dependency.
     */
    @Inject @Named("StaticFileHandler")
    private InterfaceStaticFileHandler staticFileHandler;

    /**
     * ParserToml dependency.
     */
    @Inject @Named("ParserToml")
    private ParserToml parserToml;

    /**
     * Create the entire website from a directory.
     *
     * @param srcDirectory source directory.
     * @param dstDirectory destination directory.
     * @throws Exception when the source directory is not correct,
     *      absence of the site.toml or index.md files
     *      or if something getting wrong during the creation of the website.
     */
    @Override
    @SuppressFBWarnings
    public void createWebSite(String srcDirectory, String dstDirectory) throws Exception {

        logger.info("createWebSite() : attempt to create web site");

        File folder = new File(srcDirectory);

        if (!isConvertible(folder)) {
            logger.error("createWebSite(): couldn't convert into website "
                    +
                    "because the source directory format isn't correct");
            throw new NotConvertibleException(
                    "createWebSite(): couldn't convert into website "
                    +
                    "because the source directory format isn't correct");
        }

        File[] listFiles = Objects.requireNonNull(folder.listFiles());
        Arrays.sort(listFiles);
        for (File item : Objects.requireNonNull(listFiles,"Empty directory")) {
            if (RAW_CONTENTS.equals(item.getName())) {
                createContentDirectoryInDestination(dstDirectory);
                convertEverythingInsideContentsDirectory(item, dstDirectory + CONTENTS);
            } else if (RAW_STATIC.equals(item.getName())) {
                logger.info(
                        "createWebSite() "
                                +
                                ": apply the function handle from staticFileHandler {}",
                        item.getName());
                staticFileHandler.handle(item.getPath() + "/", dstDirectory);
            } else if (SITE_TOML.equals(item.getName())) {
                logger.info("createWebSite() : reading config file {}", item.getPath());
                String bufferConfig =  fileReader.read(item.getPath());
                logger.info("createWebSite() : parsing config file {}", item.getPath());
                this.config = parserToml.parse(bufferConfig);
            }
        }
        logger.info("createWebSite() : create website successfully");
    }

    private void createContentDirectoryInDestination(String dstDirectory) {
        try {
            Files.createDirectories(Path.of(dstDirectory + CONTENTS));
            logger.info("BuildSite : " + dstDirectory + " directory created ");
        } catch (FileAlreadyExistsException e) {
            logger.info("BuildSite : " + dstDirectory + " already exists, no action required");
        } catch (IOException e) {
            logger.error("Buildsite : There was a problem when we create directories", e);
        }
    }

    private void convertEverythingInsideContentsDirectory(File item, String dstDirectory)
            throws Exception {
        File[] listFiles = Objects.requireNonNull(item.listFiles());
        Arrays.sort(listFiles);

        List<Callable<Void>> tasks = new ArrayList<>();

        for (File subItem : Objects.requireNonNull(listFiles,"Empty directory")) {
            if (!subItem.isDirectory()) {
                Callable<Void> task = new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        try {
                            buildPage.run(subItem.getPath(), dstDirectory);
                            logger.info("createWebSite() : {} was"
                                    + " successfully create", subItem.getPath());
                            return null;
                        } catch (Exception e) {
                            logger.error("createWebSite() : exception"
                                    + " raise when create web site", e);
                            throw e;
                        }
                    }
                };
                tasks.add(task);
            } else {
                Files.createDirectories(Path.of(dstDirectory + subItem.getName()));
                convertEverythingInsideContentsDirectory(subItem,
                        dstDirectory + subItem.getName() + "/");
            }
        }
        logger.info("createwebsite() : launching executorService");
        ExecutorService executorService = Executors.newFixedThreadPool(jobs);
        try {
            List<Future<Void>> results = executorService.invokeAll(tasks);
            for (Future<Void> futur : results) {
                futur.get();
            }
        } catch (Exception e) {
            logger.error("createwebsite() : Error occured when "
                    + "running excutor service :  {}", e);
            executorService.shutdown();
            throw e;
        } finally {
            executorService.shutdown();
        }
    }

    private boolean isConvertible(File folder) {
        boolean thereIsSiteToml = false;
        boolean thereIsContentsDirectoryAndIndexFile = false;
        for (File item : Objects.requireNonNull(folder.listFiles(),"Empty directory")) {
            if (SITE_TOML.equals(item.getName())) {
                thereIsSiteToml = true;
            }
            if (RAW_CONTENTS.equals(item.getName())) {
                for (File subItem : Objects.requireNonNull(item.listFiles())) {
                    if (INDEX_MD.equals(subItem.getName())) {
                        thereIsContentsDirectoryAndIndexFile = true;
                        break;
                    }
                }
            }
        }
        return thereIsSiteToml && thereIsContentsDirectoryAndIndexFile;
    }

    /**
     * Sets number of jobs.
     *
     * @param jobs value to set
     */
    @Override
    public void setJobs(int jobs) {
        this.jobs = jobs;
    }
}