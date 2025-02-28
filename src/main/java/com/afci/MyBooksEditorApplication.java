package com.afci;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(info = @Info(title = "Books API", version = "v1", description = "Gestion des livres"))
@SpringBootApplication
@EnableWebMvc
public class MyBooksEditorApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyBooksEditorApplication.class, args);
	}

}
