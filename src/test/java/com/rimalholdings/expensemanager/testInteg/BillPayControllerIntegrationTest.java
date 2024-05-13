package com.rimalholdings.expensemanager.testInteg;

import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Date;

import com.rimalholdings.expensemanager.data.dao.*;
import com.rimalholdings.expensemanager.data.entity.ApPaymentEntity;
import com.rimalholdings.expensemanager.data.entity.BillPaymentEntity;
import com.rimalholdings.expensemanager.data.entity.VendorEntity;
import com.rimalholdings.expensemanager.util.DateTimeUtil;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@DirtiesContext
public class BillPayControllerIntegrationTest extends AbstractIntegrationTest {

@Autowired private BillPaymentRepository billPaymentRepository;
@Autowired private VendorRepository vendorRepository;
@Autowired private ExpenseRepository expenseRepository;
@Autowired private ApPaymentRepository apPaymentRepository;
private static final String BASE_ENTITY_URL = "/api/v1/bill-payment/";

@BeforeEach
void setUp() {
	billPaymentRepository.deleteAll();
	createExpenseEntity();
}

@Test
void testIntegGetAllBillPayments() {
	String responseString = getResponseString(BASE_ENTITY_URL);
	assertNotNull(responseString);
}

@Test
void testIntegPostBillPayShouldReturnCreatedBillPay() {
	String responseString = billPayPostResponseString(BASE_ENTITY_URL);
	assertNotNull(responseString);
}

@Test
void testIntegGetBillPayByIdShouldReturnBillPay() {
	createBllPaymentEntity();
	Long billPaymentId = billPaymentRepository.findAll().get(0).getId();
	String url = BASE_ENTITY_URL + billPaymentId;
	String responseString = getResponseString(url);
	assertNotNull(responseString);
}

private String billPaymentBody() {
	Long vendorEntity = vendorRepository.findAll().get(0).getId();
	String expenseId = expenseRepository.findAll().get(0).getId().toString();
	return """
						{
								"paymentAmount": 100,
								"paymentMethod": 1,
								"paymentReference": "123",
								"toSync": true,
								"paymentDate": "2021-09-01 00:00:00.0",
								"expensePayments": [
												{
																"vendorId": %s,
																"expenseId": %s,
																"paymentAmount": 100
												}
								]
				}

				\t"""
		.formatted(vendorEntity, expenseId);
	//	return "{\n"
	//		+ "  \"paymentAmount\": 100,\n"
	//		+ "  \"paymentMethod\": 1,\n"
	//		+ "  \"paymentReference\": \"123\",\n"
	//		+ "  \"paymentDate\": \"2021-09-01 00:00:00.0\",\n"
	//		+ "  \"vendorId\": "
	//		+ vendorEntity
	//		+ ",\n"
	//		+ "  \"expensePayments\": {\n"
	//		+ "    \""
	//		+ expenseId
	//		+ "\": 100\n"
	//		+ "  }\n"
	//		+ "}";
}

private String billPayPostResponseString(String url) {
	return given()
		.contentType(ContentType.JSON)
		.body(billPaymentBody())
		.when()
		.post(url)
		.then()
		.statusCode(201)
		.extract()
		.asString();
}

private void createBllPaymentEntity() {
	VendorEntity vendorEntity = saveVendorEntity();
	createApPaymentEntity();
	createExpenseEntity();

	BillPaymentEntity billPaymentEntity = new BillPaymentEntity();
	billPaymentEntity.setId(1L);
	billPaymentEntity.setPaymentAmount(new BigDecimal(100).round(new MathContext(2)));
	billPaymentEntity.setPaymentMethod(3);
	billPaymentEntity.setExpense(expenseRepository.findAll().get(0));
	billPaymentEntity.setApPayment(apPaymentRepository.findAll().get(0));
	billPaymentEntity.setPaymentReference("123");
	billPaymentEntity.setPaymentDate(DateTimeUtil.getCurrentTimeInUTC());
	billPaymentEntity.setCreatedDate(DateTimeUtil.getCurrentTimeInUTC());
	billPaymentEntity.setPaymentApplicationStatus(1);
	billPaymentEntity.setVendor(vendorEntity);
	billPaymentEntity.setToSync(true);
	billPaymentRepository.saveAndFlush(billPaymentEntity);
}

protected void createApPaymentEntity() {
	ApPaymentEntity apPaymentEntity = new ApPaymentEntity();
	apPaymentEntity.setId(1);
	apPaymentEntity.setPaymentAmount(new BigDecimal(100).round(new MathContext(2)));
	apPaymentEntity.setPaymentMethod(3);
	apPaymentEntity.setPaymentReference("123");
	apPaymentEntity.setPaymentDate(new Date(System.currentTimeMillis()));
	apPaymentEntity.setCreatedDate(new Date(System.currentTimeMillis()));

	apPaymentRepository.saveAndFlush(apPaymentEntity);
}
}
