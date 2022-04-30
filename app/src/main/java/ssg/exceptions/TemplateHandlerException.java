package ssg.exceptions;

/**
 * Custom TemplateHandler exception.
 * thrown if an exception happened while handling a template.
 */
public final class TemplateHandlerException extends Exception {

    /**
     * Exception constructor.
     *
     * @param errorMessage error message.
     */
    public TemplateHandlerException(String errorMessage) {
        super(errorMessage);
    }

    /**
     * Exception constructor for stack trace preservation.
     *
     * @param exn exception.
     */
    public TemplateHandlerException(Exception exn) {
        super(exn);
    }
}
