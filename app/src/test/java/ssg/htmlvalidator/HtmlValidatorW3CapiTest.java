package ssg.htmlvalidator;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for HtmlValidator.
 */
@SuppressWarnings("PMD.AvoidCatchingGenericException")
class HtmlValidatorW3CapiTest {

    /**
     * class to be tested.
     */
    private HtmlValidator htmlvalidator;

    /**
     * random bad HTML content for test.
     */
    static final String badHtmlContent = "<h1> Big title </h1>";

    /**
     * random good HTML content for test.
     */
    static final String goodHtmlContent = "dudu";

    /**
     * filename for testFile.
     */
    static final String testFileName = "test.html";

    /**
     * path for test files.
     */
    static final String testFilesPath = "build/";


    /**
     * Setup the class before each test.
     */
    @BeforeEach
    void setup() {
        htmlvalidator = new HtmlValidatorW3Capi();
        String file = testFilesPath + testFileName;
        Path path = Paths.get(file);

        try {
            Files.deleteIfExists(path);
            Files.writeString(path, badHtmlContent, StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            fail(e);
        }
    }

    @Test
    void validateHtmlShouldLogInfo() {
        String file = testFilesPath + testFileName;
        Path path = Paths.get(file);

        try {
            Files.deleteIfExists(path);
            Files.writeString(path, goodHtmlContent, StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            fail(e);
        }

        try {
            this.htmlvalidator.validateHtml(file);
        } catch (Exception e) {
            fail(e);
        }
    }

    /**
     * Removing emptyFile from disk.
     */
    @AfterAll
    public static void cleanTestFile() {
        String file = testFilesPath + testFileName;
        Path path = Paths.get(file);

        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            fail(e);
        }
    }
}
