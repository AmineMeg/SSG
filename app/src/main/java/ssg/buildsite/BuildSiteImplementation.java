package ssg.buildsite;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ssg.buildpage.BuildPage;
import ssg.exceptions.NotConvertibleException;
import ssg.staticfilehandler.InterfaceStaticFileHandler;

/**
 * Website builder.
 */
@SuppressWarnings({"PMD.LawOfDemeter", "PMD.AvoidCatchingGenericException",
    "PMD.GuardLogStatement", "PMD.SignatureDeclareThrowsException"})
@SuppressFBWarnings
public class BuildSiteImplementation implements BuildSite {

    /**
     * Log4J Logger.
     */
    public static final Logger logger = LogManager.getLogger(BuildSiteImplementation.class);

    /**
     * TOML index File.
     */
    public static final String SITE_TOML = "site.toml";

    /**
     * Directory with markdown File.
     */
    public static final String CONTENTS = "content/";

    /**
     * Directory with markdown File.
     */
    public static final String RAW_CONTENTS = "content";

    /**
     * markdown file inside contents/.
     */
    public static final String INDEX_MD = "index.md";

    /**
     * static directory.
     */
    public static final String STATIC = "static/";

    /**
     * static directory.
     */
    public static final String RAW_STATIC = "static";

    //private final HashMap<K,V> config;

    /**
     * BuildPage dependency.
     */
    @Inject @Named("BuildPage")
    private BuildPage buildPage;

    /**
     * StaticFileHandlerApache dependency.
     */
    @Inject @Named("StaticFileHandler")
    private InterfaceStaticFileHandler staticFileHandler;

    /**
     * ParserToml dependency.
     */
    /*@Inject @Named("ParserToml")
    private ParserToml parserToml;*/

    /**
     * Create the entire website from a directory.
     *
     * @param srcDirectory source directory
     * @param dstDirectory destination directory
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
            } /*else if (SITE_TOML.equals(item.getPath())){

            }*/
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
        for (File subItem : Objects.requireNonNull(listFiles,"Empty directory")) {
            if (!subItem.isDirectory()) {
                try {
                    buildPage.run(subItem.getPath(), dstDirectory);
                    logger.info("createWebSite() : {} was successfully create", subItem.getPath());
                } catch (Exception e) {
                    logger.error("createWebSite() : exception raise when create web site", e);
                    throw e;
                }
            } else {
                Files.createDirectories(Path.of(dstDirectory + subItem.getName()));
                convertEverythingInsideContentsDirectory(subItem,
                        dstDirectory + subItem.getName() + "/");
            }
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
}