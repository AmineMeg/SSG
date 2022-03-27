package ssg.staticfilehandler;

import java.io.IOException;

/**
 * Handle the static directory.
 */
public interface InterfaceStaticFileHandler {

    /**
    * Copy the static directory from source to target.
    *
    * @param staticSrcDirectory static source directory.
    * @param staticDstDirectory static destination directory.
    */
    void handle(String staticSrcDirectory, String staticDstDirectory) throws IOException;
}
