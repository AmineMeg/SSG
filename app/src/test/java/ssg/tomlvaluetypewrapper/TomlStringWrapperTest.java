package ssg.tomlvaluetypewrapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Junit test class for TomlStringWrapper.
 */
class TomlStringWrapperTest {

    @Test void textualRepresentation() {
        String s = "foo";
        TomlStringWrapper wrapper = new TomlStringWrapper(s);
        assertEquals(s, wrapper.toString(),
                "string wrapper textual representation must match string wrappee");
    }
}
