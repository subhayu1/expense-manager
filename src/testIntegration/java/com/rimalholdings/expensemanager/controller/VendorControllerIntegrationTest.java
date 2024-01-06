package com.rimalholdings.expensemanager.controller;

import com.rimalholdings.expensemanager.data.dao.UserRepository;
import com.rimalholdings.expensemanager.data.dao.VendorRepository;
import com.rimalholdings.expensemanager.data.dto.CreateUserDTO;
import com.rimalholdings.expensemanager.data.entity.UserEntity;
import com.rimalholdings.expensemanager.data.entity.VendorEntity;
import com.rimalholdings.expensemanager.service.UserService;
import com.rimalholdings.expensemanager.service.VendorService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static  io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.rimalholdings.expensemanager"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VendorControllerIntegrationTest {

	VendorController vendorController;
	@LocalServerPort
	private Integer port= 9000;

	@Autowired
	VendorRepository vendorRepository;
	@Autowired
	 UserRepository userRepository;

@Container
public static MySQLContainer<?> mysql =
	new MySQLContainer<>("mysql:8.0.26");


  @BeforeAll
static void beforeAll() {
	mysql.start();
}
@AfterAll
static void afterAll() {
	mysql.stop();
}
@DynamicPropertySource
static void mysqlProperties(DynamicPropertyRegistry registry) {
	registry.add("spring.datasource.url", mysql::getJdbcUrl);
	registry.add("spring.datasource.username", mysql::getUsername);
	registry.add("spring.datasource.password", mysql::getPassword);
}

@BeforeEach
void setUp() {
	RestAssured.baseURI = "http://localhost:" + port;

}

	@Test
void testGetAllVendors()   {
		String token = "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJyaW1hbGhvbGRpbmdzLmNvbSIsInN1YiI6ImFkbWluIiwiZXhwIjoxNzA0NTY5NTU3LCJpYXQiOjE3MDQ1NjU5NTcsInNjb3BlIjoiUk9MRV9BRE1JTiJ9.P-QB5n6DqtwIxm-3mw6Nykawjo8s9FTxpje8P6cGhiwy5UERH9zm3HbyEED0hNNj4eZ96uDasooxzSVNxcbmzxRJfyYxBIlLfyuuJQ-VtdMVrbZFLEMmQeBdZJJ549vdYT6RPY4WW4KuH2_I4WzitevrnSNTE28dw8RhYMAIbtcGQwEFNlpognlKD4HTSggM-g_8I61EjQSSQGMgiQT1t-9fb8-lG7BV2y_0jc16J_v7bgZN66E1N-kAcAMXFWeCnOnxvqgdlF-sceE9bpWp4aSJBKm6szyuMJQbM_g6NzGEP7T7t8LgvA4WeLmbAd2snb12_GK-9IeMiwsaVMhk9w";
	given()
			.header("Authorization", "Bearer " + token)
			.contentType(ContentType.JSON)
		.when()
		.get("/api/v1/vendor/")
		.then()
		.statusCode(200);



}

}

