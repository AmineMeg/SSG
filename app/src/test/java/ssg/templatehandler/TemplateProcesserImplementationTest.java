package ssg.templatehandler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;
import ssg.exceptions.TemplateProcesserException;
import ssg.filereader.FileReader;

/**
 * Junit test class for TemplateReaderImplementation.
 */
@SuppressWarnings({"PMD.TooManyMethods", "PMD.LawOfDemeter"})
class TemplateProcesserImplementationTest {

    /**
     * FileReader dependency instance.
     */
    @Inject
    private final FileReader reader = Mockito.mock(FileReader.class);

    /**
     * Guice dependencies injector.
     */
    private final Injector injector = Guice.createInjector(new AbstractModule() {
        @Override
        protected void configure() {
            bind(FileReader.class)
                    .annotatedWith(Names.named("FileReader"))
                    .toInstance(reader);
        }
    });

    /**
     * Processer instance to test.
     */
    private TemplateProcesserImplementation processer;

    /**
     * Path for templates test files.
     */
    private static final String TEST_TEMPLATES_PATH = "./";

    /**
     * Initialisation.
     */
    @BeforeEach
    void processerInit() {
        processer = new TemplateProcesserImplementation();
        injector.injectMembers(processer);
    }

    /**
     * pure HTML content must remain unchanged after processing.
     */
    @Test
    void pureHtmlProcessedRemainUnchanged() {
        String input = """
             <!DOCTYPE html>
            <html>
                <body>
                    <h1>Heading/h1>
                    <p>Paragraph</p>
                </body>
            </html>
            """;
        try {
            assertEquals(input, processer.process(input, null, TEST_TEMPLATES_PATH),
                    "Processed pure HTML must match original content");
        } catch (TemplateProcesserException e) {
            fail("Unexpected exception was thrown", e);
        }
    }

    /**
     * processing a template with variable resolution from context.
     */
    @Test
    void variableResolution() {
        String variableName = "content";
        String variableContent = "<p>Le Point Aveugle</p>";
        Map<String, Object> context = new HashMap<>();
        context.put(variableName, variableContent);

        String input = """
                <div id="article">{{ %s }}</div>
            """.formatted(variableName);
        String expected = """
                <div id="article">%s</div>
            """.formatted(variableContent);

        try {
            assertEquals(expected, processer.process(input, context, TEST_TEMPLATES_PATH),
                    "processed template didn't match expected content");
        } catch (TemplateProcesserException e) {
            fail("Unexpected exception was thrown", e);
        }
    }

    /**
     * processing a template with undefined variable must throw exception.
     */
    @Test
    void undefinedVariableResolutionThrowsException() {
        String variableName = "title";
        Map<String, Object> context = new HashMap<>();

        String input = """
                <div id="article">{{ %s }}</div>
            """.formatted(variableName);

        assertThrows(TemplateProcesserException.class,
            () -> processer.process(input, context, TEST_TEMPLATES_PATH),
            "attempt to resolve an undefined variable did not throw the expected exception");
    }

    /**
     * processing a template with map.key resolution from context.
     */
    @Test
    void keyResolution() {
        String keyName = "title";
        String keyContent = "La theories des types";
        String mapName = "metadata";

        Map<String, String> map = new HashMap<>();
        map.put(keyName, keyContent);

        Map<String, Object> context = new HashMap<>();
        context.put(mapName, map);

        String input = """
                <title>{{ %s.%s }}</title>
            """.formatted(mapName, keyName);
        String expected = """
                <title>%s</title>
            """.formatted(keyContent);

        try {
            assertEquals(expected, processer.process(input, context, TEST_TEMPLATES_PATH),
                    "processed template didn't match expected content");
        } catch (TemplateProcesserException e) {
            fail("Unexpected exception was thrown", e);
        }
    }

    @Test
    void includeSimpleFileIsReplacedBySimpleFileContent() {
        String file = "menu.html";
        String fileContent = "<p> content </p>";
        String templateContent = """
            <body>
              <div id="menu">%s</div>
            </body>
            """;
        // '%%' escapes '%' when using String.formatted()
        String input = templateContent.formatted("{%% include '%s' %%}".formatted(file));
        String expectedOutput = templateContent.formatted(fileContent);

        try {
            when(reader.read(TEST_TEMPLATES_PATH + file)).thenReturn(fileContent);
            assertEquals(expectedOutput, processer.process(input, null, TEST_TEMPLATES_PATH),
                    "processed template didn't match expected content");
        } catch (TemplateProcesserException | IOException e) {
            fail("Unexpected exception was thrown", e);
        }
    }

