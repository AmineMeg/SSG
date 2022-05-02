package ssg.htmlvalidator;

import java.nio.file.Files;
import java.nio.file.Path;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * HtmlValidator implementation using the W3C API.
 */
@SuppressWarnings("PMD.LawOfDemeter")
public class HtmlValidatorW3Capi implements HtmlValidator {

    /**
     * Log4J Logger.
     */
    private static final Logger logger = LogManager.getLogger(HtmlValidatorW3Capi.class);

    /**
     * Verifies that an HTML content is valid.
     *
     * @param htmlFileName the HTML content.
     * @throws Exception when there is any problem.
     */
    @Override
    public void validateHtml(String htmlFileName) throws Exception {
        Path fileToCheck = Path.of(htmlFileName);
        String source = Files.readString(fileToCheck);
        HttpResponse<String> uniResponse = Unirest.post("https://validator.w3.org/nu/?out=gnu")
                .header("User-Agent",
                        "Mozilla/5.0 (X11; Linux x86_64) "
                                +
                                "AppleWebKit/537.36 (KHTML, like Gecko) "
                                +
                                "Chrome/41.0.2272.101 Safari/537.36")
                .header("Content-Type", "text/html; charset=UTF-8")
                .queryString("out", "gnu")
                .body(source)
                .asString();
        String response = uniResponse.getBody();
        if (!"".equals(response)) {
            logger.info(response);
        }
    }
}
