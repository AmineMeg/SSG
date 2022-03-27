package ssg.buildpage;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import java.io.IOException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ssg.exceptions.MetadataException;
import ssg.exceptions.NotMarkdownException;
import ssg.exceptions.NullArgumentException;
import ssg.filereader.FileReader;
import ssg.filesplitter.FileSplitter;
import ssg.filesplitter.Pair;
import ssg.filewriter.FileWriter;
import ssg.htmlvalidator.HtmlValidator;
import ssg.markdowntohtmlconverter.MarkdownToHtmlConverter;

/**
 * Junit test class for BuildPage.
 */
@SuppressWarnings({"PMD.LawOfDemeter", "PMD.AvoidCatchingGenericException",
    "PMD.GuardLogStatement", "PMD.SignatureDeclareThrowsException",
    "PMD.RedundantFieldInitializer",
    "PMD.MutableStaticState"})
class BuildPageTest {


    /**
     * Class to be tested.
     */
    private BuildPageImplementation buildPage;

    /**
     * FileReader dependency.
     */
    @Inject
    private final FileReader fileReader = Mockito.mock(FileReader.class);

    /**
     * FileSplitter dependency.
     */
    @Inject
    private final FileSplitter fileSplitter = Mockito.mock(FileSplitter.class);

    /**
     * ParserTOML dependency.
     */
    //private ParserTOML parserTOML;

    /**
     * MarkdownToHtmlConverter dependency.
     */
    @Inject
    private final MarkdownToHtmlConverter markdownToHtmlConverter =
            Mockito.mock(MarkdownToHtmlConverter.class);

    /**
     * TemplateHandler dependency.
     */
    //private TemplateHandler templateHandler;

    /**
     * FileWriter dependency.
     */
    @Inject
    private final FileWriter fileWriter = Mockito.mock(FileWriter.class);

    /**
     * HtmlValidator dependency.
     */
    @Inject
    private final HtmlValidator htmlValidator = Mockito.mock(HtmlValidator.class);

    /**
     * Inject mock dependencies into the tested class.
     */
    private final Injector injector = Guice.createInjector(new AbstractModule() {
        @Override
        protected void configure() {
            bind(FileReader.class)
                    .annotatedWith(Names.named("FileReader"))
                    .toInstance(fileReader);
            bind(FileSplitter.class)
                    .annotatedWith(Names.named("FileSplitter"))
                    .toInstance(fileSplitter);
            bind(MarkdownToHtmlConverter.class)
                    .annotatedWith(Names.named("MarkdownToHtmlConverter"))
                    .toInstance(markdownToHtmlConverter);
            bind(FileWriter.class)
                    .annotatedWith(Names.named("FileWriter"))
                    .toInstance(fileWriter);
            bind(HtmlValidator.class)
                    .annotatedWith(Names.named("HtmlValidator"))
                    .toInstance(htmlValidator);

        }
    });

    /**
     * source file name for test.
     */
    static final String sourceFileName = "dada.md";
    /**
     * source file name for test.
     */
    static final String errorSourceFileName = "dada";
    /**
     * target file name for test.
     */
    static final String targetFileName = "toto/";
    /**
     * random markdown content for test.
     */
    static final String randomMarkdownContent = "random markdown content";
    /**
     * random HTML content for test.
     */
    static final String htmlContent = "<h1> Big title </h1>";

    /**
     * random Metadata for test.
     */
    static final String randomMetadata = "dudu";

    /**
     * random Pair for test.
     */
    static Pair<String, Optional<String>> pair = null;

    static {
        try {
            pair = new Pair(htmlContent, randomMetadata);
        } catch (NullArgumentException e) {
            fail(e);
        }
    }

    /**
     * Setup the class before each test.
     */
    @BeforeEach
     void setup() {
        buildPage = new BuildPageImplementation();
        injector.injectMembers(buildPage);
    }

