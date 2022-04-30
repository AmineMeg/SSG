package ssg.filesplitter;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ssg.exceptions.MetadataException;
import ssg.exceptions.NullArgumentException;
import ssg.pair.Pair;


/**
 * FileSplitterImplementation class.
 */
@SuppressWarnings("PMD.LawOfDemeter")
public class FileSplitterImplementation implements FileSplitter {

    /**
     * Logger.
     */
    private static final Logger logger = LogManager.getLogger();

    /**
     * Encapsulation for metadata.
     */
    private static final String ENCAPSULATION = "+++";

    /**
     * Opening quote.
     */
    private static final String REGEX_OPENING_QUOTE = "(.*)\"(.*)";

    /**
     * To eliminate the first quote occurrence.
     */
    private static final String REGEX_ELIMINATE_OPENING_QUOTE = "^[^\"]*\"(.*)$";

    /**
     * Closing quote, ignoring \" inside string.
     */
    private static final String REGEX_CLOSING_QUOTE = "((.*)[^\\\\]\"(.*))|(\"(.*))";

    /**
     * Split the content and the metadata into a Pair.
     *
     * @param buffer text of the parsed file .md.
     * @return a Pair containing the content and the metadata separately.
     * @throws MetadataException if the metadata is not properly declared.
     * @throws NullArgumentException Pair has a null argument.
     */
    @Override
    public Pair<String, Optional<String>> split(String buffer)
            throws MetadataException, NullArgumentException {

        String[] lines = buffer.split("\\r?\\n");

        try {

            // check for metadata delimiter on first line
            // please note that lines.length is > 0 as result of a split
            if (ENCAPSULATION.equals(lines[0].strip())) {
                errorDeclaration(lines[0], 1);
                return metadataPresent(lines);
            }
            // buffer doesn't start with the metadata delimiter
            // check that there is no metadata delimiter inside

            for (int i = 1; i < lines.length; i++) {
                if (ENCAPSULATION.equals(lines[i].strip())) {
                    String error = String.format("Cannot declare metadata here: line %d", i + 1);
                    throw new MetadataException(error);
                }
            }
            // buffer is clean and can be returned
            return new Pair<>(buffer,Optional.empty());

        } catch (MetadataException e) {
            String message = e.getMessage();
            logger.error(message);
            throw e;
        } catch (NullArgumentException e) {
            String message = "Pair has a null argument (Should not happen, investigate ASAP) :"
                    + e.getMessage();
            logger.error(message);
            throw e;
        }
    }

    /**
     * Part of split() function. Case when metadata is present.
     *
     * @param lines text lines separated in an array.
     * @return a Pair containing the content and the metadata separately.
     * @throws MetadataException if the metadata is not properly declared.
     * @throws NullArgumentException ignoreString must not be null.
     */
    private Pair<String, Optional<String>> metadataPresent(String... lines)
            throws MetadataException, NullArgumentException {

        final StringBuilder content = new StringBuilder();
        StringBuilder metadata = new StringBuilder();

        // Metadata
        int i;
        for (i = 1; i < lines.length; i++) {
            if (ENCAPSULATION.equals(lines[i].strip())) {
                errorDeclaration(lines[i], i + 1);
                break;
            } else if (Pattern.matches(REGEX_OPENING_QUOTE, lines[i])) {
                Pair<Integer, StringBuilder> res = ignoreString(metadata, lines, i);
                i = res.getFirstValue();
                metadata = res.getSecondValue();
            } else if (i == lines.length - 1) {
                throw new MetadataException(String.format(
                        "The metadata was not properly encapsulated: line %d", (i + 1)));
            }
            metadata.append(lines[i]).append(System.getProperty("line.separator"));
        }

        // Content
        for (int j = i + 1; j < lines.length; j++) {
            if (ENCAPSULATION.equals(lines[j].strip())) {
                throw new MetadataException(String.format(
                        "Cannot declare multiple times metadata: line %d", (j + 1)));
            }
            content.append(lines[j]).append(System.getProperty("line.separator"));
        }

        return new Pair<>(content.toString(),Optional.of(metadata.toString()));
    }

    /**
     * Ignores everything in a string until a closing quote is detected.
     *
     * @param metadata the metadata string that is built.
     * @param lines the lines in the file.
     * @param index the index where the opening quote was detected.
     * @return the line where to continue and the metadata string.
     */
    private Pair<Integer, StringBuilder>
        ignoreString(StringBuilder metadata, String[] lines, int index)
            throws NullArgumentException {

        // Eliminate the first quote we found on the line
        Pattern pattern = Pattern.compile(REGEX_ELIMINATE_OPENING_QUOTE);
        Matcher matcher = pattern.matcher(lines[index]);
        String line = "";
        if (matcher.matches()) {
            line = matcher.group(1);
        }

        // If closing quote is on the same line
        if (Pattern.matches(REGEX_CLOSING_QUOTE, line)) {
            return new Pair<>(index, metadata);
        }
        metadata.append(lines[index]).append(System.getProperty("line.separator"));

        // Else we look on the next lines is there is a closing quote
        for (int i = index + 1; i < lines.length - 1; i++) {
            if (Pattern.matches(REGEX_CLOSING_QUOTE, lines[i])) {
                return new Pair<>(i, metadata);
            }
            metadata.append(lines[i]).append(System.getProperty("line.separator"));
        }

        return new Pair<>(lines.length - 2, metadata);
    }

    /**
     * Throws an error when metadata declaration is ambiguous.
     *
     * @param s text to be compared with ENCAPSULATION.
     * @param line the line number of s.
     * @throws MetadataException if s does not equal ENCAPSULATION.
     */
    private void errorDeclaration(String s,int line) throws MetadataException {
        if (!ENCAPSULATION.equals(s)) {
            throw new MetadataException(String.format(
                    "Declaration of metadata \"+++\" is ambiguous: line %d", line));
        }
    }
}