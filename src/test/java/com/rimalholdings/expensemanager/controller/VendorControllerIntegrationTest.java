package com.rimalholdings.expensemanager.controller;

import com.rimalholdings.expensemanager.data.dao.UserRepository;
import com.rimalholdings.expensemanager.data.dao.VendorRepository;
import com.rimalholdings.expensemanager.data.entity.VendorEntity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@Transactional
public class VendorControllerIntegrationTest extends AbstractIntegrationTest {

	@Autowired
	VendorRepository vendorRepository;
	@BeforeEach
	void setUp() {
		createAndSaveVendor("test", 12345, "test", "t12345", 1);

	}
	private void createAndSaveVendor(String name, int zip, String address, String externalId, int vendorType) {
		VendorEntity vendor = new VendorEntity();
		vendor.setName(name);
		vendor.setZip(zip);
		vendor.setAddress1(address);
		vendor.setExternalId(externalId);
		vendor.setVendorType(vendorType);
		vendorRepository.save(vendor);
	}
	@Test
	void testIntegGetAllVendors() {
		String responseString = getResponseString("/api/v1/vendor/");
		System.out.println(responseString);
		assertNotNull(responseString);
	}

//	@Test
//	@Transactional
//	@DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
//	void testIntegGetVendorByIdShouldReturnSingleVendor()  {
//	 createAndSaveVendor("test", 12345, "test", "t12345", 1);
//	 Long id = vendorRepository.findAll().get(0).getId();
//	 String url = String.format("/api/v1/vendor/%s", id);
//		String responseString = getResponseString(url);
//		System.out.println(responseString);
//
//		assertNotNull(responseString);
//	}
protected String postResponseString(String url, VendorEntity vendor) {
	return given()
			.header("Authorization", "Bearer " + getToken())
			.contentType(ContentType.JSON)
			.body(vendor)
			.when()
			.post(url)
			.then()
			.statusCode(201)
			.extract()
			.asString();
}

	@Test
	void testIntegPostVendorShouldReturnCreatedVendor() {
		VendorEntity vendor = new VendorEntity();
		vendor.setName("test2");
		vendor.setZip(12336);
		vendor.setAddress1("test2");
		vendor.setExternalId("t12336");
		vendor.setVendorType(1);

		String responseString = postResponseString("/api/v1/vendor/", vendor);
		System.out.println(responseString);
		assertNotNull(responseString);
	}

//	@Test
//	@Transactional
//	void testIntegDeleteVendorShouldReturnDeletedVendor() {
//		String url = String.format("/api/v1/vendor/%s", vendorId);
//		String responseString = deleteResponseString(url);
//		System.out.println(responseString);
//		assertNotNull(responseString);
//		assertEquals("Vendor deleted", responseString);
//	}

}
