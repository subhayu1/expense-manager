/* (C)1 */
package com.rimalholdings.expensemanager.config;

import java.util.List;

import com.rimalholdings.expensemanager.service.UserService;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

private final RsaKeyProperties jwtConfigProperties;

private final UserService userService;

public SecurityConfig(RsaKeyProperties jwtConfigProperties, UserService userService) {
	this.jwtConfigProperties = jwtConfigProperties;
	this.userService = userService;
}

// @Bean
// public InMemoryUserDetailsManager users() {
// return new
// InMemoryUserDetailsManager(User.withUsername("srimal").password("{noop}password").authorities("read").build());
// }

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	return http.csrf(AbstractHttpConfigurer::disable)
		.authorizeHttpRequests(
			auth ->
				auth.requestMatchers(
						"/swagger-ui/index.html",
						"/swagger-ui/**",
						"/v3/api-docs/**",
						"/v3/api-docs/",
						"/auth/token",
								"/api/v1/**")
					.permitAll()
					.anyRequest()
					.authenticated())
		.sessionManagement(
			session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.decoder(jwtDecoder())))
		.userDetailsService(userService)
		.exceptionHandling(
			(ex) ->
				ex.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
					.accessDeniedHandler(new BearerTokenAccessDeniedHandler()))
		.build();
}
@Order(Ordered.HIGHEST_PRECEDENCE)
@Bean
SecurityFilterChain tokenSecurityFilterChain(HttpSecurity http) throws Exception {
	return http.securityMatcher(new AntPathRequestMatcher("/auth/token"))
		.authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
		.sessionManagement(
			session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.csrf(AbstractHttpConfigurer::disable)
		.userDetailsService(userService)
		.exceptionHandling(
			ex -> {
			ex.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint());
			ex.accessDeniedHandler(new BearerTokenAccessDeniedHandler());
			})
		.httpBasic(withDefaults())
		.build();
}

@Bean
public SecurityFilterChain basicAuthFilterChain(HttpSecurity http) throws Exception {
	return http.csrf(AbstractHttpConfigurer::disable)
		.authorizeHttpRequests(
			auth ->
				auth.requestMatchers("/auth/token")
					.permitAll()
					.anyRequest()
					.authenticated()) // match
		// the
		// token
		// endpoint
		.httpBasic(withDefaults()) // enable Basic Auth
		.csrf(AbstractHttpConfigurer::disable) // disable CSRF protection
		.sessionManagement(
			session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // set
		// session
		// management
		// to
		// stateless
		.build();
}

@Bean
//@Order(Ordered.HIGHEST_PRECEDENCE)
// matcher for create user endpoint
public SecurityFilterChain createUserFilterChain(HttpSecurity http) throws Exception {
	return http.csrf(AbstractHttpConfigurer::disable)
		.authorizeHttpRequests(
			auth ->
				auth.requestMatchers(new AntPathRequestMatcher("/auth/user", "POST"))
					.permitAll()
					.anyRequest()
					.authenticated())
		.sessionManagement(
			session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.decoder(jwtDecoder())))
		.userDetailsService(userService)
		.exceptionHandling(
			(ex) ->
				ex.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
					.accessDeniedHandler(new BearerTokenAccessDeniedHandler()))
		.build();
}
@Bean
// matcher for bill pay template endpoint
public SecurityFilterChain billPayTemplateFilterChain(HttpSecurity http) throws Exception {
	return http.csrf(AbstractHttpConfigurer::disable)
		.authorizeHttpRequests(
			auth ->
				auth.requestMatchers(new AntPathRequestMatcher("/api/v1/bill-payment/**", "GET"))
					.permitAll()
					.anyRequest()
					.authenticated())
		.sessionManagement(
			session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.decoder(jwtDecoder())))
		.userDetailsService(userService)
		.exceptionHandling(
			(ex) ->
				ex.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
					.accessDeniedHandler(new BearerTokenAccessDeniedHandler()))
		.build();
}

@Bean
JwtDecoder jwtDecoder() {
	return NimbusJwtDecoder.withPublicKey(jwtConfigProperties.publicKey()).build();
}

@Bean
JwtEncoder jwtEncoder() {
	JWK jwk =
		new RSAKey.Builder(jwtConfigProperties.publicKey())
			.privateKey(jwtConfigProperties.privateKey())
			.build();
	JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
	return new NimbusJwtEncoder(jwks);
}

@Bean
CorsConfigurationSource corsConfigurationSource() {
	CorsConfiguration configuration = new CorsConfiguration();
	configuration.setAllowedOrigins(List.of("https://localhost:3000"));
	configuration.setAllowedHeaders(List.of("*"));
	configuration.setAllowedMethods(List.of("GET"));
	UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	source.registerCorsConfiguration("/**", configuration);
	return source;
}

@Bean
public PasswordEncoder passwordEncoder() {
	return new BCryptPasswordEncoder();
}
}
