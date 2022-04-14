package ssg.tomlvaluetypewrapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

/**
 * Junit test class for TomlLocalDateTimeWrapper.
 */
@SuppressWarnings("PMD.LawOfDemeter")
class TomlLocalDateTimeWrapperTest {

    @Test void textualReprentation() {
        LocalDateTime dt = LocalDateTime.now();
        TomlLocalDateTimeWrapper wrapper = new TomlLocalDateTimeWrapper(dt);
        assertEquals(dt.toString(), wrapper.toString(),
                "wrapper textual representation must match wrappee representation");
    }
}
