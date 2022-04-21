package ssg.templatehandler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static ssg.config.SiteStructureVariable.MINIMAL_TEMPLATE;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ssg.exceptions.TemplateHandlerException;
import ssg.exceptions.TemplateProcesserException;
import ssg.filereader.FileReader;
import ssg.page.PageDraft;
import ssg.templatehandler.TemplateProcesser;
import ssg.tomlvaluetypewrapper.TomlStringWrapper;
import ssg.tomlvaluetypewrapper.TomlValueTypeWrapper;

/**
 * Test class for TemplateHandler.
 */
@SuppressWarnings({"PMD.LawOfDemeter", "PMD.ExcessiveImports"})
class TemplateHandlerTest {
    /**
     * Log4J Logger.
     */
    private static final Logger logger = LogManager.getLogger();

    /**
     * Class instance.
     */
    private TemplateHandler handler;

    /**
     * TemplateProcesser.
     */
    @Inject
    private final TemplateProcesser processer = Mockito.mock(TemplateProcesser.class);

    /**
     * TemplateProcesser.
     */
    @Inject
    private final FileReader reader = Mockito.mock(FileReader.class);

    /**
     * Guice injector.
     */
    private final Injector injector = Guice.createInjector(new AbstractModule() {
        @Override
        protected void configure() {
            bind(TemplateProcesser.class)
                    .annotatedWith(Names.named("TemplateProcesser"))
                    .toInstance(processer);
            bind(FileReader.class)
                    .annotatedWith(Names.named("FileReader"))
                    .toInstance(reader);
        }
    });

    /**
     * Keyword to reference a template in a page datas.
     */
    private static final String TEMPLATE_KEY = "template";


    /**
     * Default template name.
     */
    private static final String USER_TEMPLATE_NAME = "index.html";

    /**
     * Default template name.
     */
    private static final String DEFAULT_TEMPLATE_NAME = "default.html";


    /**
     * Some template content to use as mocking result.
     */
    private static final String SOME_TEMPLATE_CONTENT = """
        <html>
            <head>
                <title>%s</title>
            </head>
            <body>
                <p>%s</p>
            </body>
        </html>
        """;

    /**
     * Tests directory.
     */
    private static final String TEST_DIR = "./";

    /**
     * Templates directory.
     */
    private static final String TEMPLATE_DIR = "template/";

    /**
     * create an empty directory on disk.
     *
     * @param caller the name of the test function calling.
     * @param directory the directory to create.
     */
    static void createDirectoryOnDisk(String caller, String directory) {
        Path path = Path.of(directory);
        File dir = path.toFile();
        
        try {
            if (dir.isFile()) {
                logger.info("{}(): removing file {} to replace it with a directory",
                        caller, directory);
                Files.delete(path);
            }
            if (!dir.exists()) {
                logger.info("{}(): attempt to create test directory {}", caller, directory);
                Files.createDirectory(path);
            }
        } catch (IOException e) {
            logger.error("{}(): exception raised", caller, e);
            fail(e);
        }
    }

    /**
     * create an empty file on disk.
     *
     * @param caller the name of the test function calling.
     * @param directory the directory path where the file is created.
     * @param filename name of the file.
     */
    static void createFileOnDisk(String caller, String directory, String filename) {
        Path path = Path.of(directory, filename);
        File file = path.toFile();

        try {
            if (file.isDirectory()) {
                logger.info("{}(): attempt to remove directory {} to replace it with a directory",
                        caller, filename);
                Files.delete(path);
            }
            if (!file.exists()) {
                logger.info("{}(): attempt to write test file {}", caller, file);
                if (file.createNewFile()) {
                    logger.info("{}(): {} successfully created", caller, file);
                } else {
                    logger.info("{}(): error while creating {}", caller, file);
                    fail("%s unsucessfully created".formatted(filename));
                }
            }
        } catch (IOException e) {
            logger.error("{}(): exception raised {}", caller, e);
            fail(e);
        }
    }

