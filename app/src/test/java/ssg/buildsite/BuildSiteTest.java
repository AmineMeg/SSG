package ssg.buildsite;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static ssg.config.SiteStructureVariable.CONTENTS;
import static ssg.config.SiteStructureVariable.INDEX_MD;
import static ssg.config.SiteStructureVariable.SITE_TOML;
import static ssg.config.SiteStructureVariable.STATIC;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ssg.buildpage.BuildPage;
import ssg.exceptions.NotConvertibleException;
import ssg.filereader.FileReader;
import ssg.filereader.FileReaderImplementation;
import ssg.parsertoml.ParserToml;
import ssg.parsertoml.ParserTomlImplementation;
import ssg.staticfilehandler.InterfaceStaticFileHandler;


/**
 * JUnit test class for BuildSite.
 */
@SuppressWarnings({"PMD.LawOfDemeter", "PMD.AvoidCatchingGenericException",
    "PMD.TooManyMethods", "PMD.ExcessiveImports"})
class BuildSiteTest {

    /**
     * Class to be tested.
     */
    private BuildSite buildSite;

    /**
     * BuildPage dependency.
     */
    @Inject
    private final BuildPage buildPage = Mockito.mock(BuildPage.class);

    /**
     * StaticFileHandlerApache dependency.
     */
    @Inject
    private final InterfaceStaticFileHandler staticFileHandler =
            Mockito.mock(InterfaceStaticFileHandler.class);

    /**
     * Inject mock dependencies into the tested class.
     */
    private final Injector injector = Guice.createInjector(new AbstractModule() {
        @Override
        protected void configure() {
            bind(BuildPage.class)
                    .annotatedWith(Names.named("BuildPage"))
                    .toInstance(buildPage);
            bind(InterfaceStaticFileHandler.class)
                    .annotatedWith(Names.named("StaticFileHandler"))
                    .toInstance(staticFileHandler);
            bind(ParserToml.class)
                    .annotatedWith(Names.named("ParserToml"))
                    .to(ParserTomlImplementation.class);
            bind(FileReader.class)
                    .annotatedWith(Names.named("FileReader"))
                    .to(FileReaderImplementation.class);
        }
    });

    /**
     * Temporary source directory.
     */
    static final String srcDirectory = "srcDir/";

    /**
     * SubDirectory.
     */
    static final String subDirectory = "subDir/";

    /**
     * filename for test name.
     */
    static final String srcFileNameBis = "dodo.md";

    /**
     * Temporary destination  directory.
     */
    static final String dstDirectory = "dstDir/";

    /**
     * Log4J Logger.
     */
    private static final Logger logger = LogManager.getLogger();

    /**
     * Set up the class before each test.
     */
    @BeforeEach
    void setup() {
        buildSite = new BuildSiteImplementation();
        injector.injectMembers(buildSite);
        cleanTestDirectory();
    }

    @AfterEach
    void clean() {
        cleanTestDirectory();
    }

    /**
     * Attempt to find a missing directory.
     */
    @Test
    void readingMissingDirectoryThrowsException() {
        assertThrows(NullPointerException.class,
                () -> buildSite.createWebSite(
                        "missingSrcDirectory/",
                        "missingDstDirectory/"),
                "Attempt to find missing directory did raise IOException");
    }

    /**
     * Attempt to convert an input which miss site.toml and index.md.
     */
    @Test
    void throwWhenAttemptingToConvertWhenMissingSiteAndIndexFile() {
        try {
            Files.createDirectory(Path.of(srcDirectory));

            assertThrows(NotConvertibleException.class,
                    () -> buildSite.createWebSite(
                            srcDirectory,
                            "_output/"),
                    "Attempt to convert when there is no site.toml or index.md in content");
            Files.deleteIfExists(Path.of(srcDirectory));
        } catch (Exception e) {
            fail(e);
        }
    }

