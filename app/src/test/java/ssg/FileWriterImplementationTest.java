package ssg;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;


/**
 * JUnit test class for FileWriterImplementation
 */
class FileWriterImplementationTest {
    /**
     * filename for testFile
     */
    static final String testFilesName = "test.txt";

    /**
     * content for testFile
     */
    static final String testFileContent =
            "<h1> Heading level 1 </h1>"+
            "<h2> Heading level 2 </h2>"+
            "<h3> Heading level 3 </h3>"+
            "<h4> Heading level 4 </h4>"+
            "<h5> Heading level 5 </h5>"+
            "<h6> Heading level 6 </h6>";

    /**
     * content empty for empty File
     */
    static final String empty = "";
    /**
     * filename for emptyFile
     */
    static final String emptyFileName = "empty.html";

    /**
     * path for test files
     */
    static final String testFilesPath = "build/";

    /**
     * Writer instance that is used for testing
     */
    private static final FileWriterImplementation writer = new FileWriterImplementation();

    /**
     * Log4J Logger
     */
    private static final Logger logger = LogManager.getLogger(FileWriterImplementation.class);

    /**
     * Writing testFile on disk.
     */
    @BeforeAll
    public static void initTestFile(){
        String file = testFilesPath + testFilesName;
        Path path = Paths.get(file);

        logger.info("initTestFile(): attemps to write test file {}",file);

        try {
            Files.deleteIfExists(path);
            writer.write(file,testFileContent);
        } catch (IOException e) {
            logger.error("initTestFile(): exception raised while writing {}",file,e);
            fail(e);
        }
    }

    /**
     * write content must equal testFileContent
     */
    @Test
    void writeContentEqualsReadContent() {
        String file = testFilesPath + testFilesName;
        Path path = Paths.get(file);

        logger.info("writeContentEqualsFileContent(): attempt to write test file {}", file);

        try{
            String content = Files.readString(path);
            assertEquals(testFileContent,content,"write content did not read content");
        } catch (IOException e) {
            logger.error("writeContentEqualsReadContent"
                    + " exception raised while writing{}", file, e);
            fail(e);
        }
    }

    /**
     * Removing testFile from disk
     */
    @AfterAll
    public static void cleaningTestFile(){
        String file = testFilesPath + testFilesName;
        Path path = Paths.get(file);

        logger.info("cleaningTestFile(): attempt to clear test file{}",file);

        try{
            Files.deleteIfExists(path);
        } catch (IOException e) {
            logger.error("cleaningTestfile(): exception raised while cleaning{}",file, e);
            fail(e);
        }
    }

    /**
     * Writing emptyFile on disk
     */
    @BeforeAll
    public static void initEmptyFile(){
        String file = testFilesPath + emptyFileName;
        Path path = Paths.get(file);

        logger.info("initEmpty(): attempt to write test files{}",file);

        try{
            Files.deleteIfExists(path);
            writer.write(file,empty);
        } catch (IOException e) {
            logger.error("initEmptyFile():exception raised while writing {}",file,e);
            fail(e);
        }
    }

    @Test
    void emptyFileWriteContentEqualsReadContent(){
        String file = testFilesPath + emptyFileName;
        Path path = Paths.get(file);

        logger.info("emptyFileWriteContenteEqualsReadContent(): attempt to write test file{}",file);

        try{
            String content = Files.readString(path);
            assertEquals(empty,content,"write content did not match read content");
        } catch (IOException e) {
            logger.error("emptyFileWriteContentEqualsReadContent():"
            + " exception raised while writing {}",file,e);
            fail(e);
        }
    }

    /**
     * Removing emptyFile from disk
     */
    @AfterAll
    public static void cleanTestFile(){
        String file = testFilesPath + emptyFileName;
        Path path = Paths.get(file);

        logger.info("cleanTestFile(): attempt to clear test file {}",file);

        try{
            Files.deleteIfExists(path);
        } catch (IOException e) {
            logger.error("cleanTestfile(): exception raised while cleaning {}",file,e);
            fail(e);
        }
    }


}