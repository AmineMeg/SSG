package ssg;

import ssg.exceptions.NullArgumentException;

/**
 * Class Pair.
 */
public final class Pair<T, V> {

    /**
     * First value.
     */
    private T value1;

    /**
     * Second value.
     */
    private V value2;

    /**
     * Constructor.
     */
    public Pair(T value1, V value2) throws NullArgumentException {

        if (value1 == null || value2 == null) {
            throw new NullArgumentException("cannot put a null value in a pair");
        }
        this.value1 = value1;
        this.value2 = value2;
    }

    /**
     * First value getter.
     */
    public T getFirstValue() {
        return value1;
    }

    /**
     * First value setter.
     */
    public void setFirstValue(T val) {
        this.value1 = val;
    }

    /**
     * Second value getter.
     */
    public V getSecondValue() {
        return value2;
    }

    /**
     * Second value setter.
     */
    public void setSecondValue(V val) {
        this.value2 = val;
    }

    /**
     * Compare if fields are equal.
     */
    public boolean isEqual(Pair<T, V> p) {
        return value1.equals(p.value1) && value2.equals(p.value2);
    }
}
