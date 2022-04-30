package ssg.httpserver.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import ssg.Generated;

/**
 * Json Utility class.
 */
@SuppressWarnings("PMD.LawOfDemeter")
@Generated
public final class Json {

    /**
     * Private Constructor to make it a utility class.
     */
    private Json() {}

    /**
     * Default object mapper.
     */
    private static final ObjectMapper myObjectMapper = defaultObjectMapper();

    /**
     * Config object mapper.
     *
     * @return an ObjectMapper.
     */
    private static ObjectMapper defaultObjectMapper() {
        ObjectMapper om = new ObjectMapper();
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return om;
    }

    /**
     * Parse Json.
     *
     * @param jsonSrc json file.
     * @return a JsonNode.
     * @throws JsonProcessingException when there is a problem during the process.
     */
    public static JsonNode parse(String jsonSrc) throws JsonProcessingException {
        return myObjectMapper.readTree(jsonSrc);
    }

    /**
     * Convert Json to an instance of the class.
     *
     * @param node a JsonNode.
     * @param clazz generic class.
     * @param <T> generic class.
     * @return an instance of the generic class.
     * @throws JsonProcessingException when there is a problem during the process.
     */
    public static <T> T fromJson(JsonNode node, Class<T> clazz) throws JsonProcessingException {
        return myObjectMapper.treeToValue(node, clazz);
    }

    /**
     * Convert an objet to Json.
     *
     * @param object an object.
     * @return a JsonNode.
     */
    public static JsonNode toJson(Object object) {
        return myObjectMapper.valueToTree(object);
    }

    /**
     * Stringify a JsonNode.
     *
     * @param node a JsonNode.
     * @return a String representing the Json.
     * @throws JsonProcessingException when there is a problem during the process.
     */
    public static String stringify(JsonNode node) throws JsonProcessingException {
        return generateJson(node, false);
    }

    /**
     * Stringify a JsonNode prettier.
     *
     * @param node a JsonNode.
     * @return a String representing the Json.
     * @throws JsonProcessingException when there is a problem during the process.
     */
    public static String stringifyPretty(JsonNode node) throws JsonProcessingException {
        return generateJson(node, true);
    }

    /**
     * Generate string from Json.
     *
     * @param object an Object.
     * @param pretty use pretty style or not.
     * @return a string.
     * @throws JsonProcessingException when there is a problem during the process.
     */
    private static String generateJson(Object object, boolean pretty)
            throws JsonProcessingException {
        ObjectWriter objectWriter = myObjectMapper.writer();
        if (pretty) {
            objectWriter = objectWriter.with(SerializationFeature.INDENT_OUTPUT);
        }
        return objectWriter.writeValueAsString(object);
    }

}
