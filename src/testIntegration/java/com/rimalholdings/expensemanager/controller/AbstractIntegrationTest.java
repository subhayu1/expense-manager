package com.rimalholdings.expensemanager.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
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
  public static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.26")
      .withUsername("root")
      .withPassword("password");

  @BeforeAll
  static void beforeAll() {
    mysql.start();
    // Configure Flyway to use hardcoded credentials
    Flyway.configure()
        .dataSource(mysql.getJdbcUrl(), "root", "password")
        .load()
        .migrate();
  }
  @BeforeEach
  void setUp(@LocalServerPort int port) {
    RestAssured.baseURI = "http://localhost:" + port;
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
    registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
    registry.add("flyway.url", mysql::getJdbcUrl);
    registry.add("flyway.user", () -> "root");
    registry.add("flyway.password", () -> "password");

  }

  protected String authenticateAndGetToken(String username, String password) {
    String base64Credentials = Base64.getEncoder()
        .encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));

    Response response = RestAssured.given()
        .header("Authorization", "Basic " + base64Credentials)
        .header("Accept", "application/json")
        .contentType(ContentType.JSON)
        .when()
        .post("/auth/token");

    return response.then()
        .extract()
        .path("token");
  }
}
