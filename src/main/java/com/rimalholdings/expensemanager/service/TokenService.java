/// * (C)1 */
// package com.rimalholdings.expensemanager.service;
//
// import java.time.Instant;
// import java.time.temporal.ChronoUnit;
// import java.util.stream.Collectors;
//
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.oauth2.jwt.JwtClaimsSet;
// import org.springframework.security.oauth2.jwt.JwtEncoder;
// import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
// import org.springframework.stereotype.Service;
//
// @Service
// public class TokenService {
//
// private static final String ISSUER = "rimalholdings.com";
// private final JwtEncoder encoder;
//
// public TokenService(JwtEncoder encoder) {
//	this.encoder = encoder;
// }
//
// public String generateToken(Authentication authentication) {
//	Instant now = Instant.now();
//	String scope =
//		authentication.getAuthorities().stream()
//			.map(GrantedAuthority::getAuthority)
//			.collect(Collectors.joining(" "));
//	JwtClaimsSet claims =
//		JwtClaimsSet.builder()
//			.issuer(ISSUER)
//			.issuedAt(now)
//			.expiresAt(now.plus(1, ChronoUnit.HOURS))
//			.subject(authentication.getName())
//			.claim("scope", scope)
//			.build();
//	return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
// }
// }
