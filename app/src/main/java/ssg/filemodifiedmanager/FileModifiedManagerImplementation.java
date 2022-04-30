package ssg.filemodifiedmanager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ssg.Generated;
import ssg.config.SiteStructureVariable;

/**
 * Implementation of FileModifiedManager.
 */
@Generated
@SuppressWarnings("PMD.LawOfDemeter")
public class FileModifiedManagerImplementation implements FileModifiedManager {

    /**
     * Logger.
     */
    private static final Logger logger = LogManager.getLogger();

    /**
     * Constant path_separator.
     */
    public static final String PATH_SEPARATOR = "/";

    /**
     * Determines if a source file was modified since it was last computed.
     *
     * @param path the file that we will analyze to determine if it was modified.
     * @param outputDirectory the expected outputDirectory for computed files.
     * @return true if the file was modified otherwise false.
     * @throws IOException when file didn't exist or couldn't get the last modified time.
     */
    @Override
    public boolean wasFileModified(String path, String outputDirectory) throws IOException {

        logger.info("wasFileModified() : determining if file {} "
                + "was modified since last build", path);
        Path pathTypePath = Path.of(path);
        if (Files.exists(pathTypePath)) {
            try {
                FileTime inputFileTime = Files.getLastModifiedTime(pathTypePath);
                File file = new File(FilenameUtils.getName(path));

                String fileOutputName = Path.of(file.getName()).toString()
                        .replace(".md", ".html");

                int i = 0;
                for (String subPath : path.split(PATH_SEPARATOR)) {
                    if (SiteStructureVariable.RAW_CONTENTS.equals(subPath)) {
                        break;
                    }
                    i++;
                }

                Path base = pathTypePath.subpath(i, pathTypePath.getNameCount() - 1);
                Path pathOutputName = Path.of(outputDirectory
                        + base + PATH_SEPARATOR + fileOutputName);

                if (Files.exists(pathOutputName)) {

                    FileTime outputFileTime = Files.getLastModifiedTime(pathOutputName);
                    return inputFileTime.compareTo(outputFileTime) > 0;
                } else {
                    logger.info("wasFileModified() : file {} didn't exists", pathOutputName);
                    return true;
                }
            } catch (IOException e) {
                logger.error("wasFileModified() : Couldn't get the last modified time ", e);
                throw e;
            }
        }

        return true;
    }

}
