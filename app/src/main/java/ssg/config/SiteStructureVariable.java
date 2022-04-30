package ssg.config;

import java.time.ZoneId;
import java.time.ZoneOffset;
import ssg.Generated;

/**
 * Website minimal structure.
 */
@Generated
public class SiteStructureVariable {

    /**
     * TOML index File.
     */
    public static final String SITE_TOML = "site.toml";

    /**
     * Directory with markdown File.
     */
    public static final String CONTENTS = "content/";

    /**
     * Directory with markdown File.
     */
    public static final String RAW_CONTENTS = "content";

    /**
     * markdown file inside contents/.
     */
    public static final String INDEX_MD = "index.md";

    /**
     * static directory.
     */
    public static final String STATIC = "static/";

    /**
     * static directory.
     */
    public static final String RAW_STATIC = "static";

    /**
     * dependency file.
     */
    public static final String DEPENDENCIES_FILE = "dependencies.toml";

    /**
     * Minimal template.
     */
    public static final String MINIMAL_TEMPLATE = """
        <!DOCTYPE html>
        <html lang="en">
          <head>
            <title>%s</title>
          </head>
          <body>
              %s
          </body>
        </html>
        """;

    /**
     * default zoneOffset Id.
     */
    public static final ZoneId zoneOffsetId = ZoneOffset.systemDefault();

    /**
     * String key template.
     */
    public static final String TEMPLATE = "template";

    /**
     * Directory that holds templates.
     */
    public static final String TEMPLATES_DIRECTORY = "templates/";
}
