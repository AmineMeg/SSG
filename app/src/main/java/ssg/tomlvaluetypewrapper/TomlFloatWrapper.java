package ssg.tomlvaluetypewrapper;

/**
 * Wrapper float class for HashMap in PageDraft.
 */
public class TomlFloatWrapper extends TomlValueTypeWrapper {
    /**
     * A wrapped float.
     */
    private final float value;

    /**
     * Constructor.
     */
    public TomlFloatWrapper(float value) {
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
