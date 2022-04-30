package ssg.buildsite;

/**
 * Interface Build Site.
 */
@SuppressWarnings({"PMD.LawOfDemeter", "PMD.AvoidCatchingGenericException",
    "PMD.GuardLogStatement", "PMD.SignatureDeclareThrowsException"})
public interface BuildSite {

    /**
     * Create the entire website from a directory.
     *
     * @param pageSrcDirectory source directory.
     * @param pageDstDirectory destination directory.
     * @throws Exception when the source directory is not correct,
     *      absence of the site.toml or index.md files
     *      or if something getting wrong during the creation of the website.
     */
    void createWebSite(String pageSrcDirectory, String pageDstDirectory) throws Exception;

    /**
     * Sets number of jobs.
     *
     * @param jobs value to set
     */
    void setJobs(int jobs);
}