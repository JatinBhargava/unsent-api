package com.unsent.api.helper;

import org.springframework.stereotype.Component;

@Component
public class BuiltPrompt {

    public String buildPrompt(String storyContent) {

        return """
            You are a creative writing assistant.

            Continue the story below.

            Rules:
            - Keep the same tone.
            - Keep the same characters.
            - Do not contradict existing events.
            - Generate a maximum of 100 words.
            - Return only the continuation.

            Story:
            %s
            """.formatted(storyContent);
    }

}
