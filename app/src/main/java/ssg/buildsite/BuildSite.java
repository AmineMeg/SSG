package ssg.buildsite;

/**
 * Interface Build Site.
 */
@SuppressWarnings({"PMD.LawOfDemeter", "PMD.AvoidCatchingGenericException",
    "PMD.GuardLogStatement", "PMD.SignatureDeclareThrowsException"})
public interface BuildSite {

    /**
    * Create the entire website from a directory.
    */
    void createWebSite(String pageSrcDirectory, String pageDstDirectory) throws Exception;

}