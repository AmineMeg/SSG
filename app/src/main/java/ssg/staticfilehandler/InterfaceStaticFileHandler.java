package ssg.staticfilehandler;

import java.io.IOException;

/**
 * Handle the static directory.
 */
public interface InterfaceStaticFileHandler {

    /**
     * Copy static directory.
     *
     * @param staticSrcDirectory static source directory.
     * @param staticDstDirectory static destination directory.
     * @throws IOException when there is a problem with the static directory.
     */
    void handle(String staticSrcDirectory, String staticDstDirectory) throws IOException;
}
