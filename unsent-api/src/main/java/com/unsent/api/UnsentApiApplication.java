package com.unsent.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication(scanBasePackages = "com.unsent")
@EnableCaching
public class UnsentApiApplication {

	public static void main(String[] args) {

		SpringApplication.run(UnsentApiApplication.class, args);
	}

}
