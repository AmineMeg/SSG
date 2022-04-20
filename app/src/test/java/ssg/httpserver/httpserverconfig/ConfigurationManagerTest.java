package ssg.httpserver.httpserverconfig;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * Test class for Configuration Manager.
 */
class ConfigurationManagerTest {

    /**
     * Class to be tested.
     */
    private ConfigurationManager configurationManager;

    @Test
    void instanceIsNull() {
        assertNull(configurationManager, "test if configurationManager is null");
    }

    @Test
    void getInstance() {
        configurationManager = ConfigurationManager.getInstance();
        assertNotNull(configurationManager,
                "test that we do get an instance of ConfigurationManager");
    }

}
