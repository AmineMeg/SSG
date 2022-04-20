package ssg.filemodifiedmanager;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
            Path path = Paths.get(outputDirectory);
            Files.createDirectories(path);

            File inputFile = new File(inputFilePath);
            boolean wentOkay = inputFile.createNewFile();

            if (!wentOkay) {
                fail("Error when creating file" + inputFilePath);
            }

            File outputFile = new File(outputDirectory + outputFilePath);
            wentOkay = outputFile.createNewFile();

            if (!wentOkay) {
                fail("Error when creating file" + outputDirectory + outputFilePath);
            }


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


        File inputFile = new File(inputFilePath);
        boolean wentOkay = inputFile.delete();

        if (!wentOkay) {
            fail("Error when deleting file file" + inputFilePath);
        }

        File outputFile = new File(outputDirectory + outputFilePath);
        wentOkay = outputFile.delete();

        if (!wentOkay) {
            fail("Error when deleting file"
                    + outputDirectory + outputFilePath);
        }

        File outputDir = new File(outputDirectory);
        wentOkay = outputDir.delete();

        if (!wentOkay) {
            fail("Error when deleting directory" + outputDirectory);
        }
    }


    /**
     * Checking if a non-existing files exists does return false.
     */
    @Test
    void readingMissingFileReturnsFalse() {
        assertFalse(fileModifiedManagerImplementation.doesFileExists(nonExistentFilePath),
                "doesFileExists should have returned false but got true");
    }

    /**
     * Checking if an existing files exists should return true.
     */
    @Test
    void readingExistingFileReturnsTrue() {
        assertTrue(fileModifiedManagerImplementation.doesFileExists(inputFilePath),
                "doesFileExists should have returned true but got false");
    }

    /**
     * Testing that if input file was last modified
     * before output file then file exists returns false.
     */
    @Test
    void fileModifiedShouldReturnFalse() {

        try {
            Files.setLastModifiedTime(Path.of(inputFilePath), FileTime.from(instantAnterior));
            Files.setLastModifiedTime(Path.of(outputDirectory + outputFilePath),
                    FileTime.from(instantPosterior));

            assertFalse(fileModifiedManagerImplementation
                    .wasFileModified(inputFilePath,outputDirectory),
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
            Files.setLastModifiedTime(Path.of(inputFilePath), FileTime.from(instantPosterior));
            Files.setLastModifiedTime(Path.of(outputDirectory + outputFilePath),
                    FileTime.from(instantAnterior));

            assertTrue(fileModifiedManagerImplementation
                    .wasFileModified(inputFilePath,outputDirectory),
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
    void fileModifiedOnNonExistingFileReturnsFalse() {
        try {
            assertFalse(fileModifiedManagerImplementation
                    .wasFileModified(nonExistentFilePath, outputDirectory),
                    "wasFileModified should have returned false but got true");
        } catch (IOException e) {
            logger.error("FileModifiedManagerTest : exception occured when "
                    + "running wasFileModified ", e);
            fail(e);
        }

    }


}
