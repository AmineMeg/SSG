package ssg.tomlvaluetypewrapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.OffsetDateTime;
import org.junit.jupiter.api.Test;

/**
 * Junit test class for TomlOffsetDateTimeWrapper.
 */
@SuppressWarnings("PMD.LawOfDemeter")
class TomlOffsetDateTimeWrapperTest {

    @Test void textualReprentation() {
        OffsetDateTime odt = OffsetDateTime.now();
        TomlOffsetDateTimeWrapper wrapper = new TomlOffsetDateTimeWrapper(odt);
        assertEquals(odt.toString(), wrapper.toString(),
                "wrapper textual representation must match wrappee representation");
    }
}
