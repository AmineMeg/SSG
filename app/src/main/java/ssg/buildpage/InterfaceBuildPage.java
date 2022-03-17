package ssg.buildpage;

/**
 * Build an HTML page file from a Markdown file.
 */
public interface InterfaceBuildPage {
    /**
     * Build an HTML file from a Markdown source file.
     *
     * @param sourceFileName source Markdown file.
     * @param targetFileName target HTML file
     * @throws Exception when there's an issue with reading, writing or validation.
     */
    boolean run(String sourceFileName, String targetFileName) throws Exception;
}
