package com.rimalholdings.expensemanager.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@ComponentScan(basePackages = {"com.rimalholdings.expensemanager"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public abstract class AbstractIntegrationTest {

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

  protected String authenticateAndGetToken(String username, String password) {
    // Encode credentials
    String base64Credentials = Base64.getEncoder()
        .encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));

    // Send request with Basic Authorization
    Response response = RestAssured.given()
        .header("Authorization", "Basic " + base64Credentials)
        .header("Accept", "application/json")
        .contentType(ContentType.JSON) // Include this if the endpoint expects a Content-Type
        .when()
        .post("/auth/token"); // Adjust the endpoint as needed

    return response.then()
        .extract()
        .path("token");
  }
}

