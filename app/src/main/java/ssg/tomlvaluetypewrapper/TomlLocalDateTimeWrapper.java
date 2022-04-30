package ssg.tomlvaluetypewrapper;

import java.time.LocalDateTime;

/**
 * Wrapper LocalDateTime class for HashMap in PageDraft.
 */
public class TomlLocalDateTimeWrapper extends TomlValueTypeWrapper {
    /**
     * A wrapped LocalDateTime.
     */
    private final LocalDateTime value;

    /**
     * Constructor.
     *
     * @param value the value that we wrap.
     */
    public TomlLocalDateTimeWrapper(LocalDateTime value) {
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
