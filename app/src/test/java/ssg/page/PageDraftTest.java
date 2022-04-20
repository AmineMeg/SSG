package ssg.page;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import ssg.tomlvaluetypewrapper.TomlIntegerWrapper;
import ssg.tomlvaluetypewrapper.TomlValueTypeWrapper;

/**
 * Junit test class for PageDraft.
 */
@SuppressWarnings("PMD.TooManyMethods")
class PageDraftTest {

    @Test
    @SuppressWarnings("PMD.LawOfDemeter")
    void getDataEqualsData() {
        HashMap<String, TomlValueTypeWrapper> data = new HashMap<>();
        data.put("un", new TomlIntegerWrapper(1));
        data.put("deux", new TomlIntegerWrapper(2));
        PageDraft draft = new PageDraft(data, "", "");
        assertEquals(data, draft.getData().get(),
                "getData() must equal page given datas");
    }

    @Test
    void emptyDataEqualsOptionalEmpty() {
        PageDraft draft = new PageDraft(null, "", "");
        assertEquals(Optional.empty(), draft.getData(),
                "getData() must equal Optional.empty() for null datas");
    }

    @Test
    void getTitleEqualsTitle() {
        String title = "foo";
        PageDraft draft = new PageDraft(null, "", title);
        assertEquals(title, draft.getTitle(),
                "getTitle() must equal page given title");
    }

    @Test
    void getContentEqualsContent() {
        String content = "bar";
        PageDraft draft = new PageDraft(null, content, "");
        assertEquals(content, draft.getContent(),
                "getContent() must equal page given content");
    }
}
