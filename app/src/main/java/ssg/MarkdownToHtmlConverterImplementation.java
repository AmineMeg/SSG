package ssg;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

/**
 * Convert CommonMark to HTML.
 */
public class MarkdownToHtmlConverterImplementation extends MarkdownToHtmlConverter {

    /**
     * CommonMarkJava parser.
     */
    private static final Parser parser = Parser.builder().build();

    /**
     * CommonMarkJava renderer.
     */
    private static final HtmlRenderer renderer = HtmlRenderer.builder().build();

    /**
     * convert CommonMark to HTML.
     *
     * @param input CommonMark content to convert
     *
     * @return input content translated to HTML
     */
    @Override
    public String convert(String input) {
        Node document = parser.parse(input);
        return renderer.render(document);
    }
}
