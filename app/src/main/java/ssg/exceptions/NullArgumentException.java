package ssg.exceptions;

/**
 * Custom Metadata exception.
 */
public final class NullArgumentException extends Exception {

    /**
     * Constructor.
     */
    public NullArgumentException(String errorMessage) {
        super(errorMessage);
    }
}