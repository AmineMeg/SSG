package ssg.tomlvaluetypewrapper;

/**
 * Wrapper String class for HashMap in PageDraft.
 */
public class TomlStringWrapper extends TomlValueTypeWrapper {
    /**
     * A wrapped String.
     */
    private final String value;

    /**
     * Constructor.
     *
     * @param value the value that we wrap.
     */
    public TomlStringWrapper(String value) {
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
