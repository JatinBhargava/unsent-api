package com.unsent.unsent_listener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class UnsentListenerApplication {

	public static void main(String[] args) {
		SpringApplication.run(UnsentListenerApplication.class, args);
	}

}