    /**
     * attempt to include a missing file must throw an exception.
     */
    @Test
    void includeMissingFileThrowsException() {
        String missingFile = "missing.html";
        String input = "<p>{%% include '%s' %%}</p>".formatted(missingFile);
        try {
            when(reader.read(TEST_TEMPLATES_PATH + missingFile))
                .thenThrow(new IOException());
            assertThrows(TemplateProcesserException.class,
                () -> processer.process(input, null, TEST_TEMPLATES_PATH),
                "attempt to include missing file did not raise TemplateProcesserException");
        } catch (IOException e) {
            fail("unexpected exception was thrown", e);
        }
    }

    /**
     * attempt to include a file above working directory must throw an exception.
     */
    @Test
    void includeFileAboveWorkingDirThrowsException() {
        String input = """
            <body>
            <div id="menu">{% include "../menu.html" %}</div>
            </body>
            """;
        assertThrows(TemplateProcesserException.class,
                () -> processer.process(input, null, TEST_TEMPLATES_PATH),
                "attempt to include missing file did not raise TemplateProcesserException");
    }

    @Test
    void nestedIncludesResolution() {
        String file1 = "nested.html";
        String file2 = "nested2.html";
        String file1Content = """
            <body>
            %s
            </body>
            """;
        String file2Content = "<p>%s</p>";
        String template = """
            <html>
            <head>
                <title>MyTitle</title>
            </head>
                %s
            </html>
            """;

        String variableName = "content";
        String variableContent = "foobar";
        Map<String, Object> context = new HashMap<>();
        context.put(variableName, variableContent);

        String includeTag = "{%% include '%s' %%}";
        String file2PreProcessed = file2Content.formatted("{{ " + variableName + " }}");
        String file1PreProcessed = file1Content.formatted(includeTag.formatted(file2));

        String file2Resolved = file2Content.formatted(variableContent);
        String file1Resolved = file1Content.formatted(file2Resolved);

        String input = template.formatted(includeTag.formatted(file1));
        String expectedOutput = template.formatted(file1Resolved);
        try {
            when(reader.read(TEST_TEMPLATES_PATH + file1)).thenReturn(file1PreProcessed);
            when(reader.read(TEST_TEMPLATES_PATH + file2)).thenReturn(file2PreProcessed);
            assertEquals(expectedOutput, processer.process(input, context, TEST_TEMPLATES_PATH),
                    "processed template didn't match expected content");
        } catch (TemplateProcesserException | IOException e) {
            fail("Unexpected exception was thrown", e);
        }
    }

    /**
     * attempt to process cyclic inclusions must throw an exception.
     */
    @Test
    void processCyclicInclusionsThrowsException() {
        String file1 = "cyclic1.html";
        String file2 = "cyclic2.html";
        String filesContent = "<p>{%% include '%s' %%}</p>";
        String file1Content = filesContent.formatted(file2);
        String file2Content = filesContent.formatted(file1);

        try {
            when(reader.read(TEST_TEMPLATES_PATH + file1)).thenReturn(file1Content);
            when(reader.read(TEST_TEMPLATES_PATH + file2)).thenReturn(file2Content);
            assertThrows(TemplateProcesserException.class,
                () -> processer.process(file1Content, null, TEST_TEMPLATES_PATH),
                "attempt to process cyclic inclusions did not raise TemplateProcesserException");
        } catch (IOException e) {
            fail("Unexpected exception was thrown", e);
        }
    }

    /**
     * processing a wrongly formatted template.
     */
    @Test
    void includeInsideFilterThrowsException() {
        String input = """
            <body>
              <div id="menu">{{ include "menu.html" }}</div>
            </body>
            """;
        assertThrows(TemplateProcesserException.class,
            () -> processer.process(input, null, TEST_TEMPLATES_PATH),
            "processing a badly formatted template did not raise TemplateProcesserException");
    }

    /**
     * processing template with a for loop.
     */
    @Test
    void forLoop() {
        String input = """
            {% for i in range(2) %}
             {{ i }}
            {% endfor %}
            """;
        String expectedOutput = """

             0

             1

            """;
        try {
            assertEquals(expectedOutput, processer.process(input, null, TEST_TEMPLATES_PATH),
                    "processed template didn't match expected content");
        } catch (TemplateProcesserException e) {
            fail("Unexpected exception was thrown", e);
        }
    }

