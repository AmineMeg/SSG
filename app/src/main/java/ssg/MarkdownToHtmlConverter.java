package ssg;

/**
 * Convert CommonMark to HTML.
 */
public abstract class MarkdownToHtmlConverter {
    /**
     * convert CommonMark to HTML.
     *
     * @param input CommonMark content to convert
     *
     * @return input content translated to HTML
     */
    public abstract String convert(String input);
}
