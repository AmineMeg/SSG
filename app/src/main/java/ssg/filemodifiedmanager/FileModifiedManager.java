package ssg.filemodifiedmanager;

import java.io.IOException;

/**
 * Interface to determine if a file exists of was updated since the last ssg build.
 */
public interface FileModifiedManager {

    /**
     * Determines if a source file was modified since it was last computed.
     *
     * @param path the file that we will analyze to determine if it was modified.
     * @param outputDirectory the expected outputDirectory for computed files.
     * @return true if the file was modified otherwise false.
     * @throws IOException when couldn't get the last modified time.
     */
    boolean wasFileModified(String path, String outputDirectory) throws IOException;


}
