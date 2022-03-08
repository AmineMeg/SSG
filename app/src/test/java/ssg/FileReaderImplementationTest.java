package ssg;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Junit test class for FileReaderImplementation.
 */
class FileReaderImplementationTest {

    /**
     * filename for testFile.
     */
    static final String testFileName = "test.txt";

    /**
     * content for testFile.
     */
    static final String testFileContent = """
            # Heading level 1
            ## Heading level 2
            ### Heading level 3
            #### Heading level 4
            ##### Heading level 5
            ###### Heading level 6"
            """;

    /**
     * filename for emptyFile.
     */
    static final String emptyFileName = "empty.md";

    /**
     * path for test files.
     */
    static final String testFilesPath = "build/";

    /**
     * Reader instance that is used for testing.
     */
    private static final FileReaderImplementation reader = new FileReaderImplementation();

    /**
     * Log4J Logger.
     */
    private static final Logger logger = LogManager.getLogger();

    /**
     * Attempt to read a missing file.
     */
    @Test
    void readingMissingFileThrowsException() {
        assertThrows(IOException.class,
            () -> reader.read("missingFile.md"),
            "attempt to read missing file did not raise IOException");
    }

    /**
     * Writing testFile on disk.
     */
    @BeforeAll
    public static void initTestFile() {
        String file = testFilesPath + testFileName;
        Path path = Paths.get(file);

        logger.info("initTestFile(): attempt to write test file {}", file);

        try {
            Files.deleteIfExists(path);
            Files.writeString(path, testFileContent, StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            logger.error("initTestFile(): exception raised while writing {}", file, e);
            fail(e);
        }
    }

    /**
     * read content must equal file content.
     */
    @Test
    void readContentEqualsFileContent() {
        String file = testFilesPath + testFileName;

        logger.info("readContentEqualsFileContent(): attempt to read test file {}", file);

        try {
            String content = reader.read(file);
            assertEquals(testFileContent, content, "read content did not match file content");
        } catch (IOException e) {
            logger.error("readContentEqualsFileContent():"
                    + " exception raised while reading {}", file, e);
            fail(e);
        }
    }

    /**
     * Removing testFile from disk.
     */
    @AfterAll
    public static void cleaningTestFile() {
        String file = testFilesPath + testFileName;
        Path path = Paths.get(file);

        logger.info("cleaningTestFile(): attempt to clear test file {}", file);

        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            logger.error("cleaningTestFile(): exception raised while cleaning {}", file, e);
            fail(e);
        }
    }

    /**
     * Writing emptyFile on disk.
     */
    @BeforeAll
    public static void initEmptyFile() {
        String file = testFilesPath + emptyFileName; 
        Path path = Paths.get(file);

        logger.info("initEmptyFile(): attempt to write test file {}", file);

        try {
            Files.deleteIfExists(path);
            Files.writeString(path, "", StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            logger.error("initEmptyFile(): exception raised while writing {}", file, e);
            fail(e);
        }
    }
    
    /**
     * empty file read content must equal file content.
     */
    @Test
    void emptyFileReadContentEqualsEmptyString() {
        String file = testFilesPath + emptyFileName;

        logger.info("emptyFileReadContentEqualsFileContent(): attempt to read test file {}", file);

        try {
            String content = reader.read(file);
            assertEquals("", content, "read content did not match file content");
        } catch (IOException e) {
            logger.error("emptyFileReadContentEqualsFileContent():"
                    + " exception raised while reading {}", file, e);
            fail(e);
        }
    }

    /**
     * Removing emptyFile from disk.
     */
    @AfterAll
    public static void cleanTestFile() {
        String file = testFilesPath + emptyFileName;
        Path path = Paths.get(file);

        logger.info("cleanTestFile(): attempt to clear test file {}", file);

        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            logger.error("cleanTestFile(): exception raised while cleaning {}", file, e);
            fail(e);
        }
    }
}
