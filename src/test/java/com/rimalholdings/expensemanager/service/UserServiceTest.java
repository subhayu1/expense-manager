package com.rimalholdings.expensemanager.service;

import com.rimalholdings.expensemanager.data.dao.UserRepository;
import com.rimalholdings.expensemanager.data.dto.CreateUser;
import com.rimalholdings.expensemanager.data.entity.UserEntity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserServiceTest {

@Mock private UserRepository userRepository;

private UserService userService;

@Mock private BCryptPasswordEncoder passwordEncoder;

@BeforeEach
public void setup() {
	MockitoAnnotations.openMocks(this);
	userService = new UserService(userRepository);
}

@Test
public void shouldLoadUserByUsernameWhenUserExists() {

	String username = "testUser";
	String password = "testPassword";
	String role = "USER";
	String encodedPassword = "encodedPassword";

	when(passwordEncoder.encode(password)).thenReturn(encodedPassword);

	UserEntity userEntity = new UserEntity();
	userEntity.setUsername(username);
	userEntity.setPassword(encodedPassword);
	userEntity.setRole(role);
	when(userRepository.findByUsername(username)).thenReturn(userEntity);

	userService.loadUserByUsername(username);

	verify(userRepository).findByUsername(username);
}

@Test
public void shouldThrowExceptionWhenLoadingNonExistingUser() {
	String username = "testUser";
	when(userRepository.findByUsername(username)).thenReturn(null);

	assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(username));

	verify(userRepository).findByUsername(username);
}

@Test
public void shouldCreateUserWhenUserDoesNotExist() {
	CreateUser createUser = new CreateUser();
	createUser.setUsername("testUser");
	createUser.setPassword("testPassword");
	createUser.setRole("USER");

	userService.createUser(createUser);

	verify(userRepository).save(any(UserEntity.class));
}
}
