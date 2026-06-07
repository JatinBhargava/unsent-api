package com.unsent.api.repository;

import java.util.Optional;

public interface AiClient {
    Optional<String> generate(String prompt);
}
