package ssg.filemodifiedmanager;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


/**
 * Junit test class for FileModifierManagerImplementation.
 */
class FileModifiedManagerTest {

    /**
     * Log4J Logger.
     */
    private static final Logger logger = LogManager.getLogger();

    /**
     * Reader instance that is used for testing.
     */
    private static final FileModifiedManagerImplementation fileModifiedManagerImplementation =
            new FileModifiedManagerImplementation();

    /**
     * path for non created file.
     */
    static final String nonExistentFilePath = "notCreated.md";

    /**
     * path for input file.
     */
    static final String inputFilePath = "input.md";

    /**
     * path for input file.
     */
    static final String inputDirectory = "testInput/";

    /**
     * path for input file.
     */
    static final String content = "content/";

    /**
     * path output file.
     */
    static final String outputFilePath = "input.html";

    /**
     * path outputDirectory.
     */
    static final String outputDirectory = "outputTest/";

    /**
     * Earlier local date.
     */
    static final LocalDate localDateAnterior = LocalDate.of(1997, 12, 30);

    /**
     * Later local date.
     */
    static final LocalDate localDatePosterior = LocalDate.of(1997, 12, 31);

    /**
     * Earlier instant.
     */
    static final Instant instantAnterior =
            localDateAnterior.atStartOfDay(ZoneId.systemDefault()).toInstant();

    /**
     * Posterior instant.
     */
    static final Instant instantPosterior =
            localDatePosterior.atStartOfDay(ZoneId.systemDefault()).toInstant();


    /**
     * Creating files and directories for testing purposes.
     */

    @BeforeAll
    static void initTests() {

        try {
            cleanTest();
            Files.createDirectory(Path.of(outputDirectory));
            Files.createDirectory(Path.of(inputDirectory));
            Files.createDirectory(Path.of(inputDirectory + content));
            Files.createDirectory(Path.of(outputDirectory + content));
            Files.createFile(Path.of(outputDirectory + content + outputFilePath));
            Files.createDirectory(Path.of(inputDirectory + content + inputFilePath));
        } catch (IOException e) {
            logger.error("FileModifiedManagerTest : exception occured when "
                    + "creating tests files and directories", e);
            fail(e);
        }


    }

    /**
     * Deleting all files and directories created for testing purposes.
     */
    @AfterAll
    static void cleanTest() {
        try {
            Files.deleteIfExists(Path.of(inputDirectory + content + inputFilePath));
            Files.deleteIfExists(Path.of(outputDirectory + content + outputFilePath));
            Files.deleteIfExists(Path.of(outputDirectory + content));
            Files.deleteIfExists(Path.of(inputDirectory + content));
            Files.deleteIfExists(Path.of(inputDirectory));
            Files.deleteIfExists(Path.of(outputDirectory));
        } catch (IOException e) {
            logger.error("FileModifiedManagerTest : exception occured when "
                    + "deleting tests files and directories", e);
            fail(e);
        }
    }



    /**
     * Testing that if input file was last modified
     * before output file then file exists returns false.
     */
    @Test
    void fileModifiedShouldReturnFalse() {

        try {
            Files.setLastModifiedTime(Path.of(inputDirectory + content
                    + inputFilePath), FileTime.from(instantAnterior));
            Files.setLastModifiedTime(Path.of(outputDirectory + content + outputFilePath),
                    FileTime.from(instantPosterior));

            assertFalse(fileModifiedManagerImplementation
                    .wasFileModified(inputDirectory + content + inputFilePath,outputDirectory),
                    "wasFileModified should have returned false but got true");
        } catch (IOException e) {

            logger.error("FileModifiedManagerTest : exception occured when "
                    + "setting last modified time on test files", e);
            fail(e);
        }
    }

    /**
     * Testing that if input file was last modified after output file then file exists returns true.
     */
    @Test
    void fileModifiedShouldReturnTrue() {

        try {
            Files.setLastModifiedTime(Path.of(inputDirectory
                    + content + inputFilePath), FileTime.from(instantPosterior));
            Files.setLastModifiedTime(Path.of(outputDirectory + content + outputFilePath),
                    FileTime.from(instantAnterior));

            assertTrue(fileModifiedManagerImplementation
                    .wasFileModified(inputDirectory + content + inputFilePath,outputDirectory),
                    "wasFileModified should have returned true but got false");
        } catch (IOException e) {

            logger.error("FileModifiedManagerTest : exception occured when "
                    + "setting last modified time on test files", e);
            fail(e);
        }
    }

    /**
     * Checking if an existing files exists should return true.
     */
    @Test
    void fileModifiedOnNonExistingFileReturnsTrue() {
        try {
            assertTrue(fileModifiedManagerImplementation
                    .wasFileModified(nonExistentFilePath, outputDirectory),
                    "wasFileModified should have returned false but got true");
        } catch (IOException e) {
            logger.error("FileModifiedManagerTest : exception occured when "
                    + "running wasFileModified ", e);
            fail(e);
        }

    }


}
