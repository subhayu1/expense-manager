package com.rimalholdings.expensemanager.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration {

@Bean
public WebMvcConfigurer corsConfigurer() {
	return new WebMvcConfigurer() {
	@Override
	public void addCorsMappings(CorsRegistry registry) {

		registry
			.addMapping("/auth/token")
			.allowedOrigins("http://localhost:5173")
			.allowedMethods("POST")
			.allowedHeaders("*")
			.exposedHeaders("Authorization", "Content-Type")
			.allowCredentials(true)
			.maxAge(3600);
	}
	};
}

@Bean
public CorsFilter corsFilter() {
	CorsConfiguration configuration = new CorsConfiguration();
	configuration.setAllowCredentials(true);

	// Using patterns that are explicitly known to work with CORS
	configuration.setAllowedOriginPatterns(
		List.of(
			"http://localhost:[*]", // Matches all ports on localhost
			"http://piserver.ops" // Assumes this is a valid origin, adjust if this needs wildcard
			));

	configuration.addAllowedHeader("*");
	configuration.addAllowedMethod("*");

	UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	source.registerCorsConfiguration("/**", configuration);
	return new CorsFilter(source);
}
}
