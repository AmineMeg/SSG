package ssg.buildpage;

/**
 * Build an HTML page file from a Markdown file.
 */
@SuppressWarnings({"PMD.LawOfDemeter", "PMD.AvoidCatchingGenericException",
    "PMD.GuardLogStatement", "PMD.SignatureDeclareThrowsException"})
public interface BuildPage {
    /**
     * Build an HTML file from a Markdown source file.
     *
     * @param sourceFileName source Markdown file.
     * @param targetFileName target HTML file.
     * @throws Exception when there's an issue with reading, writing or validation.
     */
    void run(String sourceFileName, String targetFileName) throws Exception;
}
