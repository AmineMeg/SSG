package ssg.markdowntohtmlconverter;

/**
 * Convert CommonMark to HTML.
 */
public interface MarkdownToHtmlConverter {
    /**
     * convert CommonMark to HTML.
     *
     * @param input CommonMark content to convert.
     * @return input content translated to HTML.
     */
    String convert(String input);
}
