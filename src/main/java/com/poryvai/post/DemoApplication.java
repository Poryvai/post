package com.poryvai.post;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main entry point for the Spring Boot application.
 * This class uses the {@link SpringBootApplication @SpringBootApplication} annotation,
 * which combines {@code @Configuration}, {@code @EnableAutoConfiguration}, and {@code @ComponentScan}
 * to provide a comprehensive setup for a Spring Boot application.
 */
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
