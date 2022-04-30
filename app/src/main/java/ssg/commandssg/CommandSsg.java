package ssg.commandssg;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;
import ssg.Generated;


/**
 * CommandSsg class.
 */
@Generated
@CommandLine.Command(name = "ssg", description = "ssg main command",
        mixinStandardHelpOptions = true,
        subcommands = {CommandSsgBuild.class, CommandSsgServe.class})
public class CommandSsg implements Runnable {

    /**
     * Logger.
     */
    private static final Logger logger = LogManager.getLogger();

    /**
     * Runs the command.
     */
    @Override
    public void run() {
        logger.info("ssg command called");
    }


}
