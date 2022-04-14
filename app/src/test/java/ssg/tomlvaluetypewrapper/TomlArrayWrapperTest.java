package ssg.tomlvaluetypewrapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.StringBuilder;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

/**
 * Junit test class for TomlArrayWrapper.
 */
class TomlArrayWrapperTest {

    @Test void emptyArrayTextualRepresentation() {
        TomlValueTypeWrapper array = new TomlArrayWrapper(new TomlValueTypeWrapper[0]);
        String expectedOutput = """
            <ul>
            </ul>
            """;
        assertEquals(expectedOutput, array.toString(),
                "empty array representation must be an empty HTML5 table");
    }

    @Test void arraysAreIterable() {
        TomlArrayWrapper item = new TomlArrayWrapper(new TomlValueTypeWrapper[0]);
        TomlArrayWrapper array = new TomlArrayWrapper(item, item);

        StringBuilder result = new StringBuilder();
        for (Object v : array) {
            result.append(v.toString());
        }

        String expectedOutput = """
            <ul>
            </ul>
            <ul>
            </ul>
            """;
        assertEquals(expectedOutput, result.toString(),
                "this must match the concatenation of textual elements from the array");
    }

    @Test void incorrectIterationThrowsException() {
        TomlArrayWrapper array = new TomlArrayWrapper();
        assertThrows(NoSuchElementException.class,
            () -> array.iterator().next(),
            "attempt to call next() on iterator at final position did not raise Exception");
    }

    @SuppressWarnings("PMD.LawOfDemeter")
    @Test void hasNextTrue() {
        TomlArrayWrapper array = new TomlArrayWrapper(new TomlStringWrapper("foo"));
        assertTrue(array.iterator().hasNext(),
                "hasNext() must be true if the iterator is not the last element");
    }

    @SuppressWarnings("PMD.LawOfDemeter")
    @Test void hasNextFalse() {
        TomlArrayWrapper array = new TomlArrayWrapper();
        assertFalse(array.iterator().hasNext(),
                "hasNext() must be false if the iterator is the last element");
    }
}
