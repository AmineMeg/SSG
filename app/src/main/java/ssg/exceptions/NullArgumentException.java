package ssg.exceptions;

/**
 * Custom Metadata exception.
 */
public final class NullArgumentException extends Exception {

    /**
     * Exception constructor.
     *
     * @param errorMessage error message.
     */
    public NullArgumentException(String errorMessage) {
        super(errorMessage);
    }
}