package com.rimalholdings.expensemanager.testInteg;

import com.rimalholdings.expensemanager.data.dao.ExpenseRepository;
import com.rimalholdings.expensemanager.data.dao.VendorRepository;

import io.restassured.http.ContentType;
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
public class ExpenseControllerIntegrationTest extends AbstractIntegrationTest {

@Autowired private ExpenseRepository expenseRepository;
@Autowired private VendorRepository vendorRepository;

private static final String BASE_ENTITY_URL = "/api/v1/expense/";

@BeforeEach
void setUp() {
	expenseRepository.deleteAll();
	saveVendorEntity();
}

@Test
void testIntegGetAllExpenses() {
	String responseString = getResponseString(BASE_ENTITY_URL);
	assertNotNull(responseString);
}

@Test
void testIntegGetExpenseByIdShouldReturnExpense() {
	createExpenseEntity();
	Long expenseId = expenseRepository.findAll().get(0).getId();
	String url = BASE_ENTITY_URL + expenseId;
	String responseString = getResponseString(url);
	assertNotNull(responseString);
}

@Test
void testIntegPostExpenseShouldReturnCreatedExpense() {
	String responseString = postRequest();
	assertNotNull(responseString);
}

@Test
void testIntegDeleteExpenseShouldReturn200() {
	createExpenseEntity();
	Long expenseId = expenseRepository.findAll().get(0).getId();
	String url = BASE_ENTITY_URL + expenseId;
	String deleteResponse = deleteResponseString(url);
	assertEquals(0, expenseRepository.count());
	assertEquals("Expense deleted", deleteResponse);
	assertNotNull(deleteResponse);
}

@Test
void testIntegUpdateExpenseShouldReturnUpdatedExpense() {
	createExpenseEntity();
	String putResponse = putRequest();
	assertNotNull(putResponse);
}

private String postRequest() {
	return given()
		.header("Authorization", "Bearer " + getToken())
		.contentType(ContentType.JSON)
		.body(expensePostRequestString())
		.when()
		.post(BASE_ENTITY_URL)
		.then()
		.statusCode(201)
		.extract()
		.asString();
}

private String putRequest() {
	return given()
		.header("Authorization", "Bearer " + getToken())
		.contentType(ContentType.JSON)
		.body(expensePutRequestString())
		.when()
		.put(BASE_ENTITY_URL)
		.then()
		.statusCode(200)
		.extract()
		.asString();
}

private String expensePostRequestString() {
	Long vendorId = vendorRepository.findAll().get(0).getId();
	return """
				{
						"vendorId": %s,
						"totalAmount": 100,
						"description": "test",
						"dueDate": "2024-11-22 12:00:00.0"
				}"""
		.formatted(vendorId);
}

private String expensePutRequestString() {
	Long vendorId = vendorRepository.findAll().get(0).getId();

	return """
							{
						"id": 1,
						"vendorId": %s,
						"totalAmount": 100,
						"description": "test",
						"dueDate": "2024-11-22 12:00:00.0"
				}"""
		.formatted(vendorId);
}
}
