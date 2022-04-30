package ssg.page;

import java.util.Map;
import java.util.Optional;
import ssg.tomlvaluetypewrapper.TomlValueTypeWrapper;


/**
 * A page draft used to build the page around a metadata.
 * table and a buffer containing the on build page.
 */
public class PageDraft {

    /**
     * Page title.
     */
    private final String title;

    /**
     * Page content.
     */
    private final String content;

    /**
     * Optional page data.
     */
    private final Optional<Map<String, TomlValueTypeWrapper>> data;

    /**
     * Constructor.
     *
     * @param data page data.
     * @param content page content.
     * @param title page title.
     */
    public PageDraft(Map<String, TomlValueTypeWrapper> data, String content, String title) {
        this.data = Optional.ofNullable(data);
        this.content = content;
        this.title = title;
    }

    /**
     * Return page data.
     *
     * @return page data.
     */
    public Optional<Map<String, TomlValueTypeWrapper>> getData() {
        return data;
    }

    /**
     * Return page title.
     *
     * @return page title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Return page content.
     *
     * @return page content.
     */
    public String getContent() {
        return content;
    }
}
