package ssg.pair;

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
     *
     * @param value1 first value of the pair.
     * @param value2 second value of the pair.
     * @throws NullArgumentException if one of the argument is null.
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
     *
     * @return first value of the pair.
     */
    public T getFirstValue() {
        return value1;
    }

    /**
     * First value setter.
     *
     * @param val value that we want to set.
     */
    public void setFirstValue(T val) {
        this.value1 = val;
    }

    /**
     * Second value getter.
     *
     * @return second value of the pair.
     */
    public V getSecondValue() {
        return value2;
    }

    /**
     * Second value setter.
     *
     * @param val value that we want to set.
     */
    public void setSecondValue(V val) {
        this.value2 = val;
    }

    /**
     * Compare if fields are equal.
     *
     * @param p the other pair that we want to compare to.
     * @return boolean true if both pair are equals, false otherwise.
     */
    public boolean isEqual(Pair<T, V> p) {
        return value1.equals(p.value1) && value2.equals(p.value2);
    }
}
