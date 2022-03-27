package ssg.buildsite;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.File;
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
    public static final String CONTENTS = "contents";

    /**
     * markdown file inside contents/.
     */
    public static final String INDEX_MD = "index.md";

    /**
     * static directory.
     */
    public static final String STATIC = "static";

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
    @SuppressFBWarnings //TODO: Check later if we can pass argument without user input.
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

        //TODO: Seems like Spotbugs doesn't know about Objects.requireNonNull.
        File[] listFiles = Objects.requireNonNull(folder.listFiles());
        Arrays.sort(listFiles);
        for (File item : Objects.requireNonNull(listFiles,"Empty directory")) {
            if (CONTENTS.equals(item.getName())) {
                convertEverythingInsideContentsDirectory(item);
            } else if (STATIC.equals(item.getName())) {
                logger.info(
                        "createWebSite() "
                                +
                                ": apply the function handle from staticFileHandler {}",
                        item.getName());
                staticFileHandler.handle(item.getName(), dstDirectory);
            } /*else if (SITE_TOML.equals(item.getPath())){

            }*/
        }
        logger.info("createWebSite() : create website successfully");
    }

    private void convertEverythingInsideContentsDirectory(File item) throws Exception {
        File[] listFiles = Objects.requireNonNull(item.listFiles());
        Arrays.sort(listFiles);
        for (File subItem : Objects.requireNonNull(listFiles,"Empty directory")) {
            if (!subItem.isDirectory()) {
                try {
                    buildPage.run(subItem.getPath(),
                            subItem.getPath()
                                    .replace(".md", ".html")
                                    .replace("srcDir", "dstDir"));
                    logger.info("createWebSite() : {} was successfully create", subItem.getPath());
                } catch (Exception e) {
                    logger.error("createWebSite() : exception raise when create web site", e);
                    throw e;
                }
            } else {
                convertEverythingInsideContentsDirectory(subItem);
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
            if (CONTENTS.equals(item.getName())) {
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