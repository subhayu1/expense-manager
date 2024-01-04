package com.rimalholdings.expensemanager.controller;

import com.rimalholdings.expensemanager.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "AuthController")
@RestController
public class AuthController {

  private final TokenService tokenService;

  public AuthController(TokenService tokenService) {
    this.tokenService = tokenService;
  }

  @PostMapping("/token")
  public String token(Authentication authentication) {
    log.debug("Token requested for user: '{}'", authentication.getName());
    String token = tokenService.generateToken(authentication);
    log.info("token generated for user: '{}' ", token );

    return token;
  }

}
