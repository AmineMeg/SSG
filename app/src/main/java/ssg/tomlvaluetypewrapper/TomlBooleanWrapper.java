package ssg.tomlvaluetypewrapper;

/**
 * Wrapped Boolean class for HashMap in PageDraft.
 */
public class TomlBooleanWrapper extends TomlValueTypeWrapper {

    /**
     * A wrapped boolean.
     */
    private final Boolean value;

    /**
     * Constructor.
     *
     * @param value the value that we wrap.
     */
    public TomlBooleanWrapper(Boolean value) {
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
