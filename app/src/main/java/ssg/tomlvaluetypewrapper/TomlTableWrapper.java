package ssg.tomlvaluetypewrapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * Wrapper for a Table of TOMLValueTypeWrapper.
 */
@SuppressWarnings("PMD.TooManyMethods")
public class TomlTableWrapper extends TomlValueTypeWrapper
    implements Map<String, TomlValueTypeWrapper> {

    /**
     * A wrapped Hashmap of String, TOMLValueTypeWrapper.
     */
    private final Map<String, TomlValueTypeWrapper> value;

    /**
     * Constructor.
     *
     * @param value the value that we wrap.
     */
    public TomlTableWrapper(Map<String, TomlValueTypeWrapper> value) {
        this.value = new HashMap<>(value);
    }

    /**
     * Default constructor.
     */
    public TomlTableWrapper() {
        this.value = new HashMap<>();
    }

    @Override
    public Set<Map.Entry<String,TomlValueTypeWrapper>> entrySet() {
        return value.entrySet();
    }

    @Override
    public Collection<TomlValueTypeWrapper> values() {
        return value.values();
    }

    @Override
    public Set<String> keySet() {
        return value.keySet();
    }

    @Override
    public void clear() {
        value.clear();
    }

    @Override
    public void putAll(Map<? extends String,? extends TomlValueTypeWrapper> m) {
        value.putAll(m);
    }

    @Override
    public TomlValueTypeWrapper remove(Object k) {
        return value.remove(k);
    }

    @Override
    public TomlValueTypeWrapper put(String k, TomlValueTypeWrapper v) {
        return value.put(k, v);
    }

    @Override
    public TomlValueTypeWrapper get(Object k) {
        return value.get(k);
    }

    @Override
    public boolean containsValue(Object v) {
        return value.containsValue(v);
    }

    @Override
    public boolean containsKey(Object v) {
        return value.containsKey(v);
    }

    @Override
    public boolean isEmpty() {
        return value.isEmpty();
    }

    @Override
    public int size() {
        return value.size();
    }

    /**
     * Return the wrapped value.
     * the returned format is hardcoded to HTML5 tables. 
     */
    @SuppressWarnings("PMD.LawOfDemeter")
    @Override
    public String toString() {
        StringBuilder table = new StringBuilder(19);

        table.append("<table>\n");
        for (Map.Entry<String, TomlValueTypeWrapper> entry : value.entrySet()) {
            String tr = """
                    <tr>
                      <td>%s</td>
                      <td>%s</td>
                    </tr>
                """.formatted(entry.getKey(), entry.getValue());
            table.append(tr);
        }
        table.append("</table>\n");

        return table.toString();
    }
}
