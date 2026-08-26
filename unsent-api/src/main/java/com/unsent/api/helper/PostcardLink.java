package com.unsent.api.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unsent.helper.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class PostcardLink {

    private static final String OPEN_PATH = "/postcard/open";

    // Owned rather than injected: this app registers no ObjectMapper bean, and a
    // link payload needs none of the application-wide serialization settings.
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String frontendUrl;

    public PostcardLink(@Value("${app.frontend-url}") String frontendUrl) {
        this.frontendUrl = frontendUrl.replaceAll("/+$", "");
    }

    /**
     * The whole postcard rides in the URL fragment. A browser never sends a fragment
     * to a server, so the link *is* the postcard — it needs no row in any table, and
     * the API never sees the message again after delivery.
     */
    public String build(String toName, String fromName, String message) {

        Map<String, String> payload = new LinkedHashMap<>();
        payload.put("to", toName);
        payload.put("from", fromName);
        payload.put("message", message);

        try {
            String json = objectMapper.writeValueAsString(payload);
            String encoded = Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(json.getBytes(StandardCharsets.UTF_8));

            return frontendUrl + OPEN_PATH + "#" + encoded;

        } catch (JsonProcessingException e) {
            throw new BusinessException("PC004", "We could not prepare that postcard.");
        }
    }
}
