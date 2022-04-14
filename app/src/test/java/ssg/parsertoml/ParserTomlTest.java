package ssg.parsertoml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import ssg.tomlvaluetypewrapper.TomlArrayWrapper;
import ssg.tomlvaluetypewrapper.TomlBooleanWrapper;
import ssg.tomlvaluetypewrapper.TomlFloatWrapper;
import ssg.tomlvaluetypewrapper.TomlIntegerWrapper;
import ssg.tomlvaluetypewrapper.TomlLocalDateTimeWrapper;
import ssg.tomlvaluetypewrapper.TomlLocalDateWrapper;
import ssg.tomlvaluetypewrapper.TomlLocalTimeWrapper;
import ssg.tomlvaluetypewrapper.TomlOffsetDateTimeWrapper;
import ssg.tomlvaluetypewrapper.TomlStringWrapper;
import ssg.tomlvaluetypewrapper.TomlValueTypeWrapper;

/**
 * Junit test class for ParseToml.
 */
@SuppressWarnings({ "PMD.LawOfDemeter", "PMD.JUnitTestContainsTooManyAsserts", 
    "PMD.TooManyMethods", "PMD.ExcessiveImports"})
class ParserTomlTest {

    /**
     * Error message for wrong parse.
     */
    static final String wrongParseMsg = "Wrong toml conversion.";

    /**
     * Logger for messages.
     */
    private static final Logger logger = LogManager.getLogger();

    /**
     * Class To Test.
     */
    private final ParserToml parserToml = new ParserTomlImplementation();


    /**
     * Test parseObject with string objects.

     * @throws IOException case wrong parse
     */
    //@Disabled
    @Test
    @SuppressWarnings({ "PMD.JUnitTestContainsTooManyAsserts", "PMD.LawOfDemeter" })
    void stringParseObject() throws IOException {
        logger.info("stringParseObject() begins");
        try {         
            String stringError = "The two strings are not equal.";
            Map<String, TomlValueTypeWrapper> res = 
                ((ParserTomlImplementation) parserToml).parse(TOML);

            assertEquals("The quick brown fox jumps over the lazy dog.",
                res.get("str1").toString(), stringError);

            assertEquals("The quick brown fox jumps over the lazy dog.",
                res.get("str2").toString(), stringError);

            assertEquals("The quick brown fox jumps over the lazy dog.",
                res.get("str3").toString(), stringError);

            assertEquals("Here are two quotation marks: \"\". Simple enough.",
                res.get("str4").toString(), stringError);
            
            assertEquals("Here are three quotation marks: \"\"\".",
                res.get("str5").toString(), stringError);
            
            assertEquals("Here are fifteen quotation marks: \"\"\"\"\"\"\"\"\"\"\"\"\"\"\".",
                res.get("str6").toString(), stringError);
            
            /*assertEquals("This,\" she said, \"is just a pointless statement.",
                res.get("str7").toString(), stringError);*/

        } catch (NoSuchFileException e) {
            if (logger.isErrorEnabled()) {
                logger.error(wrongParseMsg, e);
            }
            fail("File does not exist.");
        } catch (IllegalArgumentException e) {
            if (logger.isErrorEnabled()) {
                logger.error(wrongParseMsg);
                logger.error(e);
            }
            fail("Try to parse a non-allowed type.");
        }
    }

    /**
     * Test parseObject with int objects.

     * @throws IOException case wrong parse
     */
    @Test
    @SuppressWarnings({ "PMD.JUnitTestContainsTooManyAsserts", "PMD.LawOfDemeter" })
    void intParseObject() throws IOException {
        String intError = "The two int are not equal.";
        try {
            Map<String, TomlValueTypeWrapper> res = 
                ((ParserTomlImplementation) parserToml).parse(TOML);
            
            assertEquals("99",
                res.get("int1").toString(), intError);

            assertEquals("42",
                res.get("int2").toString(), intError);

            assertEquals("0",
                res.get("int3").toString(), intError);

            assertEquals("-17",
                res.get("int4").toString(), intError);
            
            assertEquals("1000",
                res.get("int5").toString(), intError);
            
            assertEquals("5349221",
                res.get("int6").toString(), intError);

        } catch (NoSuchFileException e) {
            if (logger.isErrorEnabled()) {
                logger.error(wrongParseMsg, e);
            }
            fail("File does not exist.");
        } catch (IllegalArgumentException e) {
            if (logger.isErrorEnabled()) {
                logger.error(wrongParseMsg);
                logger.error(e.getClass().getName());
                logger.error(e.getMessage());
            }
            fail("Try to parse a non-allowed type.");
        }
    }

