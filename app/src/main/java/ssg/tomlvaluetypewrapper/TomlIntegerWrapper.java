package ssg.tomlvaluetypewrapper;


/**
 * Wrapper Integer class for HashMap in PageDraft.
 */
public class TomlIntegerWrapper extends TomlValueTypeWrapper {
    /**
     * A wrapped int.
     */
    private final int value;

    /**
     * Constructor.
     *
     * @param value the value that we wrap.
     */
    public TomlIntegerWrapper(int value) {
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
