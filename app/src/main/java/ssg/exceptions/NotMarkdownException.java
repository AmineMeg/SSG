package ssg.exceptions;

/**
 * Customized exception when the file that we receive isn't a markdown.
 */
public class NotMarkdownException extends Exception {

    /**
     * Constructor.
     */
    public NotMarkdownException(String errorMessage) {
        super(errorMessage);
    }
}
