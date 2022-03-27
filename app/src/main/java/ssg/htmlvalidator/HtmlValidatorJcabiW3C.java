package ssg.htmlvalidator;

import com.jcabi.w3c.ValidationResponse;
import com.jcabi.w3c.ValidatorBuilder;
import java.nio.file.Files;
import java.nio.file.Path;
import ssg.Generated;

/**
 * /!\ NOT WORKING: Class which implement HtmlValidator using JcabiW3C validator HTML Checker.
 */
@Generated
public class HtmlValidatorJcabiW3C implements HtmlValidator {

    /**
     * Verifies that an HTML content is valid.
     *
     * @param htmlFileName the HTML content.
     * @throws Exception when there is any problem.
     */
    @Override
    public void validateHtml(String htmlFileName) throws Exception {
        Path toCheck = Path.of(htmlFileName);
        String actual = Files.readString(toCheck);
        ValidationResponse response =
                new ValidatorBuilder().html().validate(actual);
        assert response.valid();
    }
}