    /**
     * remove a file from the disk.
     *
     * @param caller the name of the test function calling.
     * @param directory the file's directory path.
     * @param filename the name of the file.
     */
    private static void removeFileFromDisk(String caller, String directory, String filename) {
        String file = directory + filename;
        Path path = Path.of(file);

        logger.info("{}(): attempt to clear test file {}", caller, file);

        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            logger.error("{}(): exception raised while clearing {}", caller, file, e);
            fail(e);
        }
    }

    /**
     * remove a directory from the disk.
     *
     * @param caller the name of the test function calling.
     * @param dir the directory to remove.
     */
    private static void removeDirectoryFromDisk(String caller, String dir) {
        Path path = Path.of(dir);

        logger.info("{}(): attempt to remove test directory {}", caller, dir);

        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            logger.error("{}(): exception raised while removing {}", caller, dir, e);
            fail(e);
        }
    }

    /**
     * Initialisation.
     */
    @BeforeEach
    void handlerInit() {
        handler = new TemplateHandler(TEST_DIR + TEMPLATE_DIR);
        injector.injectMembers(handler);
    }

    @Test
    void handlingWithMinimalTemplate() {
        String title = "foo";
        String content = "<p> Hello World! </p>";
        String expectedOutput = MINIMAL_TEMPLATE.formatted(title, content);
        PageDraft draft = new PageDraft(null, content, title);
        try {
            assertEquals(expectedOutput, handler.handle(draft),
                    "draft without templates is not translated into the expected HTML content");
        } catch (TemplateHandlerException e) {
            fail("template handling raised unexpected exception", e);
        }
    }

    /**
     * Test class grouping tests that must run in an environment including a template.
     */
    @Nested
    class EnvironmentIncludingTemplateTest {

        @Test
        @BeforeAll
        static void writeTestTemplatesOnDisk() {
            createDirectoryOnDisk("writeTestTemplatesOnDisk", TEST_DIR + TEMPLATE_DIR);
            createFileOnDisk(
                    "writeTestTemplatesOnDisk",
                    TEST_DIR + TEMPLATE_DIR,
                    USER_TEMPLATE_NAME);
        }

        @Test
        void handlingUserSpecifiedTemplate() {
            String content = SOME_TEMPLATE_CONTENT
                .formatted("{{ metadata.title }}", "{{ content }}");
            String expectedOutput = SOME_TEMPLATE_CONTENT
                .formatted("foo", "Hello World!");

            Map<String, TomlValueTypeWrapper> context = new HashMap<>();
            context.put(TEMPLATE_KEY, new TomlStringWrapper(USER_TEMPLATE_NAME));

            PageDraft draft = new PageDraft(context, "", "");
            try {
                when(reader.read(TEST_DIR + TEMPLATE_DIR + USER_TEMPLATE_NAME))
                    .thenReturn(content);
                when(processer.process(eq(content), anyMap()))
                    .thenReturn(expectedOutput);
                assertEquals(expectedOutput, handler.handle(draft),
                        "template handling did not process the template as expected");
            } catch (TemplateHandlerException | TemplateProcesserException | IOException e) {
                fail("Unexpected exception was thrown", e);
            }
        }

        @Test
        void handlingMinimalTemplateInEnvironmentWithUserSpecifiedTemplate() {
            String title = "foo";
            String content = "bar";
            PageDraft draft = new PageDraft(new HashMap(), content, title);
            String expectedOutput = MINIMAL_TEMPLATE.formatted(title, content);

            try {
                assertEquals(expectedOutput, handler.handle(draft),
                        "template handling did not process the minimal template as expected");
            } catch (TemplateHandlerException e) {
                fail("Unexpected exception was thrown", e);
            }
        }

        @Test
        void handlingPathAboveWorkingDirectory() {
            String templatePath = "../foo.html";
            Map<String, TomlValueTypeWrapper> context = new HashMap<>();
            context.put(TEMPLATE_KEY, new TomlStringWrapper(templatePath));

            PageDraft draft = new PageDraft(context, "", "");

            assertThrows(TemplateHandlerException.class,
                    () -> handler.handle(draft),
                    "attempt to process incorect template didn't throw the expected exception");
        }

        @Test
        @AfterAll
        static void removeTestTemplatesFromDisk() {
            removeFileFromDisk(
                    "removeDefaultTemplateFromDisk",
                    TEMPLATE_DIR,
                    USER_TEMPLATE_NAME);
            removeDirectoryFromDisk(
                    "removeDefaultTemplateFromDisk",
                    TEST_DIR + TEMPLATE_DIR);
        }
    }

    /**
     * Test class grouping tests that must run in an environment including a default template.
     */
    @Nested
    class EnvironmentIncludingDefaultTemplateTest {

        @Test
        @BeforeAll
        static void writeTestTemplatesOnDisk() {
            createDirectoryOnDisk("writeTestTemplatesOnDisk", TEST_DIR + TEMPLATE_DIR);
            createFileOnDisk(
                    "writeTestTemplatesOnDisk",
                    TEST_DIR + TEMPLATE_DIR,
                    DEFAULT_TEMPLATE_NAME);
            createFileOnDisk(
                    "writeTestTemplatesOnDisk",
                    TEST_DIR + TEMPLATE_DIR,
                    USER_TEMPLATE_NAME);
        }

        @Test
        void handlingWithDefaultTemplate() {
            String content = SOME_TEMPLATE_CONTENT
                .formatted("{{ metadata.title }}", "{{ content }}");
            String expectedOutput = SOME_TEMPLATE_CONTENT
                .formatted("foo", "Hello World!");

            PageDraft draft = new PageDraft(null, "", "");
            try {
                when(reader.read(TEST_DIR + TEMPLATE_DIR + DEFAULT_TEMPLATE_NAME))
                    .thenReturn(content);
                when(processer.process(eq(content), anyMap()))
                    .thenReturn(expectedOutput);
                assertEquals(expectedOutput, handler.handle(draft),
                        "template handling did not process the default template as expected");
            } catch (TemplateHandlerException | TemplateProcesserException | IOException e) {
                fail("Unexpected exception was thrown", e);
            }
        }

        @Test
        void handlingUserSpecifiedTemplateInEnvironmentWithDefaultTemplate() {
            String content = SOME_TEMPLATE_CONTENT
                .formatted("{{ metadata.title }}", "{{ content }}");
            String expectedOutput = SOME_TEMPLATE_CONTENT
                .formatted("foo", "Hello World!");

            Map<String, TomlValueTypeWrapper> data = new HashMap<>();
            data.put(TEMPLATE_KEY, new TomlStringWrapper(USER_TEMPLATE_NAME));

            PageDraft draft = new PageDraft(data, "", "");
            try {
                when(reader.read(TEST_DIR + TEMPLATE_DIR + USER_TEMPLATE_NAME))
                    .thenReturn(content);
                when(processer.process(eq(content), anyMap()))
                    .thenReturn(expectedOutput);
                assertEquals(expectedOutput, handler.handle(draft),
                        "template handling didn't process the user specified template as expected");
            } catch (TemplateHandlerException | TemplateProcesserException | IOException e) {
                fail("Unexpected exception was thrown", e);
            }
        }

        @Test
        void templateProcesserExceptionWhileHandlingWithDefaultTemplate() {
            String content = SOME_TEMPLATE_CONTENT
                .formatted("{{ metadata.title }}", "{{ content }}");

            PageDraft draft = new PageDraft(null, content, "");
            try {
                when(reader.read(TEST_DIR + TEMPLATE_DIR + DEFAULT_TEMPLATE_NAME))
                    .thenReturn(content);
                when(processer.process(eq(content), anyMap()))
                    .thenThrow(new TemplateProcesserException("error"));
                assertThrows(TemplateHandlerException.class,
                        () -> handler.handle(draft),
                        "attempt to process template did not throw the expected exception");
            } catch (TemplateProcesserException | IOException e) {
                fail("attempt to process template is supposed to throw exception", e);
            }
        }

        @Test
        void ioExceptionWhileHandlingWithDefaultTemplate() {
            PageDraft draft = new PageDraft(null, "", "");
            try {
                when(reader.read(TEST_DIR + TEMPLATE_DIR + DEFAULT_TEMPLATE_NAME))
                    .thenThrow(new IOException());
                assertThrows(TemplateHandlerException.class,
                        () -> handler.handle(draft),
                        "attempt to read default template did not throw the expected exception");
            } catch (IOException e) {
                fail("attempt to process default template is supposed to throw exception", e);
            }
        }

        @Test
        void handlingUserSpecifiedTemplateInEnvironmentWithDefaultTemplateRaiseException1() {
            Map<String, TomlValueTypeWrapper> data = new HashMap<>();
            data.put(TEMPLATE_KEY, new TomlStringWrapper(USER_TEMPLATE_NAME));

            PageDraft draft = new PageDraft(data, "", "");
            try {
                when(reader.read(TEST_DIR + TEMPLATE_DIR + USER_TEMPLATE_NAME))
                    .thenThrow(new IOException());
                assertThrows(TemplateHandlerException.class,
                        () -> handler.handle(draft),
                        "attempt to process template did not throw the expected exception");
            } catch (IOException e) {
                fail("Unexpected exception was thrown", e);
            }
        }

        @Test
        void handlingUserSpecifiedTemplateInEnvironmentWithDefaultTemplateRaiseException2() {
            String content = SOME_TEMPLATE_CONTENT
                .formatted("{{ metadata.title }}", "{{ content }}");

            Map<String, TomlValueTypeWrapper> data = new HashMap<>();
            data.put(TEMPLATE_KEY, new TomlStringWrapper(USER_TEMPLATE_NAME));

            PageDraft draft = new PageDraft(data, "", "");
            try {
                when(reader.read(TEST_DIR + TEMPLATE_DIR + USER_TEMPLATE_NAME))
                    .thenReturn(content);
                when(processer.process(eq(content), anyMap()))
                    .thenThrow(new TemplateProcesserException("error"));
                assertThrows(TemplateHandlerException.class,
                        () -> handler.handle(draft),
                        "attempt to process template did not throw the expected exception");
            } catch (TemplateProcesserException | IOException e) {
                fail("attempt to process template is supposed to throw exception", e);
            }
        }

        @Test
        @AfterAll
        static void removeTestTemplatesFromDisk() {
            removeFileFromDisk(
                    "removeDefaultTemplateFromDisk",
                    TEMPLATE_DIR,
                    DEFAULT_TEMPLATE_NAME);
            removeFileFromDisk(
                    "removeDefaultTemplateFromDisk",
                    TEMPLATE_DIR,
                    USER_TEMPLATE_NAME);
            removeDirectoryFromDisk(
                    "removeDefaultTemplateFromDisk",
                    TEST_DIR + TEMPLATE_DIR);
        }
    }
}
