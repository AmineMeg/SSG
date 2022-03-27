package ssg.filewriter;

import java.io.IOException;

/**
 * write string content in file text to disk.
 */
public interface FileWriter {

    /**
     * write string content in file text to disk.
     *
     * @param file file to write on disk.
     * @param content content to write on file.
     * @throws IOException if an error happened while writing file.
     */
    void write(String file, String content) throws IOException;
}