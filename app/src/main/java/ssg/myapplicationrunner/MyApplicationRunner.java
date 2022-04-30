package ssg.myapplicationrunner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;
import ssg.commandssg.CommandSsg;

/**
 * MyApplicationRunner class.
 */
public class MyApplicationRunner {

    /**
     * Logger.
     */
    private static final Logger logger = LogManager.getLogger();

    /**
     * Exit code of the command.
     */
    private int exitCode;

    /**
     * The command to run.
     */
    private final CommandSsg myCommand;

    /**
     * Command Line arguments.
     */
    private final String[] commandLineArgs;

    /**
     * Constructor.
     *
     * @param args command line arguments.
     */
    @SuppressWarnings("PMD.UseVarargs")
    public MyApplicationRunner(String[] args) {
        this.myCommand = new CommandSsg();
        this.commandLineArgs = args.clone();
    }

    /**
     * Return the run command exit code.
     *
     * @return int representing the attribute exitCode.
     */
    public int getExitCode() {
        return exitCode;
    }

    /**
     * Runs the command line.
     */
    public void run() {
        logger.info("Executing command and getting exit code from MyApplicationRunner");
        exitCode = new CommandLine(myCommand).execute(commandLineArgs);
    }
}
