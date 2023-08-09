package com.arits.docRepo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DirectoryTreeViewApplication {

	public static void main(String[] args) {		
		//SpringApplication.run(DirectoryTreeViewApplication.class, args);
		SpringApplicationBuilder builder = new SpringApplicationBuilder(DirectoryTreeViewApplication.class);

		builder.headless(false);

		ConfigurableApplicationContext context = builder.run(args);
	}

}
