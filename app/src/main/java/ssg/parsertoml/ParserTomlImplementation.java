package ssg.parsertoml;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.tomlj.Toml;
import org.tomlj.TomlArray;
import org.tomlj.TomlParseResult;
import org.tomlj.TomlTable;
import ssg.tomlvaluetypewrapper.TomlArrayWrapper;
import ssg.tomlvaluetypewrapper.TomlBooleanWrapper;
import ssg.tomlvaluetypewrapper.TomlFloatWrapper;
import ssg.tomlvaluetypewrapper.TomlIntegerWrapper;
import ssg.tomlvaluetypewrapper.TomlLocalDateTimeWrapper;
import ssg.tomlvaluetypewrapper.TomlLocalDateWrapper;
import ssg.tomlvaluetypewrapper.TomlLocalTimeWrapper;
import ssg.tomlvaluetypewrapper.TomlOffsetDateTimeWrapper;
import ssg.tomlvaluetypewrapper.TomlStringWrapper;
import ssg.tomlvaluetypewrapper.TomlTableWrapper;
import ssg.tomlvaluetypewrapper.TomlValueTypeWrapper;


/**
*   ParserTOML class.
*
*/
@SuppressWarnings({"PMD.ExcessiveImports", "PMD.GodClass", 
    "PMD.ExcessiveImports", "PMD.TooManyMethods", "PMD.AvoidReassigningLoopVariables"})
public final class ParserTomlImplementation implements ParserToml {
    /**
     * Logger for messages.
     */
    private static final Logger logger = LogManager.getLogger();

    /**
     * Parse TOML into HashMap.

     * @param buffer toml content to parse.
     * @return toml parse into Map of String TomlValueTypeWrapper.
     * @throws IOException if the path is wrong.
     */
    @SuppressFBWarnings
    @Override
    public Map<String, TomlValueTypeWrapper> parse(String buffer) throws IOException {
        String toWrite = pretreatmentToml(buffer);
        return parseArray(parseToMap(parseToToml(toWrite)), toWrite);
    }

    /**
     * Pretreatment for some String cases not considered by tomlj.

     * @param buffer content to parse.
     * @return the String parsed of path.
     * @throws IOException case file does not exist.
     */
    public String pretreatmentToml(String buffer) throws IOException {
        String toWrite = stringCases(buffer);
        return toWrite;
    }

    /**
     * Avoid new line when the string is a triple quote string 
     * and the line ended with '\'.

     * @param readedString from the file to treat.
     * @return the string parsed.
     */
    @SuppressWarnings({"PMD.LawOfDemeter", "PMD.CyclomaticComplexity", "PMD.CognitiveComplexity"})
    static String stringCases(String readedString) {
        boolean inString = false;
        boolean toDel = false;
        StringBuilder toWrite = new StringBuilder();
        for (int i = 0; i < readedString.length(); i++) {
            if (i < readedString.length() - 3 
                && "\"\"\"".equals(readedString.substring(i, i + 3))) {
                inString = !inString;
                if (inString && i < readedString.length() - 1 
                    && readedString.charAt(i + 1) == '\n') {
                    i++;
                }
            }
            char c = readedString.charAt(i);
            if (inString) {
                if (c == '\\' && i < readedString.length() - 1 
                    && readedString.charAt(i + 1) == '\n') {
                    toDel = !toDel;
                } else if (!(c == ' ' || c == '\t' || c == '\n' || c == '\r') && toDel) {
                    toDel = false;
                } 
                if (!toDel) {
                    toWrite.append(c);
                }
                
            } else {
                toWrite.append(c);
            }
        }
        return toWrite.toString();
    }


    /**
     * Parse TOML in TomlParseResult.

     * @param buffer  toml to parse.
     * @return toml parse into TomlParseResult.
     * @throws IOException if the path is wrong.
     */
    @SuppressWarnings("PMD.LawOfDemeter")
    @SuppressFBWarnings
    TomlParseResult parseToToml(String buffer) throws IOException {
        TomlParseResult result = Toml.parse(buffer);
        result.errors().forEach(error -> logger.error(error.toString()));
        return result;
    }

    /**
     * Parse TomlParseResult in dictionary.

     * @param toml result of parsing by toml.
     * @return Map key/value.
     */
    static Map<String,TomlValueTypeWrapper> parseToMap(TomlParseResult toml) {
        Map<String,TomlValueTypeWrapper> ret = new HashMap<String,TomlValueTypeWrapper>();
        for (String key : toml.dottedKeySet()) {
            ret.put(key, parseObject(toml.get(key))); 
        }
        return ret;
    }

    private static Map<String, TomlValueTypeWrapper> parseTableToMap(TomlTable t) {
        Map<String, TomlValueTypeWrapper> ret = new HashMap<String, TomlValueTypeWrapper>();
        for (String key : t.keySet()) {

            ret.put(key, parseObject(t.get(key)));
        }
        return ret;
    }

    static TomlValueTypeWrapper[] parseArrayToValue(TomlArray a) {
        TomlValueTypeWrapper[] ret = new TomlValueTypeWrapper[a.size()];
        for (int i = 0; i < a.size(); i++) {
            ret[i] = parseObject(a.get(i));
        }
        return ret;
    }