    /**
     * Test parseObject with float objects.

     * @throws IOException case wrong parse
     */
    @Test
    @SuppressWarnings({ "PMD.JUnitTestContainsTooManyAsserts", "PMD.LawOfDemeter" })
    void floatParseObject() throws IOException {
        try {
            Map<String, TomlValueTypeWrapper> res = 
                ((ParserTomlImplementation) parserToml).parse(TOML);
            String floatError = "The two float are not equal.";
            
            assertEquals("1.0",
                res.get("flt1").toString(), floatError);

            assertEquals("3.1415",
                res.get("flt2").toString(), floatError);

            assertEquals("-0.01",
                res.get("flt3").toString(), floatError);

            assertEquals("5.0E22",
                res.get("flt4").toString(), floatError);
            
            /*assertEquals("1.0E06",
                res.get("flt5").toString(), floatError);*/
            
            assertEquals("-0.02",
                res.get("flt6").toString(), floatError);
            
            assertEquals("6.626E-34",
                res.get("flt7").toString(), floatError);

        } catch (NoSuchFileException e) {
            if (logger.isErrorEnabled()) {
                logger.error(wrongParseMsg, e);
            }
            fail("File does not exist.");
        } catch (IllegalArgumentException e) {
            if (logger.isErrorEnabled()) {
                logger.error(wrongParseMsg);
                logger.error(e.getClass().getName());
                logger.error(e.getMessage());
            }
            fail("Try to parse a non-allowed type.");
        }
    }

    /**
     * Test parseObject with boolean objects.

     * @throws IOException case wrong parse
     */
    @Test
    @SuppressWarnings({ "PMD.JUnitTestContainsTooManyAsserts", "PMD.LawOfDemeter" })
    void booleanParseObject() throws IOException {
        try {
            Map<String, TomlValueTypeWrapper> res = 
                ((ParserTomlImplementation) parserToml).parse(TOML);
            String booleanError = "The two boolean are not equal.";
            
            assertEquals("true",
                res.get("bool1").toString(), booleanError);

            assertEquals("false",
                res.get("bool2").toString(), booleanError);

        } catch (NoSuchFileException e) {
            if (logger.isErrorEnabled()) {
                logger.error(wrongParseMsg, e);
            }
            fail("File does not exist.");
        } catch (IllegalArgumentException e) {
            if (logger.isErrorEnabled()) {
                logger.error(wrongParseMsg);
                logger.error(e.getClass().getName());
                logger.error(e.getMessage());
            }
            fail("Try to parse a non-allowed type.");
        }
    }

    /**
     * Test parseObject with OffsetDateTime objects.

     * @throws IOException case wrong parse
     */
    @Test
    @SuppressWarnings({ "PMD.JUnitTestContainsTooManyAsserts", "PMD.LawOfDemeter" })
    void odtParseObject() throws IOException {
        try {
            Map<String, TomlValueTypeWrapper> res = 
                ((ParserTomlImplementation) parserToml).parse(TOML);
            String odtError = "The two OffsetDateTime are not equal.";
            
            assertEquals("1979-05-27T07:32Z",
                res.get("odt1").toString(), odtError);

            assertEquals("1979-05-27T00:32-07:00",
                res.get("odt2").toString(), odtError);

            assertEquals("1979-05-27T00:32:00.999999-07:00",
                res.get("odt3").toString(), odtError);

        } catch (NoSuchFileException e) {
            if (logger.isErrorEnabled()) {
                logger.error(wrongParseMsg, e);
            }
            fail("File does not exist.");
        } catch (IllegalArgumentException e) {
            if (logger.isErrorEnabled()) {
                logger.error(wrongParseMsg);
                logger.error(e.getClass().getName());
                logger.error(e.getMessage());
            }
            fail("Try to parse a non-allowed type.");
        }
    }

