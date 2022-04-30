package ssg.tomlvaluetypewrapper;

import java.time.OffsetDateTime;

/**
 * Wrapper String class for HashMap in PageDraft.
 */
public class TomlOffsetDateTimeWrapper extends TomlValueTypeWrapper {
    /**
     * A wrapped OffsetDateTime.
     */
    private final OffsetDateTime value;

    /**
     * Constructor.
     *
     * @param value the value that we wrap.
     */
    public TomlOffsetDateTimeWrapper(OffsetDateTime value) {
        this.value = value;
    }

    /**
     * Return the wrapped value.
     */
    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