    /**
     * Attempt to convert an input which miss site.toml and index.md.
     */
    @Test
    void throwWhenAttemptingToConvertWhenThereSiteButNoIndexFile() {
        try {
            Files.createDirectory(Path.of(srcDirectory));
            Files.createFile(Path.of(srcDirectory + SITE_TOML));

            assertThrows(NotConvertibleException.class,
                    () -> buildSite.createWebSite(
                            srcDirectory,
                            "_output/"),
                    "Attempt to convert when there is no site.toml or index.md in content");
            Files.deleteIfExists(Path.of(srcDirectory + SITE_TOML));
            Files.deleteIfExists(Path.of(srcDirectory));
        } catch (Exception e) {
            fail(e);
        }
    }

    /**
     * Attempt to convert an input which miss site.toml and index.md.
     */
    @Test
    void throwWhenAttemptingToConvertWhenThereIndexButNoSiteFile() {
        try {
            Files.createDirectory(Path.of(srcDirectory));
            Files.createDirectory(Path.of(srcDirectory + CONTENTS));
            Files.createFile(Path.of(srcDirectory + CONTENTS + INDEX_MD));

            assertThrows(NotConvertibleException.class,
                    () -> buildSite.createWebSite(
                            srcDirectory,
                            "_output/"),
                    "Attempt to convert when there is no site.toml or index.md in content");
            Files.deleteIfExists(Path.of(srcDirectory + CONTENTS + INDEX_MD));
            Files.deleteIfExists(Path.of(srcDirectory + CONTENTS));
            Files.deleteIfExists(Path.of(srcDirectory));
        } catch (Exception e) {
            fail(e);
        }
    }

    /**
     * Attempt to convert an input which miss site.toml and index.md.
     */
    @Test
    void throwWhenAttemptingToConvertWhenMissingSiteAndIndex() {
        try {
            Files.createDirectory(Path.of(srcDirectory));

            assertThrows(NotConvertibleException.class,
                    () -> buildSite.createWebSite(
                            srcDirectory,
                            "missingDstDirectory/"),
                    "Attempt to convert when there is no site.toml or index.md in content");
            Files.deleteIfExists(Path.of(srcDirectory));
        } catch (Exception e) {
            fail(e);
        }
    }

    /**
     * Attempt to convert website but BuildPage raised an exception.
     */
    @Test
    void attemptToConvertButBuildPageRaisedException() {
        try {
            //GIVEN
            initDirectory();
            doThrow(new Exception()).when(buildPage).run(srcDirectory
                            + CONTENTS
                            + INDEX_MD,
                    dstDirectory + CONTENTS);

            //WHEN
            assertThrows(Exception.class, () -> buildSite.createWebSite(srcDirectory, dstDirectory),
                    "Exception Raised");

            //THEN
            verify(buildPage, times(1)).run(srcDirectory
                            + CONTENTS
                            + INDEX_MD,
                    dstDirectory + CONTENTS);
            verify(buildPage, times(1)).run(anyString(), anyString());
            verify(staticFileHandler, times(0)).handle(anyString(), anyString());

            cleanTestDirectory();
        } catch (Exception e) {
            cleanTestDirectory();
            fail(e);
        }
    }

    /**
     * Test.
     */
    @Test
    void testCreateWebSiteWithArbo() {
        try {
            //GIVEN
            initDirectory();
            Files.createDirectory(Path.of(srcDirectory + CONTENTS + subDirectory));
            Files.createFile(Path.of(srcDirectory + CONTENTS + subDirectory + srcFileNameBis));
            Files.createDirectory(Path.of(dstDirectory + CONTENTS + subDirectory));
            Files.createFile(Path.of(dstDirectory + CONTENTS + subDirectory + srcFileNameBis));
            doNothing().when(buildPage).run(srcDirectory
                            + CONTENTS
                            + INDEX_MD,
                    dstDirectory + CONTENTS);
            doNothing()
                    .when(buildPage)
                    .run(srcDirectory + CONTENTS + subDirectory + srcFileNameBis,
                            dstDirectory + CONTENTS + subDirectory);
            doNothing().when(staticFileHandler).handle(srcDirectory + STATIC, dstDirectory);

            //WHEN
            buildSite.createWebSite(srcDirectory, dstDirectory);

            //THEN
            verify(buildPage, times(1)).run(srcDirectory
                            + CONTENTS
                            + INDEX_MD,
                    dstDirectory + CONTENTS);
            verify(buildPage,times(1))
                    .run(srcDirectory + CONTENTS + subDirectory + srcFileNameBis,
                            dstDirectory + CONTENTS + subDirectory);
            verify(staticFileHandler, times(1)).handle(srcDirectory + STATIC, dstDirectory);
            verify(buildPage, times(1)).run(anyString(), anyString());
            verify(buildPage, times(1)).run(anyString(), anyString());

            cleanTestDirectory();
        } catch (Exception e) {
            cleanTestDirectory();
        }
    }

