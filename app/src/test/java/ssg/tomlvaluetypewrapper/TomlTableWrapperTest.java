package ssg.tomlvaluetypewrapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import org.junit.jupiter.api.Test;

/**
 * Junit test class for TomlTableWrapper.
 */
@SuppressWarnings({"PMD.LawOfDemeter", "PMD.TooManyMethods"})
class TomlTableWrapperTest {

    @Test
    void isEmptyTrue() {
        TomlTableWrapper wrapper = new TomlTableWrapper();
        assertTrue(wrapper.isEmpty(),
                "isEmpty() must be true when the wrapper is empty");
    }

    @Test
    void isEmptyFalse() {
        String key = "foo";
        TomlValueTypeWrapper value = new TomlTableWrapper();
        TomlTableWrapper wrapper = new TomlTableWrapper();
        wrapper.put(key, value);
        assertFalse(wrapper.isEmpty(),
                "isEmpty() must be false when the wrapper is not empty");
    }

    @Test
    void containsValueFalse() {
        TomlValueTypeWrapper value = new TomlTableWrapper();
        TomlTableWrapper wrapper = new TomlTableWrapper();
        assertFalse(wrapper.containsValue(value),
                "containsValue(_) must be false on an empty wrapper");
    }

    @Test
    void getNull() {
        String key = "foo";
        TomlTableWrapper wrapper = new TomlTableWrapper();
        assertEquals(null, wrapper.get(key),
                "get(k) must be null as k has never been inserted");
    }

    @Test
    void getNull2() {
        String key1 = "foo";
        String key2 = "bar";
        TomlValueTypeWrapper value = new TomlTableWrapper();

        TomlTableWrapper wrapper = new TomlTableWrapper();
        wrapper.put(key1, value);

        assertEquals(null, wrapper.get(key2),
                "get(k) must be null as k has never been inserted");
    }

    @Test
    void getEqualsInsertedValue() {
        String key = "foo";
        TomlValueTypeWrapper value = new TomlTableWrapper();

        TomlTableWrapper wrapper = new TomlTableWrapper();
        wrapper.put(key, value);

        assertEquals(value, wrapper.get(key),
                "get(k) must equal the value inserted for key k");
    }

    @Test
    void containsValueTrue() {
        String key = "foo";
        TomlValueTypeWrapper value = new TomlTableWrapper();

        TomlTableWrapper wrapper = new TomlTableWrapper();
        wrapper.put(key, value);

        assertTrue(wrapper.containsValue(value),
                "containsValue(v) must be true as v has been inserted");
    }

    @Test
    void containsValueFalse2() {
        String key = "foo";
        TomlValueTypeWrapper value1 = new TomlTableWrapper();
        TomlValueTypeWrapper value2 = new TomlTableWrapper();

        TomlTableWrapper wrapper = new TomlTableWrapper();
        wrapper.put(key, value1);

        assertFalse(wrapper.containsValue(value2),
                "containsValue(v) must be false as v has never been inserted");
    }

    @Test
    void containsKeyFalse() {
        String key = "foo";
        TomlTableWrapper wrapper = new TomlTableWrapper();
        assertFalse(wrapper.containsKey(key),
                "containsKey(_) must be false on an empty wrapper");
    }

    @Test
    void containsKeyTrue() {
        String key = "foo";
        TomlValueTypeWrapper value = new TomlTableWrapper();

        TomlTableWrapper wrapper = new TomlTableWrapper();
        wrapper.put(key, value);

        assertTrue(wrapper.containsKey(key),
                "containsKey(k) must be true as k was inserted");
    }

    @Test
    void containsKeyFalse2() {
        String key1 = "foo";
        String key2 = "bar";
        TomlValueTypeWrapper value = new TomlTableWrapper();

        TomlTableWrapper wrapper = new TomlTableWrapper();
        wrapper.put(key2, value);

        assertFalse(wrapper.containsKey(key1),
                "containsKey(k) must be false as k was not inserted");
    }

    @Test
    void addRemoveIsEmpty() {
        String key = "foo";
        TomlValueTypeWrapper value = new TomlTableWrapper();
        TomlTableWrapper wrapper = new TomlTableWrapper();
        wrapper.put(key, value);
        wrapper.remove(key);
        assertTrue(wrapper.isEmpty(),
                 "isEmpty() must be true when the only item has been removed");
    }

    @Test
    void emptyWrappeeValuesContainsAllWrapperValues() {
        HashMap<String, TomlValueTypeWrapper> map = new HashMap<>();
        TomlTableWrapper wrapper = new TomlTableWrapper(map);
        assertTrue(map.values().containsAll(wrapper.values()),
                "wrappee values must contain all wrapper values");
    }

    @Test
    void wrappeeValuesContainsAllWrapperValues() {
        String key = "foo";
        TomlValueTypeWrapper value = new TomlTableWrapper();

        HashMap<String, TomlValueTypeWrapper> map = new HashMap<>();
        map.put(key, value);

        TomlTableWrapper wrapper = new TomlTableWrapper(map);
        assertTrue(map.values().containsAll(wrapper.values()),
                "wrappee values must contain all wrapper values");
    }

