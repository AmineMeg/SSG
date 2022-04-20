package ssg.httpserver.httpserverconfig;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Configuration test class.
 */
class ConfigurationTest {

    /**
     * class to be tested.
     */
    private Configuration configuration;

    /**
     * init class in each test.
     */
    @BeforeEach
    void init() {
        configuration = new Configuration();
        configuration.setPort(8080);
        configuration.setWebroot("/dudu");
    }

    @Test
    void getterPortTest() {
        assertEquals(8080, configuration.getPort(), "Test port getter");
    }

    @Test
    void getterWebrootTest() {
        assertEquals("/dudu", configuration.getWebroot(), "Test webroot getter");
    }

    @Test
    void setterPortTest() {
        configuration.setPort(8081);
        assertEquals(8081, configuration.getPort(), "Test port setter");
    }

    @Test
    void setterWebrootTest() {
        configuration.setWebroot("/toto");
        assertEquals("/toto", configuration.getWebroot(), "Test webroot setter");
    }
}
