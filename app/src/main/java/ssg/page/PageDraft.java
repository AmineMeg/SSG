package ssg.page;

import java.util.Map;
import java.util.Optional;
import ssg.tomlvaluetypewrapper.TomlValueTypeWrapper;


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
    private final Optional<Map<String, TomlValueTypeWrapper>> metadata;

    /**
     * Constructor.
     *
     * @param metadata will contain the metadata.
     * @param buffer containing the string content of .md to parse.
     */
    public PageDraft(Map<String, TomlValueTypeWrapper> metadata, String buffer) {
        this.metadata = Optional.ofNullable(metadata);
        this.buffer = buffer;
    }

    /**
     * Return metadata if exists.
     */
    public Optional<Map<String, TomlValueTypeWrapper>> getMetadata() {
        return metadata;
    }

    /**
     * Return the buffer of a Page.
     */
    public String getBuffer() {
        return buffer;
    }

}
