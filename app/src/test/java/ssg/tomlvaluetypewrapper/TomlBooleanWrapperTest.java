package ssg.tomlvaluetypewrapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Junit test class for TomlBooleanWrapperTest.
 */
class TomlBooleanWrapperTest {

    @Test void textualRepresentation() {
        boolean b = true;
        TomlBooleanWrapper wrapper = new TomlBooleanWrapper(b);
        assertEquals(Boolean.toString(b), wrapper.toString(),
                "boolean wrapper textual representation must match wrappee textual representation");
    }
}
