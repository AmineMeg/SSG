package ssg.filesplitter;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import ssg.exceptions.MetadataException;
import ssg.exceptions.NullArgumentException;

/**
 * FileSplitterImplementation class test.
 */
class FileSplitterImplementationTest {

    @Test
    void testPresentMetadataSplit() {
        FileSplitterImplementation fileSplitter = new FileSplitterImplementation();
        final String newline = System.getProperty("line.separator");

        String buffer = """
                +++
                title = "Souvenirs et aventures de ma vie"
                date = "1903-02-13"
                author = "Louise Michel"
                +++

                Lorem *ipsum* dolor sit amet.""";

        String expectedContent = newline
                + "Lorem *ipsum* dolor sit amet."
                + newline;

        String expectedMetadata = "title = \"Souvenirs et aventures de ma vie\""
                + newline
                + "date = \"1903-02-13\""
                + newline
                + "author = \"Louise Michel\""
                + newline;

        try {
            Pair<String, Optional<String>> expected =
                    new Pair<>(expectedContent, Optional.of(expectedMetadata));
            assertTrue(expected.isEqual(fileSplitter.split(buffer)),
                    "split: The metadata should be present and correctly encapsulated");

        } catch (MetadataException e) {
            fail("split: syntax error for the metadata: " + e.getMessage());
        } catch (NullArgumentException e) {
            fail("split: there is at least one null argument in one or both of the pairs: "
                    + e.getMessage());
        }
    }

    @Test
    void testMetadataWithStringAmbiguousSplit() {
        FileSplitterImplementation fileSplitter = new FileSplitterImplementation();
        final String newline = System.getProperty("line.separator");

        String buffer = """
                +++
                title = "Souvenirs et aventures de ma vie"
                date = "1903-02-13"
                author = "Louise Michel"
                encapsulation = "+++"
                ambigous = "
                +++
                "
                +++

                Lorem *ipsum* dolor sit amet.""";

        String expectedContent = newline
                + "Lorem *ipsum* dolor sit amet."
                + newline;

        String expectedMetadata = "title = \"Souvenirs et aventures de ma vie\""
                + newline
                + "date = \"1903-02-13\""
                + newline
                + "author = \"Louise Michel\""
                + newline
                + "encapsulation = \"+++\""
                + newline
                + "ambigous = \""
                + newline
                + "+++"
                + newline
                + "\""
                + newline;

        try {
            Pair<String, Optional<String>> expected =
                    new Pair<>(expectedContent, Optional.of(expectedMetadata));
            assertTrue(expected.isEqual(fileSplitter.split(buffer)),
                    "split: The metadata should be present and correctly encapsulated");

        } catch (MetadataException e) {
            fail("split: syntax error for the metadata: " + e.getMessage());
        } catch (NullArgumentException e) {
            fail("split: there is at least one null argument in one or both of the pairs: "
                    + e.getMessage());
        }
    }

    @Test
    void testMetadataWithStringQuoteAmbiguousSplit() {
        FileSplitterImplementation fileSplitter = new FileSplitterImplementation();
        final String newline = System.getProperty("line.separator");

        String buffer = """
                +++
                title = "Souvenirs et aventures de ma vie"
                date = "1903-02-13"
                author = "Louise Michel"
                ambigous = "
                \\\"
                "
                +++

                Lorem *ipsum* dolor sit amet.""";

        String expectedContent = newline
                + "Lorem *ipsum* dolor sit amet."
                + newline;

        String expectedMetadata = "title = \"Souvenirs et aventures de ma vie\""
                + newline
                + "date = \"1903-02-13\""
                + newline
                + "author = \"Louise Michel\""
                + newline
                + "ambigous = \""
                + newline
                + "\\\""
                + newline
                + "\""
                + newline;

        try {
            Pair<String, Optional<String>> expected =
                    new Pair<>(expectedContent, Optional.of(expectedMetadata));
            assertTrue(expected.isEqual(fileSplitter.split(buffer)),
                    "split: The metadata should be present and correctly encapsulated");

        } catch (MetadataException e) {
            fail("split: syntax error for the metadata: " + e.getMessage());
        } catch (NullArgumentException e) {
            fail("split: there is at least one null argument in one or both of the pairs: "
                    + e.getMessage());
        }
    }

