package ssg;

import java.nio.file.Path;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import picocli.CommandLine;

import static org.junit.jupiter.api.Assertions.assertEquals;



/**
 * Junit test class for CommandSsgBuild.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CommandSsgBuildTest {

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
        CommandSsgBuild commandSsgBuild = new CommandSsgBuild();
        new CommandLine(commandSsgBuild).execute();
        assertEquals(Path.of("_output/"),commandSsgBuild.getOutputDir(),
                "no output directory was given but didn't get default value");
    }

    /**
     * Test specified value of output directory.
     */
    @Test
    void getOutputDirValueTest() {
        CommandSsgBuild commandSsgBuild = new CommandSsgBuild();
        new CommandLine(commandSsgBuild).execute("--output-dir=testDir/");
        assertEquals(Path.of("testDir/"),commandSsgBuild.getOutputDir(),
                "output dir was specified but didn't get the expected value");
    }

    /**
     * Test exit code is 2 (Invalid Input) when no input is given.
     */
    @Test
    void exitCodeOnNoInputTest() {
        CommandSsgBuild commandSsgBuild = new CommandSsgBuild();
        int exitCode = new CommandLine(commandSsgBuild).execute();
        assertEquals(2,exitCode,
                "Expected exit code was 2");

    }

    /**
     * Test exit code is 2 (Invalid Input) when no input is given.
     */
    @Test
    void exitCodeOnNoFileSpecifiedTest() {
        CommandSsgBuild commandSsgBuild = new CommandSsgBuild();
        int exitCode = new CommandLine(commandSsgBuild).execute("--output-dir=testDir/");
        assertEquals(2,exitCode,
                "Expected exit code was 2");

    }

    /**
     * Test positional parameter files are stored properly.
     */
    @SuppressWarnings("PMD.JUnitTestContainsTooManyAsserts")
    @Test
    void storeFileParameterTest() {
        CommandSsgBuild commandSsgBuild = new CommandSsgBuild();
        new CommandLine(commandSsgBuild).execute("out1.html", "out2.html");
        assertEquals(Path.of("out1.html"),commandSsgBuild.getFile(0),
                "Expected out1.html as the first file but didn't get that");
        assertEquals(Path.of("out2.html"),commandSsgBuild.getFile(1),
                "Expected out2.html as the first file but didn't get that");

    }

    /**
     * Test positional parameter files are stored properly.
     */
    @SuppressWarnings("PMD.JUnitTestContainsTooManyAsserts")
    @ParameterizedTest
    @MethodSource("argumentsProvider")
    void parametersPlusOptionTest(String... args) {
        CommandSsgBuild commandSsgBuild = new CommandSsgBuild();
        new CommandLine(commandSsgBuild).execute(args);
        assertEquals(Path.of("out1.html"),commandSsgBuild.getFile(0),
                "Expected out1.html as the first file but didn't get that");
        assertEquals(Path.of("out2.html"),commandSsgBuild.getFile(1),
                "Expected out2.html as the first file but didn't get that");
        assertEquals(Path.of("testDir/"),commandSsgBuild.getOutputDir(),
                "output dir was specified but didn't get the expected value");

    }

}