    @Test
    void foreachLoopOverArrayOfMapsResolution() {
        Map<String, Object> context = new HashMap<>();
        String mapName = "metadata";
        String keyName = "categories";
        Map<String, Object> map = new HashMap<>();
        List<Map<String,Object>> keyValue = List.of(
                Map.of("name", "Accueil", "url", "index.html"),
                Map.of("name", "Nos pains", "url", "pains/"));

        map.put(keyName, keyValue);
        context.put(mapName, map);

        String input = """
                {% for category in metadata.categories %}
                    <li><a href="{{ category.url }}">{{ category.name }}</a></li>
                {% endfor %}
                """;
        String expectedOutput = """
                
                    <li><a href="index.html">Accueil</a></li>
                                    
                    <li><a href="pains/">Nos pains</a></li>
                                
                """;
        try {
            assertEquals(expectedOutput, processer.process(input, context, TEST_TEMPLATES_PATH),
                    "processed template didn't match expected content");
        } catch (TemplateProcesserException e) {
            fail("Unexpected exception was thrown", e);
        }
    }

    @Test
    void listFilesIsCalledButNoContentFilesExists1(@TempDir File tempDir) throws IOException {
        File template = new File(tempDir, "templates");
        if (!template.mkdir()) {
            fail("fail to set up test environment");
        }
        String input = """
            {%% for str in list_files("", %s) %%}
            <p>{{ str }}</p>{%% endfor %%}
            """;
        try {
            assertEquals("\n",
                    processer.process(input.formatted("true"), null, template.toString()),
                    "processed content doesnt match expected content");
        } catch (TemplateProcesserException e) {
            fail("Unexpected exception was thrown");
        }
    }

    @Test
    void listFilesIsCalledButNoContentFilesExists2(@TempDir File tempDir) throws IOException {
        File template = new File(tempDir, "templates");
        if (!template.mkdir()) {
            fail("fail to set up test environment");
        }
        String input = """
            {%% for str in list_files("", %s) %%}
            <p>{{ str }}</p>{%% endfor %%}
            """;
        try {
            assertEquals("\n",
                    processer.process(input.formatted("false"), null, template.toString()),
                    "processed content doesnt match expected content");
        } catch (TemplateProcesserException e) {
            fail("Unexpected exception was thrown");
        }
    }

    @Test
    void listFilesRec(@TempDir File tempDir) throws IOException {
        File content = new File(tempDir, "content");
        String subContentName = "subcontent";
        File subContent = new File(content, subContentName);
        File template = new File(tempDir, "templates");
        if (!content.mkdir() || !template.mkdir() || !subContent.mkdir()) {
            fail("fail to set up test environment");
        }

        String fileName = "index.html";
        File file = new File(content, fileName);

        String subFileName = "menu.html";
        File subFile = new File(subContent, subFileName);
        if (!file.createNewFile() || !subFile.createNewFile()) {
            fail("fail to set up test environment");
        }

        String input = """
            {% for str in list_files("", true) %}
            <p>{{ str }}</p>{% endfor %}
            """;
        String expectedOutput = """

            <p>%s</p>
            <p>%s</p>
            """.formatted(subContentName + "/" + subFileName, fileName);
        try {
            assertEquals(expectedOutput, processer.process(input, null, template.toString()),
                    "processed content doesnt match expected content");
        } catch (TemplateProcesserException e) {
            fail("Unexpected exception was thrown");
        }
    }

    /**
     * processing template with a foreach loop over a string array.
     */
    @Test
    void foreachLoopOverArray() {
        Map<String, Object> context = new HashMap<>();
        List<String> array = List.of("foo", "bar");
        context.put("array", array);
        String input = """
            {% for item in array %}
                <li>{{ item }}</li>
            {% endfor %}
            """;
        String expectedOutput = """

                <li>foo</li>

                <li>bar</li>

            """;
        try {
            assertEquals(expectedOutput, processer.process(input, context, TEST_TEMPLATES_PATH),
                    "processed template didn't match expected content");
        } catch (TemplateProcesserException e) {
            fail("Unexpected exception was thrown");
        }
    }
}
