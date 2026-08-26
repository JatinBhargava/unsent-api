package com.unsent.api.helper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PostcardLinkTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PostcardLink link = new PostcardLink("https://unsent.test/");

    @Test
    void trimsTrailingSlashesFromTheConfiguredHost() {
        assertTrue(link.build("Ada", "Grace", "hi").startsWith("https://unsent.test/postcard/open#"));
    }

    @Test
    void survivesTheRoundTripTheBrowserWillDo() throws Exception {
        String message = "Some things are easier to post — café, naïve, 🌙\nsecond line";

        JsonNode decoded = decodeFragment(link.build("Ada", "Grace", message));

        assertEquals("Ada", decoded.get("to").asText());
        assertEquals("Grace", decoded.get("from").asText());
        assertEquals(message, decoded.get("message").asText());
    }

    @Test
    void producesAFragmentSafeToPasteInAnHref() {
        String fragment = fragmentOf(link.build(null, null, "x".repeat(1200)));

        // base64url only, so nothing in the payload needs percent-encoding and no
        // mail client can mangle the link by wrapping it
        assertTrue(fragment.matches("[A-Za-z0-9_-]+"), fragment);
    }

    @Test
    void keepsTheWholeMessageInsideATypicalUrlBudget() {
        // 1200 chars is the cap the UI enforces; browsers and mail clients start
        // truncating links well past this, so the encoded form has to stay under it
        assertTrue(link.build("Ada", "Grace", "x".repeat(1200)).length() < 2000);
    }

    private JsonNode decodeFragment(String url) throws Exception {
        byte[] json = Base64.getUrlDecoder().decode(fragmentOf(url));
        return objectMapper.readTree(new String(json, StandardCharsets.UTF_8));
    }

    private String fragmentOf(String url) {
        return url.substring(url.indexOf('#') + 1);
    }
}
