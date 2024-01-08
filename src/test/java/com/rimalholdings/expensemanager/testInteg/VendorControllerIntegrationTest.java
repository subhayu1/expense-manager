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
	VendorEntity vendor = new VendorEntity();
	vendor.setName("test");
	vendor.setZip(12345);
	vendor.setAddress1("test");
	vendor.setExternalId("t12345");
	vendor.setVendorType(1);
	vendorRepository.save(vendor);
}

@AfterEach
void deleteVendorRepo() {
	vendorRepository.deleteAll();
}

@Test
void testIntegGetAllVendors() {
	assertNotNull(getResponseString(BASE_ENTITY_URL));
}

@Test
void testIntegGetVendorByIdShouldReturnVendor() {
	Long vendorId = vendorRepository.findAll().get(0).getId();
	String url = BASE_ENTITY_URL + vendorId;
	assertNotNull(getResponseString(url));
}

@Test
void testIntegPostVendorShouldReturnCreatedVendor() {
	assertNotNull(postResponseString(BASE_ENTITY_URL));
}

@Test
void testIntegDeleteVendorShouldReturnDeletedVendor() {
	Long vendorId = vendorRepository.findAll().get(0).getId();
	String url = BASE_ENTITY_URL + vendorId;
	String responseString = deleteResponseString(url);
	assertNotNull(responseString);
	assertEquals("Vendor deleted", responseString);
}

protected String postResponseString(String url) {
	return given()
		.header("Authorization", "Bearer " + getToken())
		.contentType(ContentType.JSON)
		.body(postVendorBody())
		.when()
		.post(url)
		.then()
		.statusCode(201)
		.extract()
		.asString();
}

private String postVendorBody() {
	return "{\n"
		+ "    \"name\": \"Apple Inc\",\n"
		+ "    \"address1\": \"1 Apple Park Way\",\n"
		+ "    \"vendorType\": 2,\n"
		+ "    \"address2\": \"\",\n"
		+ "    \"city\": \"Cupertino\",\n"
		+ "    \"state\": \"CA\",\n"
		+ "    \"zip\": 95014,\n"
		+ "    \"phone\": \"5106760209\",\n"
		+ "    \"email\": \"tim@apple.com\"\n"
		+ "}";
}
}