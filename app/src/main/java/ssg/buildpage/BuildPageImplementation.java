package ssg.buildpage;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.File;
import java.nio.file.Path;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ssg.exceptions.NotMarkdownException;
import ssg.filereader.FileReader;
import ssg.filesplitter.FileSplitter;
import ssg.filesplitter.Pair;
import ssg.filewriter.FileWriter;
import ssg.htmlvalidator.HtmlValidator;
import ssg.markdowntohtmlconverter.MarkdownToHtmlConverter;

/**
 * Build an HTML page file from a Markdown file.
 */
@SuppressWarnings({"PMD.LawOfDemeter", "PMD.AvoidCatchingGenericException",
    "PMD.GuardLogStatement", "PMD.SignatureDeclareThrowsException"})
@SuppressFBWarnings
public class BuildPageImplementation implements BuildPage {

    /**
     * Log4J Logger.
     */
    private static final Logger logger = LogManager.getLogger();

    /**
     * FileReader dependency.
     */
    @Inject @Named("FileReader")
    private FileReader fileReader;

    /**
     * FileSplitter dependency.
     */
    @Inject @Named("FileSplitter")
    private FileSplitter fileSplitter;

    /**
     * ParserTOML dependency.
     */
    /* TODO: @Inject @Named("ParserTOML")
    private ParserTOML parserTOML;*/

    /**
     * MarkdownToHtmlConverter dependency.
     */
    @Inject @Named("MarkdownToHtmlConverter")
    private MarkdownToHtmlConverter markdownToHtmlConverter;

    /**
     * TemplateHandler dependency.
     */
    /* TODO: @Inject @Named("TemplateHandler")
    private TemplateHandler templateHandler;*/

    /**
     * FileWriter dependency.
     */
    @Inject @Named("FileWriter")
    private FileWriter fileWriter;


    /**
     * HtmlValidator dependency.
     */
    @Inject @Named("HtmlValidator")
    private HtmlValidator htmlValidator;

    /**
     * Build an HTML file from a Markdown source file.
     *
     * @param sourceFilePath source Markdown file.
     * @param outputDirectory target HTML file
     * @throws Exception when there's an issue with reading, writing or validation.
     */
    @Override
    public void run(String sourceFilePath, String outputDirectory) throws Exception {
        if (!sourceFilePath.endsWith(".md")) {
            logger.error("{} isn't a markdown file", sourceFilePath);
            throw new NotMarkdownException("Trying to convert a file that is not a markdown");
        }
        try {
            logger.info("run(): Attempt to convert a markdown file {} to an HTML file {} ",
                    sourceFilePath, outputDirectory);
            String sourceRawContent = this.fileReader.read(sourceFilePath);
            Pair<String, Optional<String>> sourceSplittedContent =
                    this.fileSplitter.split(sourceRawContent);
            String markdownToHtmlConverted = this.markdownToHtmlConverter
                    .convert(sourceSplittedContent.getFirstValue());
            File fileWrite = new File(sourceFilePath);
            String fileOutputName =  Path.of(fileWrite.getName())
                    .toString()
                    .replace(".md", ".html");
            this.fileWriter.write(outputDirectory
                    +
                    fileOutputName, markdownToHtmlConverted);
            this.htmlValidator.validateHtml(outputDirectory + fileOutputName);
            logger.info("run(): Conversion is done ");
        } catch (Exception e) {
            logger.error("run(): There was an issue during the conversion ", e);
            throw e;
        }
    }
}
