package ssg.page;

import java.util.Optional;
import ssg.tomlvaluetypewrapper.TomlTableWrapper;



/**
 * A page draft used to build the page around a metadata.
 * table and a buffer containing the onbuild page.
 */
public class PageDraft {

    /**
     * The buffer containing the HTML code.
     */
    private final String buffer;

    /**
     * A Table with the metadata required to build the page but not mandatory.
     */
    private TomlTableWrapper metadata;

    /**
     * Constructor.
     *
     * @param buffer containing the HTML code.
     */
    public PageDraft(String buffer) {
        this.buffer = buffer;
    }

    /**
     * Constructor.
     */
    public PageDraft(TomlTableWrapper metadata, String buffer) {
        this.metadata = metadata;
        this.buffer = buffer;
    }

    /**
     * Return metadata if exists.
     */
    public Optional<TomlTableWrapper> getMetadata() {
        return Optional.ofNullable(metadata);
    }

    /**
     * Return the buffer of a Page.
     */
    public String getBuffer() {
        return buffer;
    }

}
