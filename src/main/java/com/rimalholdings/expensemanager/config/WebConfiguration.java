// package com.rimalholdings.expensemanager.config;
//
// import java.util.List;
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
//	configuration.setAllowCredentials(true);
//	configuration.setAllowedOrigins(
//		List.of(
//			"http://localhost:5173",
//			"http://192.168.1.160:5173",
//			"http://admin.ops.rimalholdings.internal:5173",
//			"http://piserver.ops.rimalholdings.internal",
//			"http://192.168.1.145"));
//	configuration.addAllowedHeader("*");
//	configuration.addAllowedMethod("*");
//	configuration.setExposedHeaders(List.of("Authorization", "Content-Type"));
//	configuration.setMaxAge(
//		3600L); // Set how long the response from a pre-flight request can be cached
//
//	UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//	source.registerCorsConfiguration("/**", configuration);
//	return new CorsFilter(source);
// }
// }
