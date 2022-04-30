package ssg.dependencymanager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ssg.config.SiteStructureVariable;
import ssg.filemodifiedmanager.FileModifiedManager;
import ssg.filereader.FileReader;
import ssg.filewriter.FileWriter;
import ssg.parsertoml.ParserToml;
import ssg.tomlvaluetypewrapper.TomlArrayWrapper;
import ssg.tomlvaluetypewrapper.TomlStringWrapper;
import ssg.tomlvaluetypewrapper.TomlValueTypeWrapper;





/**
 * Junit test class for DependencyManager.
 */

@SuppressWarnings({"PMD.LawOfDemeter", "PMD.AvoidCatchingGenericException",
                      "PMD.TooManyMethods", "PMD.ExcessiveImports", "PMD.UnusedPrivateField",
                      "PMD.DoubleBraceInitialization"})
class DependencyManagerTest {

    /**
     * Class to be tested.
     */
    private DependencyManager dependencyManager;

    /**
     * FileModifiedManager dependency.
     */
    @Inject
    private final FileModifiedManager fileModifiedManager =
            Mockito.mock(FileModifiedManager.class);

    /**
     * FileReader dependency.
     */
    @Inject
    private final FileReader fileReader =
            Mockito.mock(FileReader.class);

    /**
     * ParserToml dependency.
     */
    @Inject
    private final ParserToml parserToml =
            Mockito.mock(ParserToml.class);

    /**
     * FileWriter dependency.
     */
    @Inject
    private final FileWriter fileWriter =
            Mockito.mock(FileWriter.class);

    /**
     * Temporary output directory.
     */
    static final String outputDirectory = "outputTest/";


    /**
     * Temporary output directory.
     */
    static final String inputDirectory = "testInput/";


    /**
     * Temporary output directory.
     */
    static final String template = "templates/";

    /**
     * Temporary input directory.
     */
    static final String content = "content/";


    /**
     * Test file name.
     */
    static final String nonExistsingFileName = "nonExisting.md";


    /**
     * Test file name.
     */
    static final String fileName = "test.md";

    /**
     * Test file name.
     */
    static final String fileName2 = "test2.md";

    /**
     * Test dependency name.
     */
    static final String dependencyName = "template.html";

    /**
     * Test dependency name.
     */
    static final String dependencyName2 = "template2.html";

    /**
     * Test toml content.
     */
    static final String tomlContent = fileName
            + " = [ \""
            + dependencyName
            + "\" ]"
            + "\n"
            + fileName2
            + " = [ \""
            + dependencyName
            + ", "
            + dependencyName2
            + "\" ]";

    /**
     * Test toml content bad.
     */
    static final String badTomlContent = "test.md = \"abcd\"";

    /**
     * Test parsed toml map.
     */
    static final Map<String, TomlValueTypeWrapper> tomlMap =  new HashMap<>() {
        {
            put(fileName, new TomlArrayWrapper(new TomlStringWrapper(dependencyName)));
            put(fileName2, new TomlArrayWrapper(new TomlStringWrapper(dependencyName),
                    new TomlStringWrapper(dependencyName2)));
        }
    };

    /**
     * Test parsed toml map.
     */
    static final Map<String, TomlValueTypeWrapper> badTomlMap =  new HashMap<>() {
        {
            put("test.md", new TomlStringWrapper("abcd"));

        }
    };

    /**
     * Test TomlArrayWrapper.
     */
    static final TomlArrayWrapper tomlArrayWrapper =
            new TomlArrayWrapper(new TomlStringWrapper(dependencyName),
            new TomlStringWrapper(dependencyName2));

    /**
     * Log4J Logger.
     */
    private static final Logger logger = LogManager.getLogger();

