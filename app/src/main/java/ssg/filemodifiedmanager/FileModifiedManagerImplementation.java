package ssg.filemodifiedmanager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Implementation of FileModifiedManager.
 */
@SuppressWarnings("PMD.LawOfDemeter")
public class FileModifiedManagerImplementation implements FileModifiedManager {

    /**
     * Logger.
     */
    private static final Logger logger = LogManager.getLogger();

    /**
     * Determines if a source file was modified since it was last computed.
     *
     * @param path the file that we will analyze to determine if it was modified
     * @param outputDirectory the expected outputDirectory for computed files
     * @return true if the file was modified otherwise false
     */
    @Override
    public boolean wasFileModified(String path, String outputDirectory) throws IOException {

        logger.info("FileModifiedManager : determining if file {} "
                + "was modified since last build", path);
        if (doesFileExists(path)) {

            try {
                FileTime inputFileTime = Files.getLastModifiedTime(Path.of(path));
                File file = new File(FilenameUtils.getName(path));
                String fileOutputName = outputDirectory
                        + Path.of(file.getName())
                        .toString()
                        .replace(".md", ".html");

                FileTime outputFileTime = Files.getLastModifiedTime(Path.of(fileOutputName));

                return inputFileTime.compareTo(outputFileTime) > 0;

            } catch (IOException e) {
                logger.error("FileModifiedManager : Couldn't get the last modified time ", e);
                throw e;
            }
        }

        return false;
    }

    /**
     * Determines if a source file exists.
     *
     * @param path the file that we will analyze to determine if it exists
     * @return true if the file exists otherwise false
     */
    @Override
    public boolean doesFileExists(String path) {
        logger.info("FileModifiedManager : determining if file {} exists", path);
        File file = new File(FilenameUtils.getName(path));
        return file.exists();
    }
}
