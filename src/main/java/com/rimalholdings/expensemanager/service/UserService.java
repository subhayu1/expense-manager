/* (C)1 */
package com.rimalholdings.expensemanager.service;

import com.rimalholdings.expensemanager.data.dao.UserRepository;
import com.rimalholdings.expensemanager.data.dto.CreateUser;
import com.rimalholdings.expensemanager.data.entity.UserEntity;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
private final UserRepository userRepository;

public UserService(UserRepository userRepository) {
	this.userRepository = userRepository;
}

@Override
public UserDetails loadUserByUsername(String clientId) throws UsernameNotFoundException {
	UserEntity userEntity = userRepository.findByUsername(clientId);
	if (userEntity == null) {
	throw new UsernameNotFoundException("User not found");
	}
	return User.withUsername(userEntity.getUsername())
		.password(userEntity.getPassword())
		.roles(userEntity.getRole())
		.build();
}

public void createUser(CreateUser createUser) {
	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	UserEntity userEntity = new UserEntity();
	userEntity.setUsername(createUser.getUsername());
	userEntity.setPassword(passwordEncoder.encode(createUser.getPassword()));
	userEntity.setRole(createUser.getRole());
	userRepository.save(userEntity);
}
}
