package ssg.exceptions;

/**
 * Exception raised when attempting to read the dependency
 * toml file and the format is not what was expected.
 */
public class BadDependencyTomlFormatException extends Exception {


    /**
     * Exception constructor.
     *
     * @param message errorMessage.
     */
    public BadDependencyTomlFormatException(String message) {
        super(message);
    }
}
