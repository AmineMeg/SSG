package ssg.tomlvaluetypewrapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

/**
 * Junit test class for TomlLocalDateWrapper.
 */
@SuppressWarnings("PMD.LawOfDemeter")
class TomlLocalDateWrapperTest {

    @Test void textualRepresentation() {
        LocalDate date = LocalDate.now();
        TomlLocalDateWrapper wrapper = new TomlLocalDateWrapper(date);
        assertEquals(date.toString(), wrapper.toString(),
                "LocalDate wrapper textual representation must match wrappee representation");
    }
}
