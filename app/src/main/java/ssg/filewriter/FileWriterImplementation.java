package ssg.filewriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * write string content in file text to disk.
 */
public class FileWriterImplementation implements FileWriter {

    /**
     * log4J.
     */
    private static final Logger logger = LogManager.getLogger(FileWriterImplementation.class);

    /**
     * write string content in file text to disk.
     *
     * @param file file to write on disk.
     * @param content content to write on file.
     * @throws IOException if an error happened while writing file.
     */

    @Override
    public void write(String file, String content) throws IOException {
        logger.info("write(): {} attempt to write file ",file);
        try {
            Files.writeString(Path.of(file), content,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            logger.info("write() : {} was successfully write ",file);
        } catch (IOException e) {
            logger.error("write(): exception raised while writing {}", file, e);
            throw e;
        }
    }
}
