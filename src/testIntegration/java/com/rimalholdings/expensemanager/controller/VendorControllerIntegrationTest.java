package com.rimalholdings.expensemanager.controller;

import com.rimalholdings.expensemanager.data.dao.UserRepository;
import com.rimalholdings.expensemanager.data.dao.VendorRepository;
import com.rimalholdings.expensemanager.data.dto.CreateUserDTO;
import com.rimalholdings.expensemanager.data.dto.VendorDTO;
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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.oauth2.jwt.JwtEncoderParameters.from;

@Component
public class VendorControllerIntegrationTest extends AbstractIntegrationTest{


	@Autowired
	VendorRepository vendorRepository;
	@Autowired
	 UserRepository userRepository;
	private VendorEntity createVendor(String name, int zip, String address, String externalId, int vendorType) {
		VendorEntity vendor = new VendorEntity();
		vendor.setName(name);
		vendor.setZip(zip);
		vendor.setAddress1(address);
		vendor.setExternalId(externalId);
		vendor.setVendorType(vendorType);
		return vendor;
	}
	@Test
void testGetAllVendors()   {
	VendorEntity vendor1 = createVendor("test", 12336, "test", "t12336", 1);
VendorEntity vendor2 = createVendor("test1", 12369, "test1", "t123361", 1);
vendorRepository.saveAll(List.of(vendor1, vendor2));
 String token = authenticateAndGetToken("admin", "password");
		//String token = "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJyaW1hbGhvbGRpbmdzLmNvbSIsInN1YiI6ImFkbWluIiwiZXhwIjoxNzA0NTY5NTU3LCJpYXQiOjE3MDQ1NjU5NTcsInNjb3BlIjoiUk9MRV9BRE1JTiJ9.P-QB5n6DqtwIxm-3mw6Nykawjo8s9FTxpje8P6cGhiwy5UERH9zm3HbyEED0hNNj4eZ96uDasooxzSVNxcbmzxRJfyYxBIlLfyuuJQ-VtdMVrbZFLEMmQeBdZJJ549vdYT6RPY4WW4KuH2_I4WzitevrnSNTE28dw8RhYMAIbtcGQwEFNlpognlKD4HTSggM-g_8I61EjQSSQGMgiQT1t-9fb8-lG7BV2y_0jc16J_v7bgZN66E1N-kAcAMXFWeCnOnxvqgdlF-sceE9bpWp4aSJBKm6szyuMJQbM_g6NzGEP7T7t8LgvA4WeLmbAd2snb12_GK-9IeMiwsaVMhk9w";
		String responseString = given()
				.header("Authorization", "Bearer " + token)
				.contentType(ContentType.JSON)
				.when()
				.get("/api/v1/vendor/")
				.then()
				.statusCode(200)
				.extract().asString();
		assertEquals(2, vendorRepository.count());
		assertNotNull(responseString);
	}




}

