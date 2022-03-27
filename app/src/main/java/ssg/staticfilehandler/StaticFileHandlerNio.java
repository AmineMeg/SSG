package ssg.staticfilehandler;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ssg.Generated;

/**
 * StaticFileHandle implementation using Java.nio.
 */
@SuppressWarnings("PMD.LawOfDemeter")
@SuppressFBWarnings
@Generated
public class StaticFileHandlerNio implements InterfaceStaticFileHandler {

    /**
     * Log4J Logger.
     */
    private static final Logger logger = LogManager.getLogger();

    /**
     * Copy the static directory from source to target.
     *
     * @param staticSrcDirectory static source directory.
     * @param staticDstDirectory static destination directory.
     */
    @Override
    public void handle(String staticSrcDirectory, String staticDstDirectory) throws IOException {
        Files.walk(Paths.get(staticSrcDirectory))
                .forEach(source -> {
                    Path destination = Paths.get(staticDstDirectory, source.toString()
                            .substring(staticSrcDirectory.length()));
                    try {
                        Files.copy(source, destination);
                    } catch (IOException e) {
                        logger.error("handle(): Error in copying file", e);
                    }
                });
    }
}
