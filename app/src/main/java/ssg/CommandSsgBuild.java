package ssg;

import com.google.common.collect.ImmutableList;
import java.nio.file.Path;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;


/**
 * CommandSsgBuild class.
 */
@SuppressWarnings({"PMD.UnusedPrivateField", "PMD.ImmutableField"})
@CommandLine.Command(name = "build")
public class CommandSsgBuild implements Runnable {

    /**
     * Logger.
     */
    private static final Logger logger = LogManager.getLogger();

    /**
     * Output Directory.
     */
    @CommandLine.Option(names = {"--output-dir"},
            description = "output directory, default is _output/")
    private Path outputDir = Path.of("_output/");


    /**
     * List of .html files expected as output.
     */
    @CommandLine.Parameters(arity = "1..*", description = "at least one expected .html output file")
    private List<Path> files;

    /**
     * Output Directory getter.
     *
     * @return output directory
     */
    public Path getOutputDir() {
        return this.outputDir;
    }


    /**
     * Files at index getter.
     *
     * @param index index of files you want to access
     *
     * @return path at specific index expected as output
     */
    public Path getFile(int index) {
        return files.get(index);
    }

    /**
     * Runs the command.
     */
    @Override
    @SuppressWarnings("PMD.GuardLogStatement")
    public void run() {

        logger.info("ssg build subcommand called");
        // HERE THE CODE YOU WANT TO RUN WITH THE BUILD COMMAND
    }
}