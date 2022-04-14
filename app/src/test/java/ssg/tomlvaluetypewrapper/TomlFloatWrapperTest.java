package ssg.tomlvaluetypewrapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Junit test class for TomlFloatWrapper.
 */
class TomlFloatWrapperTest {

    @Test void textualRepresentation() {
        float f = 2.2f;
        TomlFloatWrapper wrapper = new TomlFloatWrapper(f);
        assertEquals(Float.toString(f), wrapper.toString(),
                "float wrapper textual representation must match float textual representation");
    }
}
