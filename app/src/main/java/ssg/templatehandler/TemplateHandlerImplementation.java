package ssg.templatehandler;

import static ssg.config.SiteStructureVariable.MINIMAL_TEMPLATE;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ssg.exceptions.TemplateHandlerException;
import ssg.exceptions.TemplateProcesserException;
import ssg.filereader.FileReader;
import ssg.page.PageDraft;
import ssg.tomlvaluetypewrapper.TomlValueTypeWrapper;


/**
 * Resolve a page draft into html.
 */
public class TemplateHandlerImplementation implements TemplateHandler {

    /**
     * Log4J Logger.
     */
    private static final Logger logger = LogManager.getLogger();

    /**
     * Keyword to reference page datas within templates.
     */
    private static final String DATA = "metadata";

    /**
     * Keyword to reference page content within templates.
     */
    private static final String CONTENT = "content";

    /**
     * Keyword to reference a template in a page datas.
     */
    private static final String TEMPLATE_KEY = "template";

    /**
     * Default template.
     * If exists, DEFAULT_TEMPLATE will be used if the page doesn't specify a template. 
     */
    private static final String DEFAULT_TEMPLATE = "default.html";

    /**
     * TemplateProcesser process templates to resolved HTML pages.
     */
    @Inject @Named("TemplateProcesser")
    private TemplateProcesser processer;

    /**
     * FileReader reads templates from disk.
     */
    @Inject @Named("FileReader")
    private FileReader reader;

    @SuppressFBWarnings // potential path traversal are taken care of.
    @SuppressWarnings("PMD.LawOfDemeter")
    private String handleTemplate(String template, Map<String, Object> context, String templatesDir)
            throws TemplateHandlerException {

        File workingDirectory = new File(templatesDir);
        File file = new File(workingDirectory, template);
        String filePath = file.toString();

        try {
            // path traversal prevention
            if (file.getCanonicalPath().startsWith(workingDirectory.getCanonicalPath())) {
                logger.info("handleTemplate(): reading {}", filePath);
                String templateContent = reader.read(filePath);
                logger.info("handleTemplate(): processing template content");
                return processer.process(templateContent, context, templatesDir);
            } else {
                logger.error("handleTemplate(): attempt to process a file above working directory");
                throw new TemplateHandlerException(
                    "Cannot handle %s : file is above template directory"
                    .formatted(file.toString()));
            }
        } catch (TemplateProcesserException e) {
            logger.error("template processing raised an exception");
            throw new TemplateHandlerException(e);
        } catch (IOException e) {
            logger.error("attempt to read template {} raised an exception", template);
            throw new TemplateHandlerException(e);
        }
    }

    /**
     * Create a template context.
     *
     * @param data Optional page data.
     * @param content Page content.
     * @return context holding content and datas if any.
     */
    private Map<String, Object> makeContext(
        Optional<Map<String, TomlValueTypeWrapper>> data,
        String content) {

        Map<String, Object> context = new HashMap<>();
        context.put(CONTENT, content);

        data.ifPresent(stringTomlValueTypeWrapperMap
                -> context.put(DATA, stringTomlValueTypeWrapperMap));

        return context;
    }

    @SuppressFBWarnings // false positive for potential path traversal vulnerability
    @SuppressWarnings({"PMD.LawOfDemeter", "PMD.GuardLogStatement"})
    @Override
    public String handle(PageDraft draft, String templatesDir) throws TemplateHandlerException {
        String title = draft.getTitle();
        Optional<Map<String, TomlValueTypeWrapper>> datas = draft.getData();

        if (datas.isPresent() && datas.get().containsKey(TEMPLATE_KEY)) {
            String template = datas.get().get(TEMPLATE_KEY).toString();
            logger.info("handling {} and found template {} in datas", title, template);
            return handleTemplate(
                template,
                makeContext(datas, draft.getContent()),
                templatesDir);
        }

        File defaultTemplate = new File(templatesDir, DEFAULT_TEMPLATE);

        logger.info("looking for default template at {} ", defaultTemplate.toString());
        if (defaultTemplate.isFile()) {
            return handleTemplate(
                    DEFAULT_TEMPLATE,
                    makeContext(datas, draft.getContent()),
                    templatesDir);
        }
        logger.info("handling {} without template", title);
        return MINIMAL_TEMPLATE.formatted(title, draft.getContent());
    }
} 
