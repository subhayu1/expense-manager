package com.rimalholdings.expensemanager.testInteg;

import com.rimalholdings.expensemanager.data.dao.VendorRepository;
import com.rimalholdings.expensemanager.data.entity.VendorEntity;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@DirtiesContext
public class VendorControllerIntegrationTest extends AbstractIntegrationTest {

@Autowired VendorRepository vendorRepository;
private static final String BASE_ENTITY_URL = "/api/v1/vendor/";

@BeforeEach
void setUp() {
	vendorRepository.deleteAll();
	createAndSaveVendor("test", 12345, "test", "t12345", 1);
}

@AfterEach
void deleteVendorRepo() {
	vendorRepository.deleteAll();
}

private void createAndSaveVendor(
	String name, int zip, String address, String externalId, int vendorType) {
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
	String responseString = getResponseString(BASE_ENTITY_URL);
	System.out.println(responseString);
	assertNotNull(responseString);
}

@Test
void testIntegGetVendorByIdShouldReturnVendor() {
	Long vendorId = vendorRepository.findAll().get(0).getId();
	String url = BASE_ENTITY_URL + vendorId;
	String responseString = getResponseString(url);
	System.out.println(responseString);
	assertNotNull(responseString);
}

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

	String responseString = postResponseString(BASE_ENTITY_URL, vendor);
	System.out.println(responseString);
	assertNotNull(responseString);
}

@Test
void testIntegDeleteVendorShouldReturnDeletedVendor() {
	Long vendorId = vendorRepository.findAll().get(0).getId();
	String url = BASE_ENTITY_URL + vendorId;
	String responseString = deleteResponseString(url);
	System.out.println(responseString);
	assertNotNull(responseString);
	assertEquals("Vendor deleted", responseString);
}
}