    @Test
    void testNoMetadataSplit() {
        FileSplitterImplementation fileSplitter = new FileSplitterImplementation();

        String buffer = """

                Lorem *ipsum* dolor sit amet.""";

        String expectedContent = System.getProperty("line.separator")
                + "Lorem *ipsum* dolor sit amet.";

        try {
            Pair<String, Optional<String>> expected =
                    new Pair<>(expectedContent, Optional.empty());
            assertTrue(expected.isEqual(fileSplitter.split(buffer)),
                    "split: metadonnées doit être absentes");
        } catch (MetadataException e) {
            fail("split: syntax error: " + e.getMessage());
        } catch (NullArgumentException e) {
            fail("split: there is at least one null argument in one or both of the pairs: "
                    + e.getMessage());
        }
    }

    @Test
    void testEmptyFileSplit() {
        FileSplitterImplementation fileSplitter = new FileSplitterImplementation();

        try {
            Pair<String, Optional<String>> expected = new Pair<>("", Optional.empty());
            assertTrue(expected.isEqual(fileSplitter.split("")), "split: fichier doit être vide");
        } catch (MetadataException e) {
            fail("split: syntaxe incorrecte: " + e.getMessage());

        } catch (NullArgumentException e) {
            fail("split: there is at least one null argument in one or both of the pairs: "
                    + e.getMessage());
        }
    }

    @Test
    void testEncapsulationMetadataSplit() {

        assertThrows(MetadataException.class,
                () -> new FileSplitterImplementation().split("""
                +++
                title = "Souvenirs et aventures de ma vie"
                date = "1903-02-13"
                author = "Louise Michel"

                Lorem *ipsum* dolor sit amet."""),
                "split: metadonnées doit être mal encadrées");
    }

    @Test
    void testBadDeclarationMetadataSplit() {

        assertThrows(MetadataException.class,
                () -> new FileSplitterImplementation().split("""
                Lorem *ipsum* dolor sit amet.
                
                +++
                title = "Souvenirs et aventures de ma vie"
                date = "1903-02-13"
                author = "Louise Michel"
                +++"""),
                "split: metadonnées doit être mal declarées");
    }

    @Test
    void testMultipleDeclarationMetadataSplit() {

        assertThrows(MetadataException.class,
                () -> new FileSplitterImplementation().split("""
                +++
                title = "Souvenirs et aventures de ma vie"
                date = "1903-02-13"
                author = "Louise Michel"
                +++

                Lorem *ipsum* dolor sit amet.
                                
                +++
                title = "Welcome to Mini-Templates"
                date = 2021-01-13
                +++"""),
                "split: metadonnées doit être declarées multiple fois");
    }

    @Test
    void testSpaceBeginningDeclarationMetadataSplit() {

        assertThrows(MetadataException.class,
                () -> new FileSplitterImplementation().split("""
                      +++  
                title = "Souvenirs et aventures de ma vie"
                date = "1903-02-13"
                author = "Louise Michel"
                +++

                Lorem *ipsum* dolor sit amet"""),
                "split: metadonnées doit être declarées multiple fois");
    }

    @Test
    void testSpaceEndDeclarationMetadataSplit() {

        assertThrows(MetadataException.class,
                () -> new FileSplitterImplementation().split("""
                +++
                title = "Souvenirs et aventures de ma vie"
                date = "1903-02-13"
                author = "Louise Michel"
                       +++  

                Lorem *ipsum* dolor sit amet"""),
                "split: metadonnées doit être declarées multiple fois");
    }
}
