package com.unsent.unsent_listener.listeners;

import org.springframework.kafka.annotation.KafkaListener;

public class like_event {

    @KafkaListener(topics = "unsent-events")
    public void handle(String message) {
        // process event
    }

}
