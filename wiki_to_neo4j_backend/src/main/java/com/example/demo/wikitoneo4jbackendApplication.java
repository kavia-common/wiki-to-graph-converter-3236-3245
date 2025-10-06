package com.example.wikitoneo4jbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * PUBLIC_INTERFACE
 * Spring Boot entry point for the backend application.
 */
@SpringBootApplication(scanBasePackages = {
		"com.example.wikitoneo4jbackend"
})
public class wikitoneo4jbackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(wikitoneo4jbackendApplication.class, args);
	}
}