    /**
     * Test parseObject with LocalDateTime, LocalDate and LocalTime objects.

     * @throws IOException case wrong parse
     */
    @Test
    @SuppressWarnings({ "PMD.JUnitTestContainsTooManyAsserts", "PMD.LawOfDemeter" })
    void ldtParseObject() throws IOException {
        String ldtError = "The two LocalDateTime are not equal.";
        try {
            Map<String, TomlValueTypeWrapper> res = 
                ((ParserTomlImplementation) parserToml).parse(TOML);
            
            assertEquals("1979-05-27T07:32",
                res.get("ldt1").toString(), ldtError);

            assertEquals("1979-05-27T00:32:00.999999",
                res.get("ldt2").toString(), ldtError);

            assertEquals("1979-05-27",
                res.get("ld1").toString(), ldtError);

            assertEquals("07:32",
                res.get("lt1").toString(), ldtError);
    
            assertEquals("00:32:00.999999",
                res.get("lt2").toString(), ldtError);

        } catch (NoSuchFileException e) {
            if (logger.isErrorEnabled()) {
                logger.error(wrongParseMsg, e);
            }
            fail("File does not exist.");
        } catch (IllegalArgumentException e) {
            if (logger.isErrorEnabled()) {
                logger.error(wrongParseMsg);
                logger.error(e.getClass().getName());
                logger.error(e.getMessage());
            }
            fail("Try to parse a non-allowed type.");
        }
    }

    /**
     * Test parseObject with Array objects.

     * @throws IOException case wrong parse
     */
    @Test
    @SuppressWarnings({ "PMD.JUnitTestContainsTooManyAsserts", "PMD.LawOfDemeter" })
    void arrayParseObject() throws IOException {
        try {
            String arrayError = "The two array are not equal.";
            Map<String, TomlValueTypeWrapper> res = 
                ((ParserTomlImplementation) parserToml).parse(TOML);

            assertEquals("<ul><li>1</li><li>2</li><li>3</li></ul>",
                res.get("integers").toString().replaceAll("[\n\t]",""), arrayError);
            
            assertEquals("<ul><li>red</li><li>yellow</li><li>green</li></ul>",
                res.get("colors").toString().replaceAll("[\n\t]",""), arrayError);

            assertEquals("""
                <ul><li><ul><li>1</li><li>2</li></ul></li><li><ul><li>3</li>
                <li>4</li><li>5</li></ul></li></ul>""".replaceAll("[\n\t]",""),
                res
                .get("nested_array_of_int").toString().replaceAll("[\n\t]",""), arrayError);
                
            assertEquals("""
                <ul><li><ul><li>1</li><li>2</li></ul></li><li><ul><li>a</li>
                <li>b</li><li>c</li></ul></li></ul>""".replaceAll("[\n\t]",""),
                res
                .get("nested_mixed_array").toString().replaceAll("[\n\t]",""), arrayError);

            assertEquals("<ul><li>all</li><li>strings</li><li>are the same</li><li>type</li></ul>",
                res.get("string_array").toString().replaceAll("[\n\t]",""), arrayError);

            assertEquals("""
                <ul><li>0.1</li><li>0.2</li><li>0.5</li>
                <li>1</li><li>2</li><li>5</li></ul>""".replaceAll("[\n\t]",""),
                res.get("numbers").toString().replaceAll("[\n\t]",""), arrayError);

            /*assertEquals("",
                res
                .get("contributors").toString(), arrayError);*/

            assertEquals("<ul><li>1</li><li>2</li><li>3</li></ul>",
                res.get("integers2").toString().replaceAll("[\n\t]",""), arrayError);

            assertEquals("<ul><li>1</li><li>2</li></ul>",
                res.get("integers3").toString().replaceAll("[\n\t]",""), arrayError);

        } catch (NoSuchFileException e) {
            if (logger.isErrorEnabled()) {
                logger.error(wrongParseMsg, e);
            }
            fail("File does not exist.");
        } catch (IllegalArgumentException e) {
            if (logger.isErrorEnabled()) {
                logger.error(wrongParseMsg);
                logger.error(e.getMessage());
            }
            fail("Try to parse a non-allowed type.");
        }
    }

    /**
     * Test parseObject with Table objects.

     *  IOException
     */
    @Test
    @SuppressWarnings({ "PMD.JUnitTestContainsTooManyAsserts", "PMD.LawOfDemeter" })
    void tableParseObject() throws IOException {
        try {
            Map<String, TomlValueTypeWrapper> res = 
                ((ParserTomlImplementation) parserToml).parse(TOML);
            String tableError = "The two table are not equal.";

            assertEquals("red",
                res.get("fruit.apple.color")
                .toString().replaceAll("[\n\t]",""), tableError);

            assertEquals("true",
                res
                .get("fruit.apple.taste.sweet")
                .toString(), tableError);

            
        } catch (NoSuchFileException e) {
            if (logger.isErrorEnabled()) {
                logger.error(wrongParseMsg, e);
            }
            fail("File does not exist.");
        } catch (IllegalArgumentException e) {
            if (logger.isErrorEnabled()) {
                logger.error(wrongParseMsg);
                logger.error(e.getMessage());
            }
            fail("Try to parse a non-allowed type.");
        }
    }

