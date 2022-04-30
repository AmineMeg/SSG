package ssg.htmlvalidator;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.File;
import nu.validator.messages.MessageEmitterAdapter;
import nu.validator.validation.SimpleDocumentValidator;
import nu.validator.xml.SystemErrErrorHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ssg.Generated;

/**
 * /!\ NOT WORKING: Class which implement HtmlValidator using v.Nu validator HTML Checker.
 */
@Generated
@SuppressWarnings({"PMD.LawOfDemeter", "PMD.AvoidCatchingGenericException",
    "PMD.GuardLogStatement", "PMD.SignatureDeclareThrowsException"})
public class HtmlValidatorNu implements HtmlValidator {
    /**
     * log4J.
     */
    private static final Logger logger = LogManager.getLogger();

    /**
     * Verifies that an HTML content is valid.
     *
     * @param htmlFileName the HTML content.
     * @throws Exception when there is any problem.
     */
    @Override
    @SuppressFBWarnings //TODO: Check later if we can pass argument without user input.
    public void validateHtml(String htmlFileName) throws Exception {

        MessageEmitterAdapter errorHandler = new MessageEmitterAdapter();
        errorHandler.setErrorsOnly(true);

        SimpleDocumentValidator validator = new SimpleDocumentValidator();
        validator.setUpMainSchema("https://validator.w3.org/nu/", new SystemErrErrorHandler());
        validator.setUpValidatorAndParsers(errorHandler,true,false);
        validator.checkHtmlFile(new File(htmlFileName),true);
        if (errorHandler.getErrors() > 0) {
            logger.info("validateHtml(): There was {} errors in the html file",
                    errorHandler.getErrors()
            );
        }
    }
}