    @Test
    void emptyWrappeeKeySetContainsAllWrapperKeySet() {
        HashMap<String, TomlValueTypeWrapper> map = new HashMap<>();
        TomlTableWrapper wrapper = new TomlTableWrapper(map);
        assertTrue(map.keySet().containsAll(wrapper.keySet()),
                "wrappee key set must contain all wrapper key set");
    }

    @Test
    void wrappeeKeySetContainsAllWrapperKeySet() {
        String key = "foo";
        TomlValueTypeWrapper value = new TomlTableWrapper();

        HashMap<String, TomlValueTypeWrapper> map = new HashMap<>();
        map.put(key, value);

        TomlTableWrapper wrapper = new TomlTableWrapper(map);
        assertTrue(map.keySet().containsAll(wrapper.keySet()),
                "wrappee key set must contain all wrapper key set");
    }

    @Test
    void emptyWrapperValuesContainsAllWrappeeValues() {
        HashMap<String, TomlValueTypeWrapper> map = new HashMap<>();
        TomlTableWrapper wrapper = new TomlTableWrapper(map);
        assertTrue(wrapper.values().containsAll(map.values()),
                "wrapper value must contain all wrappee key set");
    }

    @Test
    void wrapperValuesContainsAllWrapeeValues() {
        String key = "foo";
        TomlValueTypeWrapper value = new TomlTableWrapper();

        HashMap<String, TomlValueTypeWrapper> map = new HashMap<>();
        map.put(key, value);

        TomlTableWrapper wrapper = new TomlTableWrapper(map);
        assertTrue(wrapper.values().containsAll(map.values()),
                "wrapper values must contain all wrappee values");
    }

    @Test
    void emptyWrapperKeySetContainsAllWrappeeKeySet() {
        HashMap<String, TomlValueTypeWrapper> map = new HashMap<>();
        TomlTableWrapper wrapper = new TomlTableWrapper(map);
        assertTrue(wrapper.keySet().containsAll(map.keySet()),
                "wrapper key set must contain all wrappee key set");
    }

    @Test
    void wrapperKeySetContainsAllWrappeeKeySet() {
        String key = "foo";
        TomlValueTypeWrapper value = new TomlTableWrapper();

        HashMap<String, TomlValueTypeWrapper> map = new HashMap<>();
        map.put(key, value);

        TomlTableWrapper wrapper = new TomlTableWrapper(map);
        assertTrue(wrapper.keySet().containsAll(map.keySet()),
                "wrapper key set must contain all wrappee key set");
    }

    @Test
    void addClearIsEmpty() {
        String key1 = "foo";
        String key2 = "bar";
        TomlValueTypeWrapper value = new TomlTableWrapper();
        TomlTableWrapper wrapper = new TomlTableWrapper();
        wrapper.put(key1, value);
        wrapper.put(key2, value);
        wrapper.clear();
        assertTrue(wrapper.isEmpty(),
                "isEmpty() must return true when the wrapper has just been cleared");
    }

    @Test
    void clearedEmptyWrapperIsEmpty() {
        TomlTableWrapper wrapper = new TomlTableWrapper();
        wrapper.clear();
        assertTrue(wrapper.isEmpty(),
                "isEmpty() must be true when the wrapper has just been cleared");
    }

    @Test
    void addRemoveEquals() {
        String key = "foo";
        TomlValueTypeWrapper value = new TomlTableWrapper();
        TomlTableWrapper wrapper = new TomlTableWrapper();
        wrapper.put(key, value);
        assertEquals(value, wrapper.remove(key),
                "removed value must match the inserted value");
    }

    @Test
    void textualRepresentation() {
        String key1 = "un";
        int value1 = 1;
        String key2 = "deux";
        int value2 = 2;

        HashMap<String, TomlValueTypeWrapper> map = new HashMap<>();
        map.put(key1, new TomlIntegerWrapper(value1));
        map.put(key2, new TomlIntegerWrapper(value2));

        TomlTableWrapper wrapper = new TomlTableWrapper(map);
        String expectedOutput = """
            <table>
                <tr>
                  <td>%s</td>
                  <td>%s</td>
                </tr>
                <tr>
                  <td>%s</td>
                  <td>%s</td>
                </tr>
            </table>
            """.formatted(key1, value1, key2, value2);
        assertEquals(expectedOutput, wrapper.toString(),
                "toString() must return an HTML5 table including wrapped datas");
    }

    @Test
    void emptyTextualRepresentation() {
        TomlTableWrapper wrapper = new TomlTableWrapper();
        String expectedOutput = """
            <table>
            </table>
            """;
        assertEquals(expectedOutput, wrapper.toString(),
                "toString() should be and empty HTML5 table when the table is empty");
    }
}
