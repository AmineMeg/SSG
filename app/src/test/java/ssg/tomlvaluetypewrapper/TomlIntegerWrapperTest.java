package ssg.tomlvaluetypewrapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Junit test class for TomlIntegerWrapper.
 */
class TomlIntegerWrapperTest {

    @Test void textualRepresentation() {
        int i = 42;
        TomlIntegerWrapper wrapper = new TomlIntegerWrapper(i);
        assertEquals(Integer.toString(i), wrapper.toString(),
                "int wrapper textual representation must match wrappee textual representation");
    }
}
