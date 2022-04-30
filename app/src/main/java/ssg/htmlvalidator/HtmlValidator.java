package ssg.htmlvalidator;

/**
 * Interface which provide a html validator.
 */
@SuppressWarnings({"PMD.LawOfDemeter", "PMD.AvoidCatchingGenericException",
    "PMD.GuardLogStatement", "PMD.SignatureDeclareThrowsException"})
public interface HtmlValidator {

    /**
     * Verifies that an HTML content is valid.
     *
     * @param htmlFileName the HTML content.
     * @throws Exception when there is any problem.
     */
    void validateHtml(String htmlFileName) throws Exception;
}
