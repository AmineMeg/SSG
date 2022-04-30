package ssg.templatehandler;

import java.util.Map;
import ssg.exceptions.TemplateProcesserException;

/**
 * Process a template by replacing motifs by the matching text fragment from context.
 */
public interface TemplateProcesser {
    /**
     * Process an HTML template with context into HTML.
     *
     * @param template source template.
     * @param context map of objects to replace.
     * @param templatesDir directory that holds templates files.
     * @return the processed template.
     * @throws TemplateProcesserException if there is any problem with the processer.
     */
    public abstract String process(
            String template, Map<String, Object> context, String templatesDir)
            throws TemplateProcesserException;
}

