package com.rimalholdings.expensemanager;

import com.rimalholdings.expensemanager.config.RsaKeyProperties;
import com.rimalholdings.expensemanager.data.dao.UserRepository;
import com.rimalholdings.expensemanager.data.dto.LoginRequestDTO;
import com.rimalholdings.expensemanager.data.entity.UserEntity;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class ExpenseManagerApplication {

  public static void main(String[] args) {
    SpringApplication.run(ExpenseManagerApplication.class, args);
  }
  @Bean
CommandLineRunner setupDefaultUser(UserRepository repository, PasswordEncoder passwordEncoder) {
  return args -> {
    repository.save(new UserEntity("admin", passwordEncoder.encode("password"), "ADMIN"));
  };
}
  }

