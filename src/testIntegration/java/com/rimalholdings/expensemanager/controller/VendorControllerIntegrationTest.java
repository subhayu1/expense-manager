package com.rimalholdings.expensemanager.controller;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.rimalholdings.expensemanager.data.dao.UserRepository;
import com.rimalholdings.expensemanager.data.dao.VendorRepository;
import com.rimalholdings.expensemanager.data.entity.VendorEntity;
import io.restassured.http.ContentType;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

@Component
@ActiveProfiles("test")
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
		System.out.println(responseString);
		assertEquals(2, vendorRepository.count());
		assertNotNull(responseString);
	}




}

