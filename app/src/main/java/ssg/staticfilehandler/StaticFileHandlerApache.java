package ssg.staticfilehandler;

import static ssg.config.SiteStructureVariable.STATIC;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ssg.Generated;

/**
 * StaticFileHandle implementation using apache.commons.
 */
@SuppressFBWarnings
@Generated
public class StaticFileHandlerApache implements InterfaceStaticFileHandler {

    /**
     * Logger.
     */
    private static final Logger logger = LogManager.getLogger();

    /**
     * Copy static directory.
     *
     * @param staticSrcDirectory static source directory.
     * @param dstDirectory static destination directory.
     * @throws IOException when there is a problem with the static directory.
     */
    @Override
    public void handle(String staticSrcDirectory, String dstDirectory) throws IOException {
        if (!(staticSrcDirectory.endsWith(STATIC))) {
            logger.error("Attempts to copy non static directory");
            return;
        }
        try {
            File sourceDirectory = new File(staticSrcDirectory);
            File destinationDirectory = new File(dstDirectory + STATIC);
            FileUtils.copyDirectory(sourceDirectory, destinationDirectory);
        } catch (IOException e) {
            logger.error("Failed to copy static directory", e);
            throw e;
        }
    }
}