    @Test
    void conversionFinishedAndHtmlFileIsCorrect() {
        try {
            // GIVEN
            when(fileReader.read(sourceFileName)).thenReturn(randomMarkdownContent);
            when(fileSplitter.split(randomMarkdownContent)).thenReturn(pair);
            when(markdownToHtmlConverter.convert(pair.getFirstValue())).thenReturn(htmlContent);
            doNothing().when(fileWriter).write(targetFileName, pair.getFirstValue());

            // WHEN
            this.buildPage.run(sourceFileName, targetFileName);

            // THEN
            verify(fileReader, times(1)).read(sourceFileName);
            verify(fileSplitter, times(1)).split(randomMarkdownContent);
            verify(markdownToHtmlConverter, times(1)).convert(pair.getFirstValue());
            verify(fileWriter, times(1))
                    .write(targetFileName + sourceFileName
                            .replace(".md", ".html"), htmlContent);
            verify(htmlValidator, times(1))
                    .validateHtml(targetFileName + sourceFileName.replace(".md", ".html"));
        } catch (Exception e) {
            fail("An error occur and an exception was raised but it shouldn't be the case"
                    + e.getMessage());
        }
    }

    @Test
    void conversionFinishedAndHtmlFileIsWrong() {
        try {
            // GIVEN
            when(fileReader.read(sourceFileName)).thenReturn(randomMarkdownContent);
            when(fileSplitter.split(randomMarkdownContent)).thenReturn(pair);
            when(markdownToHtmlConverter.convert(pair.getFirstValue())).thenReturn(htmlContent);
            doNothing().when(fileWriter).write(targetFileName, pair.getFirstValue());

            // WHEN
            this.buildPage.run(sourceFileName, targetFileName);

            // THEN
            verify(fileReader, times(1)).read(sourceFileName);
            verify(fileSplitter, times(1)).split(randomMarkdownContent);
            verify(markdownToHtmlConverter, times(1)).convert(pair.getFirstValue());
            verify(fileWriter, times(1))
                    .write(targetFileName + sourceFileName
                            .replace(".md", ".html"), htmlContent);
            verify(htmlValidator, times(1))
                    .validateHtml(targetFileName + sourceFileName
                    .replace(".md", ".html"));
        } catch (Exception e) {
            fail("An error occur and an exception was raised but it shouldn't be the case"
                    + e.getMessage());
        }
    }

    @Test
    void conversionFailedBecauseOfRead() {
        try {
            // GIVEN
            when(fileReader.read(sourceFileName)).thenThrow(new IOException());

            // WHEN
            assertThrows(
                    IOException.class, () -> this.buildPage.run(sourceFileName, targetFileName),
                    "read failed so the function did raise IOException");

            // THEN
            verify(fileReader, times(1)).read(sourceFileName);
            verify(fileSplitter, times(0)).split(randomMarkdownContent);
            verify(markdownToHtmlConverter, times(0)).convert(pair.getFirstValue());
            verify(fileWriter, times(0))
                    .write(targetFileName + sourceFileName
                            .replace(".md", ".html"), htmlContent);
            verify(htmlValidator, times(0))
                    .validateHtml(targetFileName + sourceFileName
                    .replace(".md", ".html"));
        } catch (Exception e) {
            fail("An error occur and an exception was raised but it shouldn't be the case"
                    + e.getMessage());
        }
    }

    @Test
   void conversionFailedBecauseOfWrite() {
        try {
            // GIVEN
            when(fileReader.read(sourceFileName)).thenReturn(randomMarkdownContent);
            when(fileSplitter.split(randomMarkdownContent)).thenReturn(pair);
            when(markdownToHtmlConverter.convert(pair.getFirstValue())).thenReturn(htmlContent);

            // WHEN
            doThrow(new IOException())
                    .when(fileWriter)
                    .write(targetFileName + sourceFileName
                            .replace(".md", ".html"), htmlContent);

            // THEN
            assertThrows(
                    IOException.class,
                    () -> this.buildPage.run(sourceFileName, targetFileName),
                    "write failed so the function did raise IOException");
            verify(fileReader, times(1)).read(sourceFileName);
            verify(fileSplitter, times(1)).split(randomMarkdownContent);
            verify(markdownToHtmlConverter, times(1)).convert(pair.getFirstValue());
            verify(fileWriter, times(1))
                    .write(targetFileName + sourceFileName
                            .replace(".md", ".html"), htmlContent);
            verify(htmlValidator, times(0))
                    .validateHtml(targetFileName + sourceFileName
                            .replace(".md", ".html"));
        } catch (Exception e) {
            fail("An error occur and an exception was raised but it shouldn't be the case"
                    + e.getMessage());
        }
    }