    @Test
    void testCreateWebSite() {
        try {
            //GIVEN
            initDirectory();
            doNothing().when(buildPage).run(srcDirectory
                            + CONTENTS
                            + INDEX_MD,
                    dstDirectory + CONTENTS);
            doNothing().when(staticFileHandler).handle(srcDirectory + STATIC, dstDirectory);

            //WHEN
            buildSite.createWebSite(srcDirectory, dstDirectory);

            //THEN
            verify(buildPage, times(1)).run(srcDirectory
                            + CONTENTS
                            + INDEX_MD,
                    dstDirectory + CONTENTS);
            verify(staticFileHandler, times(1)).handle(srcDirectory + STATIC, dstDirectory);
            verify(buildPage, times(1)).run(anyString(), anyString());
            verify(staticFileHandler, times(1)).handle(anyString(), anyString());

            cleanTestDirectory();
        } catch (Exception e) {
            cleanTestDirectory();
        }
    }

    /**
     * Create temporary directory for to add markdown file and static directory.
     */
    public static void initDirectory() {

        logger.info("initDirectory() : attempt to create directory");

        try {

            Files.createDirectory(Path.of(srcDirectory));
            Files.createFile(Path.of(srcDirectory + SITE_TOML));
            Files.createDirectory(Path.of(srcDirectory + CONTENTS));
            Files.createFile(Path.of(srcDirectory + CONTENTS + INDEX_MD));
            Files.createDirectory(Path.of(srcDirectory + STATIC));

            logger.info("initDirectory() : directory create successfully");

        } catch (IOException e) {
            logger.error("initDirectory() : "
                    +
                "exception raise while create directory", e);
        }
    }

    /**
     * Cleaning everything after test.
     */
    public static void cleanTestDirectory() {
        logger.info("cleanTestDirectory() : attempt to clear test directory");

        try {
            Files.deleteIfExists(Path.of(dstDirectory
                    + CONTENTS
                    + subDirectory));
            Files.deleteIfExists(Path.of(srcDirectory
                    + CONTENTS
                    + subDirectory
                    + srcFileNameBis));
            Files.deleteIfExists(Path.of(dstDirectory + CONTENTS + subDirectory));
            Files.deleteIfExists(Path.of(srcDirectory + CONTENTS + INDEX_MD));
            Files.deleteIfExists(Path.of(dstDirectory + CONTENTS));
            Files.deleteIfExists(Path.of(srcDirectory + CONTENTS + subDirectory));
            Files.deleteIfExists(Path.of(srcDirectory + SITE_TOML));
            Files.deleteIfExists(Path.of(srcDirectory + CONTENTS));
            Files.deleteIfExists(Path.of(dstDirectory + CONTENTS));
            Files.deleteIfExists(Path.of(srcDirectory + STATIC));
            Files.deleteIfExists(Path.of(dstDirectory + STATIC));
            Files.deleteIfExists(Path.of(srcDirectory));
            Files.deleteIfExists(Path.of(dstDirectory));


            logger.info("cleanTestDirectory() : cleaning is successful");

        } catch (IOException e) {
            logger.error("cleanTestDirectory() : "
                    +
                    "exception raised while cleaning",e);
            fail(e);
        }
    }
}