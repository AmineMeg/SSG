package ssg.buildpage;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ssg.config.SiteStructureVariable;
import ssg.dependencymanager.DependencyManager;
import ssg.exceptions.NotMarkdownException;
import ssg.filereader.FileReader;
import ssg.filesplitter.FileSplitter;
import ssg.filewriter.FileWriter;
import ssg.htmlvalidator.HtmlValidator;
import ssg.markdowntohtmlconverter.MarkdownToHtmlConverter;
import ssg.page.PageDraft;
import ssg.pair.Pair;
import ssg.parsertoml.ParserToml;
import ssg.templatehandler.TemplateHandler;
import ssg.tomlvaluetypewrapper.TomlValueTypeWrapper;

/**
 * Build an HTML page file from a Markdown file.
 */
@SuppressWarnings({"PMD.LawOfDemeter", "PMD.AvoidCatchingGenericException",
    "PMD.GuardLogStatement", "PMD.SignatureDeclareThrowsException",
    "PMD.CyclomaticComplexity"})
@SuppressFBWarnings
public class BuildPageImplementation implements BuildPage {

    /**
     * Log4J Logger.
     */
    private static final Logger logger = LogManager.getLogger();

    /**
     *DependencyManager dependency.
     */
    private final DependencyManager dependencyManager = DependencyManager.getInstance();

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
    @Inject @Named("ParserToml")
    private ParserToml parserToml;

    /**
     * MarkdownToHtmlConverter dependency.
     */
    @Inject @Named("MarkdownToHtmlConverter")
    private MarkdownToHtmlConverter markdownToHtmlConverter;

    /**
     * TemplateHandler dependency.
     */
    @Inject @Named("TemplateHandler")
    private TemplateHandler templateHandler;

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
     * @param outputDirectory target HTML file.
     * @throws Exception when there's an issue with reading, writing or validation.
     */
    @Override
    public void run(String sourceFilePath, String outputDirectory) throws Exception {
        if (!sourceFilePath.endsWith(".md")) {
            logger.error("{} isn't a markdown file", sourceFilePath);
            throw new NotMarkdownException("Trying to convert a file that is not a markdown");
        }
        try {
            convert(sourceFilePath,outputDirectory);
        } catch (Exception e) {
            logger.error("run(): There was an issue during the conversion ", e);
            throw e;
        }
    }

    private void convert(String sourceFilePath, String outputDirectory) throws Exception {

        logger.info("run(): Attempt to convert a markdown file {} to an HTML file {} ",
                sourceFilePath, outputDirectory);
        String sourceRawContent = this.fileReader.read(sourceFilePath);

        logger.info("run(): Attempt to parse split metadata from content for file {}  ",
                sourceFilePath);
        Pair<String, Optional<String>> sourceSplittedContent =
                this.fileSplitter.split(sourceRawContent);


        logger.info("run(): Attempt to parse metadata for file {}  ",
                sourceFilePath);

        Map<String, TomlValueTypeWrapper> metadata = null;

        //  IF METADATA ARE PRESENT THEN WE ARE PARSING THEM
        if (sourceSplittedContent.getSecondValue().isPresent()) {
            logger.info("run(): Parsing metadata for file {}  ",
                    sourceFilePath);
            metadata = parserToml.parse(sourceSplittedContent.getSecondValue().get());
        }

        //IF THE DOCUMENT IS NOT A DRAFT THEN WE COMPUTE IT
        if (isNotDraft(metadata)) {
            resolveDependencies(sourceFilePath, metadata);
            if (dependencyManager.isRecomputeRequired(sourceFilePath)) {
                compute(sourceFilePath, outputDirectory, sourceSplittedContent, metadata);
            } else {
                logger.info("run() : {} nor it's dependency were modified"
                        + " since last build so it won't be recomputed",sourceFilePath);
            }
        } else {
            logger.info("run(): According to metadata {} is a draft and will not be converted ",
                    sourceFilePath);
        }
    }

    private boolean isNotDraft(Map<String, TomlValueTypeWrapper> metadata) {
        return (metadata != null
                && metadata.containsKey("draft")
                && metadata.get("draft").toString().equals("false"))
                || metadata == null
                || !metadata.containsKey("draft");
    }

    private void compute(String sourceFilePath, String outputDirectory,
                        Pair<String, Optional<String>> sourceSplittedContent,
                        Map<String, TomlValueTypeWrapper> metadata) throws Exception {

        File file = new File(sourceFilePath);
        File templatesDir = new File(
                file.getAbsoluteFile().getParentFile().getParent(),
                SiteStructureVariable.TEMPLATES_DIRECTORY);
        String fileName = file.getName();
        

        logger.info("run(): converting file {}  ", sourceFilePath);
        String htmlContent = this.markdownToHtmlConverter
                .convert(sourceSplittedContent.getFirstValue());

        logger.info("run(): creating PageDraft instance for file {}  ", sourceFilePath);
        PageDraft pageDraft = new PageDraft(metadata, htmlContent, fileName.replace(".md", ""));

        logger.info("run(): handling potential template for file {}  ", sourceFilePath);
        String resolvedPage = templateHandler.handle(pageDraft, templatesDir.toString());

        logger.info("run(): writing converted file {}  ", sourceFilePath);
        String fileOutputName = fileName.replace(".md", ".html");

        this.fileWriter.write(outputDirectory + fileOutputName, resolvedPage);

        logger.info("run(): validating html for file {}  ", sourceFilePath);
        this.htmlValidator.validateHtml(outputDirectory + fileOutputName);

        logger.info("run(): conversion is done ");
    }



    private void resolveDependencies(String fileName,
                                     Map<String, TomlValueTypeWrapper> metadata) {

        String templateMetadata = "";

        if (metadata != null && metadata.containsKey(SiteStructureVariable.TEMPLATE)) {

            templateMetadata = metadata.get(SiteStructureVariable.TEMPLATE).toString();
        }

        logger.info("resolveDependencies() : loooking for dependency file at {}",
                dependencyManager.getOutputDirectory()
                        + SiteStructureVariable.DEPENDENCIES_FILE);

        if (!dependencyManager.hasDependency(fileName)) {
            dependencyManager.determineDependencies(fileName, templateMetadata);
        }
    }
}
