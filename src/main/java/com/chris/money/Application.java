package com.chris.money;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.chris.money") // Add the package where AccountRepository is located
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
