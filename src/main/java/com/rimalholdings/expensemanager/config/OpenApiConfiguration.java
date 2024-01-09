/* (C)1 */
package com.rimalholdings.expensemanager.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

@Bean
public OpenAPI customOpenAPI() {
	final String securitySchemeName = "bearerAuth";
	return new OpenAPI()
		.info(
			new io.swagger.v3.oas.models.info.Info()
				.title("Expense Manager API")
				.description("API endpoints for Expense Manager application.")
				.summary("CRUD operations for Expense Manager application.")
				.contact(
					new io.swagger.v3.oas.models.info.Contact().email("srimal@rimalholdings.com"))
				.version("1.0.0"))
		.addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
		.components(
			new Components()
				.addSecuritySchemes(
					securitySchemeName,
					new SecurityScheme()
						.name(securitySchemeName)
						.type(SecurityScheme.Type.HTTP)
						.scheme("bearer")
						.bearerFormat("JWT")));
}
}
