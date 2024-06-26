package com.rimalholdings.expensemanager;

import com.rimalholdings.expensemanager.config.RsaKeyProperties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@Slf4j(topic = "ExpenseManagerApplication")
@EnableConfigurationProperties(RsaKeyProperties.class)
public class ExpenseManagerApplication {

private static final String ADMIN = "admin";
private static final String PASSWORD = "password";
private static final String ROLE_ADMIN = "ADMIN";

public static void main(String[] args) {
	SpringApplication.run(ExpenseManagerApplication.class, args);
}
}