    /**
     * Method to parse poly type array.

     * @param res hashmap already treated.
     * @param toWrite string toWrite.
     * @return hashmap with poly type array.
     */
    @SuppressWarnings("PMD.LawOfDemeter")
    private static Map<String,TomlValueTypeWrapper> 
        parseArray(Map<String,TomlValueTypeWrapper> res, String toWrite) {
        Pattern p = Pattern.compile("([a-z][a-zA-Z0-9_]*)\s*=\s*(\\[.*\\])");  
        Matcher m = p.matcher(toWrite);
        while (m.find()) {
            String[] keyValue = m.group().split("\s*=[\s.]*");
            if (res.get(keyValue[0]) == null) {
                TomlValueTypeWrapper arrayParsed = arrayTreatment(keyValue[1]);
                res.put(keyValue[0], arrayParsed);

            }
        }
        return res;
    }

    @SuppressWarnings("PMD.LawOfDemeter")
    private static TomlValueTypeWrapper arrayTreatment(String toTreat) {
        String[] arrayToParse = toTreat.substring(1,toTreat.length() - 1)
            .split("(\s*\\]\s*,\s*\\[\s*)|(\s*\\]\s*,\s*)|(,)");
        TomlValueTypeWrapper[] arrayParsed = new TomlValueTypeWrapper[arrayToParse.length];
        for (int i = 0; i < arrayParsed.length; i++) {
            arrayParsed[i] = convertObject(arrayToParse[i]);
        }
        return new TomlArrayWrapper(arrayParsed);
    }

    /**
     * Parse an object in a TomlValueWrapper.

     * @param o object to parse from TomlParseResult.
     * @return TomlValueTypeWrapper corresponding to o.
     */
    @SuppressWarnings({"PMD.LawOfDemeter", "PMD.CyclomaticComplexity"})
    private static TomlValueTypeWrapper parseObject(Object o) {
        if (o instanceof String) {
            return new TomlStringWrapper((String) o);
        } else if (o instanceof Long) {
            return new TomlIntegerWrapper(((Long) o).intValue());
        } else if (o instanceof Double) {
            return new TomlFloatWrapper(((Double) o).floatValue());
        } else if (o instanceof Boolean) {
            return new TomlBooleanWrapper((Boolean) o);
        } else if (o instanceof OffsetDateTime) {
            return new TomlOffsetDateTimeWrapper((OffsetDateTime) o);
        } else if (o instanceof LocalDateTime) {
            return new TomlLocalDateTimeWrapper((LocalDateTime) o);
        } else if (o instanceof LocalDate) {
            return new TomlLocalDateWrapper((LocalDate) o);
        } else if (o instanceof LocalTime) {
            return new TomlLocalTimeWrapper((LocalTime) o);
        } else if (o instanceof TomlArray) {
            return new TomlArrayWrapper(parseArrayToValue((TomlArray) o));
        } else if (o instanceof TomlTable) {
            return new TomlTableWrapper(parseTableToMap((TomlTable) o));  
        }
        throw new IllegalArgumentException("Object to parse has a wrong type."
            .concat(o.getClass().getName()));
    }

    /**
     * Function converting objects inside a toString array into
     * TomlValueTypeWrapper with TomlValueTypeWrapper objects inside.
     *
     * @param value the string that we convert.
     * @return the TomlValueTypeWrapper corresponding to the object.
     */
    public static TomlValueTypeWrapper convertObject(String value) {
        if (value.matches("true|false")) {
            return new TomlBooleanWrapper(Boolean.parseBoolean(value));
        } else if (value.matches("\s*-?[0-9]+\\.[0-9]*\s*")) {
            return new TomlFloatWrapper(Float.parseFloat(value.replaceAll("\s","")));
        } else if (value.matches("\\s*-?[0-9]+\\s*")) {
            return new TomlIntegerWrapper(
                Integer.parseInt(value.replaceAll("\s","")));
        } else if (value.matches("\\d{4}-((0\\d)|(1[0-2]))-(([0-2]\\d)|(3[01]))T([01]"
            + "\\d|2[0-3]):[0-5]\\d:[0-5]\\d(\\.\\d{6})?")) {
            return new TomlLocalDateTimeWrapper(LocalDateTime.parse(value));
        } else if (value.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            return new TomlLocalDateWrapper(LocalDate.parse(value));
        } else if (value.matches("([01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d\\.\\d{6}")) {
            return new TomlLocalTimeWrapper(LocalTime.parse(value));
        } else if (value.matches("\\d{4}-(0\\d|1[0-2])-([0-2]\\d|3[01])T([01]\\d|"
            + "2[0-3]):[0-5]\\d:[0-5]\\d"
            + "(Z|((.\\d{6})?-([01]\\d|2[0-3]):[0-5]\\d))")) {
            return new TomlOffsetDateTimeWrapper(OffsetDateTime.parse(value));
        } else if (value.matches("\\[.*\\]")) {
            return arrayTreatment(value);
        } else {
            return new TomlStringWrapper(value);
        }
    }

    
}