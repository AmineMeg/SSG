package ssg.tomlvaluetypewrapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalTime;
import org.junit.jupiter.api.Test;

/**
 * Junit test class for TomlLocalTimeWrapper.
 */
@SuppressWarnings("PMD.LawOfDemeter")
class TomlLocalTimeWrapperTest {

    @Test void textualReprentation() {
        LocalTime t = LocalTime.now();
        TomlLocalTimeWrapper wrapper = new TomlLocalTimeWrapper(t);
        assertEquals(t.toString(), wrapper.toString(),
                "wrapper textual representation must match wrappee representation");
    }
}