    /**
     * Inject mock dependencies into the tested class.
     */
    private final Injector injector = Guice.createInjector(new AbstractModule() {
        @Override
        protected void configure() {
            bind(FileModifiedManager.class)
                    .annotatedWith(Names.named("FileModifiedManager"))
                    .toInstance(fileModifiedManager);
            bind(FileReader.class)
                    .annotatedWith(Names.named("FileReader"))
                    .toInstance(fileReader);
            bind(ParserToml.class)
                    .annotatedWith(Names.named("ParserToml"))
                    .toInstance(parserToml);
            bind(FileWriter.class)
                    .annotatedWith(Names.named("FileWriter"))
                    .toInstance(fileWriter);
        }
    });

    /**
     * Set up the class before each test.
     */
    @BeforeEach
    void setup() {
        dependencyManager = DependencyManager.getInstance(outputDirectory,
                inputDirectory + template);
        injector.injectMembers(dependencyManager);
    }

    @BeforeAll
    static void init() {
        try {
            clean();
            Files.createDirectory(Path.of(outputDirectory));
            Files.createDirectory(Path.of(inputDirectory));
            Files.createDirectory(Path.of(inputDirectory + template));
            Files.createFile(Path.of(inputDirectory + template + dependencyName));
            Files.createFile(Path.of(outputDirectory
                    + SiteStructureVariable.DEPENDENCIES_FILE));

        } catch (IOException e) {
            fail(e);
        }
    }

    @AfterAll
    static void clean() {
        try {
            Files.deleteIfExists(Path.of(outputDirectory
                    + SiteStructureVariable.DEPENDENCIES_FILE));
            Files.deleteIfExists(Path.of(inputDirectory + template + dependencyName));
            Files.deleteIfExists(Path.of(outputDirectory));
            Files.deleteIfExists(Path.of(inputDirectory + template));
            Files.deleteIfExists(Path.of(inputDirectory));
        } catch (IOException e) {
            fail(e);
        }
    }



    @Test
    void dependencyProperlyAdded() {
        dependencyManager.addDependency(inputDirectory + content + fileName, dependencyName);
        dependencyManager.addDependency(inputDirectory + content + fileName,dependencyName2);
        assertEquals("{" + inputDirectory + content
                        + fileName + "=[template.html, template2.html]}",
                dependencyManager.dependenciesToString(),
                "dependencies were added but dependency content did not match");
        dependencyManager.deleteFileFromDependencies(inputDirectory + content + fileName);
    }

    @Test
    void isRecomputeRequiredWhenDependencyModified() {

        try {
            //GIVEN

            dependencyManager.addDependency(inputDirectory + content
                    + fileName,inputDirectory + template + dependencyName);

            when(fileModifiedManager.wasFileModified(inputDirectory
                    + content + fileName,outputDirectory)).thenReturn(false);
            when(fileModifiedManager.wasFileModified(inputDirectory + template + dependencyName,
                    outputDirectory)).thenReturn(true);

            //THEN

            assertTrue(dependencyManager.isRecomputeRequired(inputDirectory + content + fileName),
                    "Recompute should be required but wasn't");

            dependencyManager.deleteFileFromDependencies(inputDirectory + content + fileName);


        } catch (IOException e) {
            fail(e);
        }
    }

    @Test
    void isRecomputeRequiredWhenNoDependencyModified() {

        try {
            //GIVEN

            dependencyManager.addDependency(inputDirectory + content
                    + fileName,inputDirectory + template + dependencyName);
            dependencyManager.addLastTimeModifiedEntry(inputDirectory
                    + template + dependencyName, LocalDateTime.now());
            when(fileModifiedManager.wasFileModified(inputDirectory
                    + content + fileName,outputDirectory)).thenReturn(false);
            when(fileModifiedManager.wasFileModified(inputDirectory
                            + template + dependencyName,
                    outputDirectory)).thenReturn(false);

            //THEN

            assertFalse(dependencyManager.isRecomputeRequired(inputDirectory + content + fileName),
                    "Recompute shouldn't be required but was");

            dependencyManager.deleteFileFromDependencies(inputDirectory + content + fileName);
            dependencyManager.deleteEntryLastTimeModifiedEntry(
                    inputDirectory + template + dependencyName);


        } catch (IOException e) {
            fail(e);
        }
    }

}
