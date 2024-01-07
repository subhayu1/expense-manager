package com.rimalholdings.expensemanager.controller;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.rimalholdings.expensemanager.data.dao.BillPaymentRepository;
import com.rimalholdings.expensemanager.data.dao.VendorRepository;
import com.rimalholdings.expensemanager.data.dto.BillPaymentDTO;
import com.rimalholdings.expensemanager.data.entity.BillPaymentEntity;
import com.rimalholdings.expensemanager.data.entity.VendorEntity;
import io.restassured.http.ContentType;
import java.math.BigDecimal;
import java.math.MathContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
public class BillPayControllerIntegrationTest extends AbstractIntegrationTest {
  @Autowired
  BillPaymentRepository billPaymentRepository;

  @Autowired
  VendorRepository vendorRepository;

  @BeforeEach
  void setUp() {
   saveVendorEntity();
  }
  @Test
  void testIntegGetAllBillPayments() {
    String responseString = getResponseString("/api/v1/bill-payment/");
    System.out.println(responseString);
    assertNotNull(responseString);
  }
  protected String billPaypostResponseString(String url, BillPaymentEntity billPayment) {
    return given()
        .header("Authorization", "Bearer " + getToken())
        .contentType(ContentType.JSON)
        .body(billPayment)
        .when()
        .post(url)
        .then()
        .statusCode(201)
        .extract()
        .asString();
  }
  @Transactional
void saveVendorEntity() {
    VendorEntity vendorEntity = new VendorEntity();
    vendorEntity.setId(1L);
    vendorEntity.setName("test");
    vendorEntity.setAddress1("test");
    vendorEntity.setZip(12345);
    vendorEntity.setExternalId("t12345");
    vendorEntity.setVendorType(1);
   vendorRepository.save(vendorEntity);
}

// @Test
// @Transactional
//  @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
//void testIntegPostBillPayShouldReturnCreatedBillPay() {
//    VendorEntity vendorEntity = vendorRepository.findAll().get(0);
//    BillPaymentEntity billPaymentEntity = new BillPaymentEntity();
//    billPaymentEntity.setVendor(vendorEntity);
//    billPaymentEntity.setPaymentAmount(new BigDecimal("100.00").round(new MathContext(2)));
//    String responseString = billPaypostResponseString("/api/v1/bill-payment/",billPaymentEntity );
//    System.out.println(responseString);
//    assertNotNull(responseString);
//}
  //TODO: Add test for BillPayControllerIntegrationTest
  //

}

