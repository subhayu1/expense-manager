// package com.rimalholdings.expensemanager.config;
//
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.cors.CorsConfiguration;
// import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
// import org.springframework.web.filter.CorsFilter;
//
// @Configuration
// public class WebConfiguration {
//
// @Bean
// public CorsFilter corsFilter() {
//	CorsConfiguration configuration = new CorsConfiguration();
//	configuration.setAllowCredentials(true); // Set based on your needs
//	configuration.addAllowedOriginPattern("*"); // Allows all origins
//
//	configuration.addAllowedHeader("*"); // Allows all headers
//	configuration.addAllowedMethod("*"); // Allows all methods
//
//	UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//	source.registerCorsConfiguration("/**", configuration);
//	return new CorsFilter(source);
// }
// }
