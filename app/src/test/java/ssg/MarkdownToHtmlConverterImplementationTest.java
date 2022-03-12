package ssg;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Junit test class for MarkdownToHtmlConverterImplementation.
 */
@SuppressWarnings("PMD.TooManyMethods")
class MarkdownToHtmlConverterImplementationTest {

    /**
     * Converter instance that is used for testing.
     */
    private static final MarkdownToHtmlConverter converter =
        new MarkdownToHtmlConverterImplementation();

    /**
     * Error message for wrong conversions.
     */
    static final String wrongConversionMsg = "md2html conversion do not match the expected output";

    @Test
    void emptyContent() {
        assertEquals("", converter.convert(""), wrongConversionMsg);
    }

    @Test
    void paragraphWithItalicContent1() {
        String input = "Fourier accompanied *Napoleon Bonaparte* in Egypt";
        String expected = "<p>Fourier accompanied <em>Napoleon Bonaparte</em> in Egypt</p>\n";
        assertEquals(expected, converter.convert(input), wrongConversionMsg);
    }

    @Test
    void paragraphWithItalicContent2() {
        String input = "The _Principa Mathematica_ is a three volume work";
        String expected = "<p>The <em>Principa Mathematica</em> is a three volume work</p>\n";
        assertEquals(expected, converter.convert(input), wrongConversionMsg);
    }

    @Test
    void paragraphWithBoldContent1() {
        String input = "Borel was born in **Saint-Affrique**, Aveyron";
        String expected = "<p>Borel was born in <strong>Saint-Affrique</strong>, Aveyron</p>\n";
        assertEquals(expected, converter.convert(input), wrongConversionMsg);
    }

    @Test
    void paragraphWithBoldContent2() {
        String input = """
            No one shall expel us from the paradise that __Cantor__ has created
            """;
        String expected = """
            <p>No one shall expel us from the paradise that <strong>Cantor</strong> has created</p>
            """;
        assertEquals(expected, converter.convert(input), wrongConversionMsg);
    }

    @Test
    void heading1() {
        String input = "# Riemann integral";
        String expected = "<h1>Riemann integral</h1>\n";
        assertEquals(expected, converter.convert(input), wrongConversionMsg);
    }

    @Test
    void heading2() {
        String input = "## Poincaré conjecture";
        String expected = "<h2>Poincaré conjecture</h2>\n";
        assertEquals(expected, converter.convert(input), wrongConversionMsg);
    }

    @Test
    void headingWithParagraph() {
        String input = """
            ## Russell's Paradox
            Is the set of all sets that are not members of themselves a member of itself ?
            """;
        String expected = """
            <h2>Russell's Paradox</h2>
            <p>Is the set of all sets that are not members of themselves a member of itself ?</p>
            """;
        assertEquals(expected, converter.convert(input), wrongConversionMsg);
    }

    @Test
    void list1() {
        String input = """
            * Variable
            * Abstraction
            * Application
            """;
        String expected = """
            <ul>
            <li>Variable</li>
            <li>Abstraction</li>
            <li>Application</li>
            </ul>
            """;
        assertEquals(expected, converter.convert(input), wrongConversionMsg);
    }

    @Test
    void list2() {
        String input = """
            - Alpha-conversion
            - Beta-reduction
            """;
        String expected = """
            <ul>
            <li>Alpha-conversion</li>
            <li>Beta-reduction</li>
            </ul>
            """;
        assertEquals(expected, converter.convert(input), wrongConversionMsg);
    }

    @Test
    void paragraphWithLink1() {
        String input = """
            Lambda calculus was introduced by \
            [Alonzo Church](https://en.wikipedia.org/wiki/Alonzo_Church) \
            in the 1930s
            """;
        String expected = """
            <p>Lambda calculus was introduced by \
            <a href="https://en.wikipedia.org/wiki/Alonzo_Church">Alonzo Church</a> \
            in the 1930s</p>
            """;
        assertEquals(expected, converter.convert(input), wrongConversionMsg);
    }

