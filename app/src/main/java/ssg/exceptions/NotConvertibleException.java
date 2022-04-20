package ssg.exceptions;

/**
 * Exception raised when input is not correctly formatted in order to convert a website.
 */
public final class NotConvertibleException extends Exception {

    /**
     * Exception constructor.
     *
     * @param message errorMessage.
     */
    public NotConvertibleException(String message) {
        super(message);
    }
}
