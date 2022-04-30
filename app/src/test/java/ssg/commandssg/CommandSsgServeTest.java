package ssg.commandssg;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import picocli.CommandLine;

/**
 * Junit test class for CommandSsgServe.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CommandSsgServeTest {
    /**
     * Log4j Logger.
     */
    static final Logger logger = LogManager.getLogger();

    /**
     * Argument provider for parametrized test.
     *
     * @return Stream Arguments
     */
    static Stream<Arguments> argumentsProvider() {
        return Stream.of(
                Arguments.of((Object)
                        new String[]{"--output-dir=testDir/", "out1.html", "out2.html"}),
                Arguments.of((Object)
                        new String[]{"out1.html","--output-dir=testDir/", "out2.html"}),
                Arguments.of((Object)
                        new String[]{"out1.html", "out2.html", "--output-dir=testDir/"})
        );
    }


    /**
     * Test default value of output directory.
     */
    @Test
    void defaultOutputDirTest() {
        CommandSsgServe commandeSsgServe = new CommandSsgServe();
        new CommandLine(commandeSsgServe).execute();
        assertEquals("_output/",commandeSsgServe.getOutputDir(),
                "no output directory was given but didn't get default value");
    }

    /**
     * Test specified value of output directory.
     */
    @Test
    void getOutputDirValueTest() {
        CommandSsgServe commandeSsgServe = new CommandSsgServe();
        new CommandLine(commandeSsgServe).execute("--output-dir=testDir/");
        assertEquals("testDir/",commandeSsgServe.getOutputDir(),
                "output dir was specified but didn't get the expected value");
    }


    /**
     * Test positional parameter files are stored properly.
     */
    @SuppressWarnings("PMD.JUnitTestContainsTooManyAsserts")
    @Test
    void storeFileParameterTest() {
        CommandSsgServe commandeSsgServe = new CommandSsgServe();
        new CommandLine(commandeSsgServe).execute("out1.html", "out2.html");
        assertEquals("out1.html",commandeSsgServe.getFile(0),
                "Expected out1.html as the first file but didn't get that");
        assertEquals("out2.html",commandeSsgServe.getFile(1),
                "Expected out2.html as the first file but didn't get that");

    }

    /**
     * Test positional parameter files are stored properly.
     */
    @SuppressWarnings("PMD.JUnitTestContainsTooManyAsserts")
    @ParameterizedTest
    @MethodSource("argumentsProvider")
    void parametersPlusOptionTest(String... args) {
        CommandSsgServe commandeSsgServe = new CommandSsgServe();
        new CommandLine(commandeSsgServe).execute(args);
        assertEquals("out1.html",commandeSsgServe.getFile(0),
                "Expected out1.html as the first file but didn't get that");
        assertEquals("out2.html",commandeSsgServe.getFile(1),
                "Expected out2.html as the first file but didn't get that");
        assertEquals("testDir/",commandeSsgServe.getOutputDir(),
                "output dir was specified but didn't get the expected value");

    }

    @AfterAll
    void cleanDirectories() {
        try {
            Files.deleteIfExists(Path.of("testDir/dependencies.toml"));
            Files.deleteIfExists(Path.of("testDir/"));
            Files.deleteIfExists(Path.of("_output/"));
        } catch (IOException e) {
            fail(e);
        }
    }
}