    @Test
    void conversionFailedBecauseOfFileSplitterMetaDataException() {
        try {
            // GIVEN
            when(fileReader.read(sourceFileName)).thenReturn(randomMarkdownContent);

            // WHEN
            doThrow(new MetadataException("ouch"))
                    .when(fileSplitter).split(randomMarkdownContent);

            // THEN
            assertThrows(MetadataException.class,
                    () -> this.buildPage.run(sourceFileName, targetFileName),
                    "fileSplitter failed so the function did raise MetadataException");
            verify(fileReader, times(1)).read(sourceFileName);
            verify(fileSplitter, times(1)).split(randomMarkdownContent);
            verify(markdownToHtmlConverter, times(0)).convert(pair.getFirstValue());
            verify(fileWriter, times(0))
                    .write(targetFileName + sourceFileName
                    .replace(".md", ".html"), htmlContent);
            verify(htmlValidator, times(0))
                    .validateHtml(targetFileName + sourceFileName
                    .replace(".md", ".html"));
        } catch (Exception e) {
            fail("An error occur and an exception was raised but it shouldn't be the case"
                    + e.getMessage());
        }
    }

    @Test
    void conversionFailedBecauseOfFileSplitterNullArgumentException() {
        try {
            // GIVEN
            when(fileReader.read(sourceFileName)).thenReturn(randomMarkdownContent);

            // WHEN
            doThrow(new NullArgumentException("ouch"))
                    .when(fileSplitter).split(randomMarkdownContent);

            // THEN
            assertThrows(NullArgumentException.class,
                    () -> this.buildPage.run(sourceFileName, targetFileName),
                    "fileSplitter failed so the function did raise MetadataException");
            verify(fileReader, times(1)).read(sourceFileName);
            verify(fileSplitter, times(1)).split(randomMarkdownContent);
            verify(markdownToHtmlConverter, times(0)).convert(pair.getFirstValue());
            verify(fileWriter, times(0))
                    .write(targetFileName + sourceFileName
                            .replace(".md", ".html"), htmlContent);
            verify(htmlValidator, times(0))
                    .validateHtml(targetFileName + sourceFileName
                            .replace(".md", ".html"));
        } catch (Exception e) {
            fail("An error occur and an exception was raised but it shouldn't be the case"
                    + e.getMessage());
        }
    }

    @Test
    void conversionFailedBecauseOfSourceFileIsNotMarkdown() {
        try {
            // GIVEN

            // WHEN
            assertThrows(NotMarkdownException.class,
                    () -> this.buildPage.run(errorSourceFileName, targetFileName),
                    "The source File Provided is not a markdown");

            // THEN
            verify(fileReader, times(0)).read(sourceFileName);
            verify(fileSplitter, times(0)).split(randomMarkdownContent);
            verify(markdownToHtmlConverter, times(0)).convert(pair.getFirstValue());
            verify(fileWriter, times(0))
                    .write(targetFileName + sourceFileName
                            .replace(".md", ".html"), htmlContent);
            verify(htmlValidator, times(0))
                    .validateHtml(targetFileName + sourceFileName
                            .replace(".md", ".html"));
        } catch (Exception e) {
            fail("An error occur and an exception was raised but it shouldn't be the case"
                    + e.getMessage());
        }
    }
}
