package ssg.commandssg;

import ssg.Generated;

/**
 * Utils Class.
 */
@Generated
public final class Utils {

    /**
     * Private constructor to avoid instantiation.
     */
    private Utils() {

    }

    /**
     * Add a backslash at the end of a path if it doesn't have one.
     *
     * @param path a string representing a path.
     * @return a path with a backslash at the end.
     */
    public static String addBackSlashes(String path) {
        if (!path.endsWith("/")) {
            return path + "/";
        }
        return path;
    }
}
