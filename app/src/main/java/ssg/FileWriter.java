package ssg;

import java.io.IOException;
import java.nio.file.Path;

/**
 * write string content in file text to disk
 */
public abstract class FileWriter {

    /**
     * write string content in file text to disk
     *
     * @param file file to write on disk
     *
     * @param content content to write on file
     *
     * @throws IOException if an error happened while writing file
     */
    public abstract void write(String file, String content) throws IOException;
}