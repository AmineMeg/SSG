package ssg.tomlvaluetypewrapper;

/**
 * Wrapper for an Array of TOMLValueTypeWrapper.
 */
public class TomlArrayWrapper extends TomlValueTypeWrapper {


    /**
     * Wrapper for an Array of TOMLValueTypeWrapper.
     */
    private final TomlValueTypeWrapper[] value;


    /**
     * Constructor.
     */
    public TomlArrayWrapper(TomlValueTypeWrapper... value) {
        this.value = value.clone();
    }

    /**
     * Return the wrapped value.
     */
    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append("<ul>\n");
        for (TomlValueTypeWrapper v : value) {
            String s = "\t<li>" + v.toString() + "</li>\n";
            res.append(s);
        }
        res.append("</ul>\n");
        return res.toString();
    }
}