    @Test
    void paragraphWithLink2() {
        String input = """
            Lebesgue shows that his conditions lead to the [theory of measure][1]

            [1]: https://en.wikipedia.org/wiki/Measure_(mathematics)
            """;
        String expected = """
            <p>Lebesgue shows that his conditions lead to the \
            <a href="https://en.wikipedia.org/wiki/Measure_(mathematics)">theory of measure</a>\
            </p>
            """;
        assertEquals(expected, converter.convert(input), wrongConversionMsg);
    }

    @Test
    void image1() {
        String input = """
            ![Point selle](https://www.bibmath.net/dico/p/images/pointcol.jpg)
            """;
        String expected = """
            <p>\
            <img src="https://www.bibmath.net/dico/p/images/pointcol.jpg" alt="Point selle" />\
            </p>
            """;
        assertEquals(expected, converter.convert(input), wrongConversionMsg);
    }

    @Test
    void image2() {
        String input = """
            # Loi normale
            ## Densite de probabilite
            ![pdf][1]
            ## Fonction de repartition
            ![cdf][2]
            
            [1]: https://probas.fr/DENSITE_NORMALE.png
            [2]: https://probas.fr/FONCTION_REPARTITION_NORMALE.jpg
            """;
        String expected = """
            <h1>Loi normale</h1>
            <h2>Densite de probabilite</h2>
            <p><img src="https://probas.fr/DENSITE_NORMALE.png" alt="pdf" /></p>
            <h2>Fonction de repartition</h2>
            <p><img src="https://probas.fr/FONCTION_REPARTITION_NORMALE.jpg" alt="cdf" /></p>
            """;
        assertEquals(expected, converter.convert(input), wrongConversionMsg);
    }

    @Test
    void quote() {
        String input = """
            > Les charmes enchanteurs de cette sublime science ne se decelent dans toute leur \
            beaute qu'a ceux qui ont le courage de l'approfondir.
            """;
        String expected = """
            <blockquote>
            <p>Les charmes enchanteurs de cette sublime science ne se decelent dans toute leur \
            beaute qu'a ceux qui ont le courage de l'approfondir.</p>
            </blockquote>
            """;
        assertEquals(expected, converter.convert(input), wrongConversionMsg);
    }

    @Test
    void horizontalRule1() {
        String input = "---";
        String expected = "<hr />\n";
        assertEquals(expected, converter.convert(input), wrongConversionMsg);
    }

    @Test
    void horizontalRule2() {
        String input = "***";
        String expected = "<hr />\n";
        assertEquals(expected, converter.convert(input), wrongConversionMsg);
    }

    @Test
    void inlineCode() {
        String input = "`(fun x -> x)` la fonction identite";
        String expected = "<p><code>(fun x -&gt; x)</code> la fonction identite</p>\n";
        assertEquals(expected, converter.convert(input), wrongConversionMsg);
    }

    @Test
    void codeBlock1() {
        String input = """
            ```
            let rec pgcd a b =
                if b = 0 then a else
                pgcd b (a mod b)
            ```
            """;
        String expected = """
            <pre><code>\
            let rec pgcd a b =
                if b = 0 then a else
                pgcd b (a mod b)
            </code></pre>
            """;
        assertEquals(expected, converter.convert(input), wrongConversionMsg);
    }

    @Test
    void codeBlock2() {
        String input = """
                # let square x = x * x;;
                val square : int -> int = <fun>
                # square 3;;
                - : int = 9
            """;
        String expected = """
            <pre><code>\
            # let square x = x * x;;
            val square : int -&gt; int = &lt;fun&gt;
            # square 3;;
            - : int = 9
            </code></pre>
            """;
        assertEquals(expected, converter.convert(input), wrongConversionMsg);
    }
}