    /**
     * Case we want to convert a String into TomlWrapper.
     * 
     */
    @Test
    void convertObjectTest() {
        String convertError = "The object has been converted in a bad way.";
        assertEquals(ParserTomlImplementation.convertObject("string").getClass(),
            TomlStringWrapper.class, convertError);
        assertEquals(ParserTomlImplementation.convertObject("023").getClass(),
            TomlIntegerWrapper.class, convertError);
        assertEquals(ParserTomlImplementation.convertObject("2.").getClass(),
            TomlFloatWrapper.class, convertError);
        assertEquals(ParserTomlImplementation.convertObject("false").getClass(),
            TomlBooleanWrapper.class, convertError);
        assertEquals(ParserTomlImplementation
            .convertObject("1979-05-27T00:32:00.999999-07:00").getClass(),
            TomlOffsetDateTimeWrapper.class, convertError);
        assertEquals(ParserTomlImplementation
            .convertObject("1979-05-27T00:32:00.999999").getClass(),
            TomlLocalDateTimeWrapper.class, convertError);
        assertEquals(ParserTomlImplementation.convertObject("1979-05-27").getClass(),
            TomlLocalDateWrapper.class, convertError);    
        assertEquals(ParserTomlImplementation.convertObject("00:32:00.999999").getClass(),
            TomlLocalTimeWrapper.class, convertError); 
        assertEquals(ParserTomlImplementation.convertObject("[1,2,3]").getClass(),
            TomlArrayWrapper.class, convertError);
    }



    /**
     * Content of the file readed.
     */
    static final String TOML = """
str1 = \"The quick brown fox jumps over the lazy dog.\"
str2 = \"\"\"The quick brown \\\n\n\n  fox jumps over \\\n    the lazy dog.\"\"\"
str3 = \"\"\"\\\n       The quick brown \\\n       fox jumps over \\\n
       the lazy dog.\\\n       \"\"\"
str4 = \"\"\"Here are two quotation marks: \"\". Simple enough.\"\"\"
str5 = \"\"\"Here are three quotation marks: \"\"\\\".\"\"\"
str6 = \"\"\"Here are fifteen quotation marks: \"\"\\\"\"\"\\\"\"\"\\\"\"\"\\\"\"\"\\\".\"\"\"
str7 = \"\"\"\"This,\" she said, \"is just a pointless statement.\"\"\"\"

# Integer\nint1 = +99\nint2 = 42\nint3 = 0\nint4 = -17\nint5 = 1_000\nint6 = 5_349_221

# Float\nflt1 = +1.0\nflt2 = 3.1415\nflt3 = -0.01\nflt4 = 5e+22\nflt5 = 1e06
flt6 = -2E-2\nflt7 = 6.626e-34

 # Boolean\nbool1 = true\nbool2 = false

# Offset date time\nodt1 = 1979-05-27T07:32:00Z\nodt2 = 1979-05-27T00:32:00-07:00
odt3 = 1979-05-27T00:32:00.999999-07:00

# Local date time
ldt1 = 1979-05-27T07:32:00\nldt2 = 1979-05-27T00:32:00.999999

# Local date\nld1 = 1979-05-27

# Local time\nlt1 = 07:32:00\nlt2 = 00:32:00.999999

# Array\nintegers = [ 1, 2, 3 ]\ncolors = [ \"red\", \"yellow\", \"green\" ]
nested_array_of_int = [ [ 1, 2 ], [3, 4, 5] ]
nested_mixed_array = [ [ 1, 2 ], [\"a\", \"b\", \"c\"] ]
string_array = [ \"all\", 'strings', \"\"\"are the same\"\"\", '''type''' ]
numbers = [ 0.1, 0.2, 0.5, 1, 2, 5 ]
contributors = [\"Foo Bar <foo@example.com>\",
{ name = \"Baz Qux\", email = \"bazqux@example.com\", url = \"https://example.com/bazqux\" }]
integers2 = [\n  1, 2, 3\n]
integers3 = [\n  1,\n  2, \n]

# Table\n[table-1]\nkey1 = \"some string\"\nkey2 = 123

[fruit]\napple.color = \"red\"\napple.taste.sweet = true

color = \"gray\"

all_types_array = [\"string\", 23, 2.1, false, 1979-05-27T00:32:00.999999-07:00, 
1979-05-27T00:32:00.999999, 1979-05-27, 00:32:00.999999]""";
}
