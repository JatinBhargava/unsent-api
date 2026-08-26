package com.unsent.api.helper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A postcard body is user-written text that lands in a stranger's inbox, so the
 * escaping is the part worth pinning down.
 */
class PostcardTemplateTest {

    private final PostcardTemplate template = new PostcardTemplate();

    private static final String URL = "https://unsent.test/postcard/open#abc";

    @Test
    void escapesMarkupInNames() {
        String html = template.buildHtml("<b>Ada</b>", "<i>Grace</i>", URL);

        assertFalse(html.contains("<b>Ada</b>"));
        assertFalse(html.contains("<i>Grace</i>"));
        assertTrue(html.contains("&lt;b&gt;Ada&lt;/b&gt;"));
        assertTrue(html.contains("&lt;i&gt;Grace&lt;/i&gt;"));
    }

    @Test
    void keepsTheMessageOutOfTheEmail() {
        String secret = "the thing I never said";
        String url = "https://unsent.test/postcard/open#encoded";

        assertFalse(template.buildHtml("Ada", "Grace", url).contains(secret));
        assertFalse(template.buildPlainText("Ada", "Grace", url).contains(secret));
    }

    @Test
    void linksToTheOpenPageInBothParts() {
        assertTrue(template.buildHtml("Ada", "Grace", URL).contains("href=\"" + URL + "\""));
        assertTrue(template.buildPlainText("Ada", "Grace", URL).contains(URL));
    }

    @Test
    void namesTheSenderWhenNamesAreMissing() {
        String html = template.buildHtml(null, "  ", URL);

        assertTrue(html.contains("Someone sent you a postcard"));
        assertTrue(html.contains(">you<"), "the card should still address the recipient");
    }

    @Test
    void carriesTheWriterInTheDisplayName() {
        assertTrue(template.buildFromName("Grace", "Unsent").equals("Grace via Unsent"));
        assertTrue(template.buildFromName(null, "Unsent").equals("Unsent"));
        assertTrue(template.buildFromName("  ", "Unsent").equals("Unsent"));
    }

    @Test
    void namesTheSenderInTheSubject() {
        assertTrue(template.buildSubject("Ada").contains("Ada"));
        assertTrue(template.buildSubject(" ").equals("You have a postcard"));
    }
}
