package ssg;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Reads file text content from disk to string.
 */
public class FileReaderImplementation extends FileReader {

    /**
     * Log4J Logger.
     */
    private static final Logger logger = LogManager.getLogger();

    /**
     * reads file text content from disk to string.
     *
     * @param file file to read on disk
     *
     * @return file textual content as String
     *
     * @throws IOException if an error happened while reading file
     */
    @Override
    public String read(String file) throws IOException {
        logger.info("read(): attempt to read file {}", file);
        try {
            String content = Files.readString(Path.of(file));
            logger.info("read(): {} was successfully read", file);
            return content;
        } catch (IOException e) {
            logger.error("read(): exception raised while reading {}", file, e);
            throw e;
        }       
    }
}
