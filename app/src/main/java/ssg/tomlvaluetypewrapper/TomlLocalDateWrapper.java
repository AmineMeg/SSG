package ssg.tomlvaluetypewrapper;

import java.time.LocalDate;

/**
 * Wrapper Integer class for HashMap in PageDraft.
 */
public class TomlLocalDateWrapper extends TomlValueTypeWrapper {
    /**
     * A wrapped LocalDate.
     */
    private final LocalDate value;

    /**
     * Constructor.
     *
     * @param value the value that we wrap.
     */
    public TomlLocalDateWrapper(LocalDate value) {
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
