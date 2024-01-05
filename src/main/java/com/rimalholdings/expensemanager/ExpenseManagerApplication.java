package com.rimalholdings.expensemanager;

import com.rimalholdings.expensemanager.config.RsaKeyProperties;
import com.rimalholdings.expensemanager.data.dao.UserRepository;
import com.rimalholdings.expensemanager.data.dto.LoginRequestDTO;
import com.rimalholdings.expensemanager.data.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@Slf4j(topic = "ExpenseManagerApplication")
@EnableConfigurationProperties(RsaKeyProperties.class)
public class ExpenseManagerApplication {

private static final String ADMIN = "ADMIN";
private static final String PASSWORD = "password";
private static final String ROLE_ADMIN = "ROLE_ADMIN";

public static void main(String[] args) {
	SpringApplication.run(ExpenseManagerApplication.class, args);
}

@Bean
CommandLineRunner setupDefaultUser(UserRepository repository, PasswordEncoder passwordEncoder) {
	return args -> {
	try {
		repository.save(new UserEntity(ADMIN, passwordEncoder.encode(PASSWORD), ROLE_ADMIN));
	} catch (DataIntegrityViolationException e) {
		log.info("User already exists,skipping default user creation.");
	}
	};
}
}

