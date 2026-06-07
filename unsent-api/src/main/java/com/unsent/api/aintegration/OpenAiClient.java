package com.unsent.api.aintegration;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.unsent.api.config.OpenAiConfig;
import com.unsent.api.repository.AiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OpenAiClient implements AiClient {

    private final OpenAiConfig config;

    @Override
    public Optional<String> generate(String prompt) {

        OpenAIClient client =
                OpenAIOkHttpClient.builder()
                        .apiKey(config.getOpenaiKey())
                        .build();

        ChatCompletionCreateParams params =
                ChatCompletionCreateParams.builder()
                        .model("gpt-4.1-mini")
                        .addUserMessage(prompt)
                        .maxCompletionTokens(150)
                        .build();

        ChatCompletion completion =
                client.chat()
                        .completions()
                        .create(params);

        return completion.choices()
                .get(0)
                .message()
                .content();
    }
}
