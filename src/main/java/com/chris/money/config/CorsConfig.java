package com.chris.money.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


public class CorsConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*") // Replace * with your allowed origins (e.g.,
														// "http://localhost:3000")
				.allowedMethods("GET", "POST", "PUT", "DELETE") // Allowed HTTP methods
				.allowedHeaders("*") // Allowed request headers
				.allowCredentials(true) // Allow credentials like cookies
				.maxAge(3600); // Max age of the CORS pre-flight request cache (in seconds)
	}
}
