package com.rimalholdings.expensemanager.controller;

import com.rimalholdings.expensemanager.data.dto.CreateUserDTO;
import com.rimalholdings.expensemanager.data.dto.LoginRequestDTO;
import com.rimalholdings.expensemanager.service.TokenService;
import com.rimalholdings.expensemanager.service.UserService;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "AuthController")
@RestController
//@RequestMapping("/auth")
public class AuthController {

  private final TokenService tokenService;
  private final UserService userService;

  public AuthController(TokenService tokenService, UserService userService) {
    this.tokenService = tokenService;
    this.userService = userService;
  }

  @PostMapping("/token")
  public ResponseEntity<Map<String,String>> token(  Authentication authentication) {
    Map<String,String> token = Map.of("token", tokenService.generateToken(authentication));

    log.info("token generated for user: '{}' ", token);
    return ResponseEntity.ok(token);
  }
  @PostMapping("/user")
  public ResponseEntity<Map<String,String>> user(CreateUserDTO createUserDTO, Authentication authentication) {
    Map<String,String> user = Map.of("user", authentication.getName());
    userService.createUser(createUserDTO);

    log.info("user generated for user: '{}' ", user);
    return ResponseEntity.ok(user);
  }

}
