package ssg.filesplitter;

import java.util.Optional;
import ssg.exceptions.MetadataException;
import ssg.exceptions.NullArgumentException;
import ssg.pair.Pair;

/**
 * FileSplitter Interface.
 */
public interface FileSplitter {

    /**
     * Split the content and the metadata into a Pair.
     *
     * @param buffer text of the parsed file .md.
     * @return a Pair containing the content and the metadata separately.
     * @throws MetadataException if the metadata is not properly declared.
     * @throws NullArgumentException Pair has a null argument.
     */
    Pair<String, Optional<String>> split(String buffer)
            throws MetadataException, NullArgumentException;
}
