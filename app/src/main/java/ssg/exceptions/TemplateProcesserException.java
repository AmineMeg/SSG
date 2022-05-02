package ssg.exceptions;

/**
 * Custom TemplateProcesser exception.
 * thrown if an exception happened while processing a template.
 */
public final class TemplateProcesserException extends Exception {

    /**
     * Exception constructor.
     *
     * @param errorMessage error message.
     */
    public TemplateProcesserException(String errorMessage) {
        super(errorMessage);
    }

    /**
     * Exception constructor for stack trace preservation.
     *
     * @param exn Exception.
     */
    public TemplateProcesserException(Exception exn) {
        super(exn);
    }
}
