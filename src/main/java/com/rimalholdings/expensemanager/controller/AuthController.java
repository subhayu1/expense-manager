/* (C)1 */
package com.rimalholdings.expensemanager.controller;

import java.util.Map;

import com.rimalholdings.expensemanager.data.dto.CreateUser;
import com.rimalholdings.expensemanager.service.TokenService;
import com.rimalholdings.expensemanager.service.UserService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j(topic = "AuthController")
@RestController
// @CrossOrigin
@RequestMapping("/auth")
public class AuthController {

private final TokenService tokenService;
private final UserService userService;

public AuthController(TokenService tokenService, UserService userService) {
	this.tokenService = tokenService;
	this.userService = userService;
}

@PostMapping("/token")
public ResponseEntity<Map<String, String>> token(Authentication authentication) {
	Map<String, String> token = Map.of("token", tokenService.generateToken(authentication));

	log.info("token generated for user: '{}' ", token);
	return ResponseEntity.ok(token);
}

@PostMapping("/user")
public ResponseEntity<Map<String, String>> user(@RequestBody CreateUser createUser) {
	Map<String, String> user = Map.of("user", createUser.getUsername());
	userService.createUser(createUser);

	log.info("user generated for user: '{}' ", user);
	return ResponseEntity.ok(user);
}
}
