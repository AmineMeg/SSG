package ssg.tomlvaluetypewrapper;

import java.util.HashMap;
import java.util.Map;


/**
 * Wrapper for a Table of TOMLValueTypeWrapper.
 */
public class TomlTableWrapper extends TomlValueTypeWrapper {
    /**
     * A wrapped Hashmap of String, TOMLValueTypeWrapper.
     */
    private final Map<String, TomlValueTypeWrapper> value;

    /**
     * Constructor.
     */
    public TomlTableWrapper(Map<String, TomlValueTypeWrapper> value) {
        this.value = new HashMap<>(value);
    }

    /**
     * Return the wrapped value.
     */
    @SuppressWarnings("PMD.LawOfDemeter")
    @Override
    public String toString() {
        StringBuilder res = new StringBuilder(19);
        res.append("<table>\n");
        for (Map.Entry<String, TomlValueTypeWrapper> entry : value.entrySet()) {
            String k = entry.getKey();
            TomlValueTypeWrapper v = entry.getValue();
            String s = "\t<tr>\n\t\t<td>"
                    + k
                    + "</td>\n\t\t<td>"
                    + v.toString()
                    + "</td>\n\t</tr>\n";
            res.append(s);
        }
        res.append("</table>\n");
        return res.toString();
    }
}
