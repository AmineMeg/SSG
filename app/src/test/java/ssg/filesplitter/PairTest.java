package ssg.filesplitter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ssg.exceptions.NullArgumentException;
import ssg.pair.Pair;

/**
 * Test Class for Pair.
 */
@SuppressWarnings("PMD.TooManyMethods")
class PairTest {

    /**
     * Class to be tested.
     */
    private Pair<String, Integer> pair;

    @BeforeEach
    void init() throws NullArgumentException {
        pair = new Pair<String, Integer>("ok", 1);
    }

    @Test
    void testIfConstructorThrowFirst() {
        assertThrows(
                NullArgumentException.class, () -> new Pair<>(null, 1),
                "null args so constructor did raise NullArgumentException");
    }

    @Test
    void testIfConstructorThrowSecond() {
        assertThrows(
                NullArgumentException.class, () -> new Pair<>("ok", null),
                "null args so constructor did raise NullArgumentException");
    }

    @Test
    void testIfConstructorThrowBoth() {
        assertThrows(
                NullArgumentException.class, () -> new Pair<>(null, null),
                "null args so constructor did raise NullArgumentException");
    }

    @Test
    void testGetFirstValue() {
        assertEquals("ok", pair.getFirstValue(), "Test getter first value");
    }

    @Test
    void testSetFirstValue() {
        pair.setFirstValue("dodo");
        assertEquals("dodo", pair.getFirstValue(), "Test setter first value");
    }

    @Test
    void testGetSecondValue() {
        assertEquals(1, pair.getSecondValue(), "Test getter second value");
    }

    @Test
    void testSetSecondValue() {
        pair.setSecondValue(666);
        assertEquals(666, pair.getSecondValue(), "Test setter second value");
    }

    @Test
    void testIsEqualTrue() throws NullArgumentException {
        Pair<String, Integer> comparePair = new Pair<String, Integer>("ok", 1);
        assertTrue(pair.isEqual(comparePair), "Test equal true");
    }

    @Test
    void testIsEqualFalseFirst() throws NullArgumentException {
        Pair<String, Integer> comparePair = new Pair<String, Integer>("lol", 1);
        assertFalse(pair.isEqual(comparePair), "Test equal false when first value is wrong");
    }

    @Test
    void testIsEqualFalseSecond() throws NullArgumentException {
        Pair<String, Integer> comparePair = new Pair<String, Integer>("ok", 9);
        assertFalse(pair.isEqual(comparePair), "Test equal false when second value is wrong");
    }

    @Test
    void testIsEqualFalseBoth() throws NullArgumentException {
        Pair<String, Integer> comparePair = new Pair<String, Integer>("lol", 9);
        assertFalse(pair.isEqual(comparePair), "Test equal false when both values are wrong");
    }

}
