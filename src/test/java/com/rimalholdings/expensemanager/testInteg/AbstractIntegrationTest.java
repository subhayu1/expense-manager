package com.rimalholdings.expensemanager.testInteg;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Date;

import com.rimalholdings.expensemanager.data.dao.ApPaymentRepository;
import com.rimalholdings.expensemanager.data.dao.BillPaymentRepository;
import com.rimalholdings.expensemanager.data.dao.ExpenseRepository;
import com.rimalholdings.expensemanager.data.dao.VendorRepository;
import com.rimalholdings.expensemanager.data.entity.ExpenseEntity;
import com.rimalholdings.expensemanager.data.entity.VendorEntity;
import com.rimalholdings.expensemanager.helper.VendorHelper;
import com.rimalholdings.expensemanager.util.DateTimeUtil;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
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
import org.testcontainers.utility.DockerImageName;

import static io.restassured.RestAssured.given;

@ComponentScan(basePackages = {"com.rimalholdings.expensemanager"})
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Testcontainers
@DirtiesContext
public abstract class AbstractIntegrationTest {

@Autowired private VendorRepository vendorRepository;
@Autowired private ExpenseRepository expenseRepository;
@Autowired private BillPaymentRepository billPaymentRepository;
@Autowired private ApPaymentRepository apPaymentRepository;

@Container
public static MySQLContainer<?> mysql =
	new MySQLContainer<>(DockerImageName.parse("mysql:8.0.37"))
		.withUsername("root")
		.withPassword("password")
		.withDatabaseName("test")
		.withReuse(true);

@BeforeAll
static void beforeAll() throws IOException, InterruptedException {
	mysql.start();
	// create test database
	mysql.execInContainer("mysql -uroot -ppassword -e 'CREATE DATABASE test'");

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

protected String getResponseString(String url) {
	return given()
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
	vendorEntity.setZip("12345");
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
	expenseEntity.setIntegrationId("integrationId");
	expenseEntity.setVendorInvoiceNumber("vendorInvoiceNumber");
	expenseEntity.setExternalInvoiceNumber("externalInvoiceNumber");
	expenseEntity.setCreatedDate(DateTimeUtil.getCurrentTimeInUTC());
	expenseEntity.setUpdatedDate(DateTimeUtil.getCurrentTimeInUTC());
	expenseEntity.setDueDate(Date.valueOf("2022-01-01"));
	expenseEntity.setInvoiceDate(Date.valueOf("2022-01-01"));
	expenseEntity.setDescription("Test Expense");
	expenseEntity.setExternalOrgId(1);
	expenseEntity.setVendor(vendorEntity);
	expenseRepository.saveAndFlush(expenseEntity);
}
}
