package ssg.exceptions;

/**
 * Customized exception when the file that we receive isn't a markdown.
 */
public final class NotMarkdownException extends Exception {

    /**
     * Exception constructor.
     *
     * @param errorMessage error message.
     */
    public NotMarkdownException(String errorMessage) {
        super(errorMessage);
    }
}
