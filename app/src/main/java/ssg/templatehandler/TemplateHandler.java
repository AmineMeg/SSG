package ssg.templatehandler;

import ssg.exceptions.TemplateHandlerException;
import ssg.page.PageDraft;

/**
 * Resolve a page draft into html.
 */
public interface TemplateHandler {

    /**
     * Resolve the page draft into HTML content.
     *
     * @param draft draft to handle.
     * @param templatesDir directory that holds templates files.
     * @return draft HTML content.
     * @throws TemplateHandlerException if an error happend while resolving the page.
     */
    String handle(PageDraft draft, String templatesDir) throws TemplateHandlerException;
}
