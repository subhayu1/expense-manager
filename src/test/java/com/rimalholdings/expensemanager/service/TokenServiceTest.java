package com.rimalholdings.expensemanager.service;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtEncoder;

public class TokenServiceTest {

@Mock private JwtEncoder jwtEncoder;

@Mock private Authentication authentication;

private TokenService tokenService;

@BeforeEach
public void setup() {
	MockitoAnnotations.openMocks(this);
	tokenService = new TokenService(jwtEncoder);
}

//    @Test
//    public void shouldGenerateTokenWhenAuthenticationIsValid() {
//        when(authentication.getName()).thenReturn("testUser");
//        when(authentication.isAuthenticated()).thenReturn(true);
//        when(jwtEncoder.encode(any())).thenReturn("testToken");
//
//        String token = tokenService.generateToken(authentication);
//
//        assertNotNull(token);
//    }
}
