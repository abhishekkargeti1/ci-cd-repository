package com.auth.Application.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Configuration
@OpenAPIDefinition(
		info = @Info(
				title="Auth Service",
				description = "Genric auth service",
				contact = @Contact(
					name="Abhishek Kargeti",
					url="https://subString.com",
					email="abs@gmail.com"
					),
				version = "1.0",
				summary = "This app is very usefull."
				),
		security= {
				@SecurityRequirement(
						name="bearerAuth"
			)
		}
)
@SecurityScheme(
		name="bearerAuth",
		type=SecuritySchemeType.HTTP,
		scheme = "bearer",
		bearerFormat = "JWT"
)
public class APIDocumentationConfig {

}
