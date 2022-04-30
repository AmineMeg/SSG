package ssg.parsertoml;

import java.io.IOException;
import java.util.Map;
import ssg.tomlvaluetypewrapper.TomlValueTypeWrapper;

/**
 * ParserToml interface.
 */
public interface ParserToml {
    
    /**
     * Main parsing function.

     * @param buffer file to parse.
     * @return result of parsing.
     * @throws IOException case file does not exist.
     */
    Map<String, TomlValueTypeWrapper> parse(String buffer) throws IOException;

}
