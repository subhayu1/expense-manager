package com.rimalholdings.expensemanager.testInteg;

import java.math.BigDecimal;
import java.math.MathContext;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import com.rimalholdings.expensemanager.data.dao.ExpenseRepository;
import com.rimalholdings.expensemanager.data.dao.VendorRepository;
import com.rimalholdings.expensemanager.data.entity.ExpenseEntity;
import com.rimalholdings.expensemanager.data.entity.VendorEntity;
import com.rimalholdings.expensemanager.helper.VendorHelper;
import com.rimalholdings.expensemanager.util.DateTimeUtil;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;

@ComponentScan(basePackages = {"com.rimalholdings.expensemanager"})
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Testcontainers
@DirtiesContext
public abstract class AbstractIntegrationTest {

@Autowired private VendorRepository vendorRepository;
@Autowired private ExpenseRepository expenseRepository;

@Container
public static MySQLContainer<?> mysql =
	new MySQLContainer<>("mysql:8.0.26").withUsername("root").withPassword("password");

@BeforeAll
static void beforeAll() {
	mysql.start();
	// Configure Flyway to use hardcoded credentials
	Flyway.configure().dataSource(mysql.getJdbcUrl(), "root", "password").load().migrate();
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
	String base64Credentials =
		Base64.getEncoder()
			.encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));

	Response response =
		given()
			.header("Authorization", "Basic " + base64Credentials)
			.header("Accept", "application/json")
			.contentType(ContentType.JSON)
			.when()
			.post("/auth/token");
	return response.then().extract().path("token");
}

protected String getToken() {
	String token = authenticateAndGetToken("admin", "password");
	System.out.println(token);
	return token;
}

protected String getResponseString(String url) {
	return given()
		.header("Authorization", "Bearer " + getToken())
		.contentType(ContentType.JSON)
		.when()
		.get(url)
		.then()
		.statusCode(200)
		.extract()
		.asString();
}

protected String deleteResponseString(String url) {
	return given()
		.header("Authorization", "Bearer " + getToken())
		.contentType(ContentType.JSON)
		.when()
		.delete(url)
		.then()
		.statusCode(200)
		.extract()
		.asString();
}

protected VendorEntity saveVendorEntity() {
	VendorEntity vendorEntity = new VendorEntity();
	vendorEntity.setId(1L);
	vendorEntity.setName("test");
	vendorEntity.setAddress1("test");
	vendorEntity.setZip(12345);
	vendorEntity.setCity("test");
	vendorEntity.setCreatedDate(DateTimeUtil.getCurrentTimeInUTC());
	vendorEntity.setUpdatedDate(DateTimeUtil.getCurrentTimeInUTC());

	vendorEntity.setExternalId(
		VendorHelper.generateVendorId(vendorEntity.getName(), vendorEntity.getZip()));
	vendorEntity.setVendorType(1);
	return vendorRepository.saveAndFlush(vendorEntity);
}

protected void createExpenseEntity() {
	VendorEntity vendorEntity = saveVendorEntity();
	ExpenseEntity expenseEntity = new ExpenseEntity();
	expenseEntity.setId(1L);
	expenseEntity.setTotalAmount(new BigDecimal(100).round(new MathContext(2)));
	expenseEntity.setPaymentStatus(3);
	expenseEntity.setAmountDue(new BigDecimal(100).round(new MathContext(2)));
	expenseEntity.setVendor(vendorEntity);
	expenseRepository.saveAndFlush(expenseEntity);
}
}
