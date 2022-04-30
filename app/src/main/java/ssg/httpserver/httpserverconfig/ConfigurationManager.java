package ssg.httpserver.httpserverconfig;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import ssg.Generated;
import ssg.exceptions.HttpConfigurationException;
import ssg.httpserver.util.Json;

/**
 * Configuration Manager class.
 */
@SuppressFBWarnings
@Generated
@SuppressWarnings("PMD.AvoidFileStream")
public final class ConfigurationManager {

    /**
     * Configuration manager singleton.
     */
    private static ConfigurationManager myConfigurationManager;

    /**
     * Current Configuration.
     */
    private static Configuration myCurrentConfiguration;

    /**
     * private constructor to avoid instantiation.
     */
    private ConfigurationManager() {}

    /**
     * Singleton pattern to get instance.
     *
     * @return a configuration manager.
     */
    public static ConfigurationManager getInstance() {
        if (myConfigurationManager != null) {
            return  myConfigurationManager;
        }

        synchronized (ConfigurationManager.class) {
            if (myConfigurationManager == null) {
                myConfigurationManager = new ConfigurationManager();
            }

            return myConfigurationManager;
        }
    }

    /**
     * Used to load a configuration file by the path provided.
     *
     * @param filePath path to config file.
     * @throws HttpConfigurationException when there is a parsing error in the configuration file.
     * @throws IOException when there is a closing file error 
     */
    public void loadConfigurationFile(String filePath) 
            throws HttpConfigurationException, IOException {
        StringBuilder sb = readConfigFile(filePath);
        JsonNode configuration = getJsonNode(sb);
        setCurrentConfiguration(configuration);
    }

    private void setCurrentConfiguration(JsonNode configuration) throws HttpConfigurationException {
        try {
            myCurrentConfiguration = Json.fromJson(configuration, Configuration.class);
        } catch (JsonProcessingException e) {
            throw new HttpConfigurationException(
                    "Error parsing the configuration file, internal", e
            );
        }
    }

    private JsonNode getJsonNode(StringBuilder sb) throws HttpConfigurationException {
        JsonNode configuration;
        try {
            configuration = Json.parse(sb.toString());
        } catch (JsonProcessingException e) {
            throw new HttpConfigurationException("Error parsing the configuration file", e);
        }
        return configuration;
    }

    private StringBuilder readConfigFile(String filePath) 
            throws HttpConfigurationException, IOException {
        FileReader fileReader;
        try {
            fileReader = new FileReader(filePath);
        } catch (FileNotFoundException e) {
            throw new HttpConfigurationException(e);
        }
        StringBuilder sb = new StringBuilder();
        int i;
        try {
            while ((i = fileReader.read()) != -1) {
                sb.append((char) i);
            }
        } catch (IOException e) {
            fileReader.close();
            throw new HttpConfigurationException(e);
        }

        fileReader.close();
        return sb;
    }

    /**
     * Returns the current loaded Configuration.
     *
     * @return the current configuration.
     * @throws HttpConfigurationException if there is no configuration set.
     */
    public Configuration getCurrentConfiguration() throws HttpConfigurationException {
        if (myCurrentConfiguration == null) {
            throw new HttpConfigurationException("No Current Configuration Set.");
        }
        return myCurrentConfiguration;
    }

}
