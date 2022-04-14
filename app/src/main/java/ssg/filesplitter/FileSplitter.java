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
     * Returns a pair (content, metadata).
     */
    Pair<String, Optional<String>> split(String buffer)
            throws MetadataException, NullArgumentException;
}
