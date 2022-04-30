package ssg.tomlvaluetypewrapper;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Wrapper for an Array of TOMLValueTypeWrapper.
 */
public class TomlArrayWrapper extends TomlValueTypeWrapper implements Iterable {

    /**
     * Wrapper.
     */
    private final TomlValueTypeWrapper[] value;


    /**
     * Constructor.
     *
     * @param value the value that we wrap.
     */
    public TomlArrayWrapper(TomlValueTypeWrapper... value) {
        this.value = value.clone();
    }

    /**
     * Return an iterator (mandatory to implement Iterable).
     */
    @Override
    public Iterator<TomlValueTypeWrapper> iterator() {

        return new Iterator<TomlValueTypeWrapper>() {
            /**
             * Position of the iterator in the array.
             */
            private int pos;

            @Override
            public boolean hasNext() {
                return value.length > pos;
            }

            @Override
            public TomlValueTypeWrapper next() {
                if (pos >= value.length) {
                    throw new NoSuchElementException(
                            "next() was called but there is no more elements");
                }
                return value[pos++];
            }
        };
    }

    /**
     * Return the textual representation for the wrapped value.
     * This representation is hardcoded for HTML5 tables.
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
