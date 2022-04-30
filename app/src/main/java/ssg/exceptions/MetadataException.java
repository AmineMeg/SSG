package ssg.exceptions;

/**
 * Custom Metadata exception.
 */
public final class MetadataException extends Exception {

    /**
     * Exception constructor.
     *
     * @param errorMessage error message.
     */
    public MetadataException(String errorMessage) {
        super(errorMessage);
    }
}
