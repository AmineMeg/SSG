package ssg;

import java.io.IOException;

/**
 * Reads file text content from disk to string.
 */
public abstract class FileReader {

    /**
     * reads file text content from disk to string.
     *
     * @param file file to read on disk
     *
     * @return file textual content as String
     *
     * @throws IOException if an error happened while reading file
     */
    public abstract String read(String file) throws IOException;
}

