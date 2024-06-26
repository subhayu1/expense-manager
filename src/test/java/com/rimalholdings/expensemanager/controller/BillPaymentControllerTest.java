package com.rimalholdings.expensemanager.controller;

import com.rimalholdings.expensemanager.data.dto.BillPayment;
import com.rimalholdings.expensemanager.data.entity.BillPaymentEntity;
import com.rimalholdings.expensemanager.model.mapper.BillPaymentServiceMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class BillPaymentControllerTest {

@Mock private BillPaymentServiceMapper billPaymentMapper;

@Mock private Pageable pageable;

@InjectMocks private BillPaymentController billPaymentController;

@BeforeEach
void setUp() {
	MockitoAnnotations.openMocks(this);
}

@Test
void shouldReturnCreatedBillPaymentWhenBillPaymentDTOIsValid() {
	BillPayment billPayment = new BillPayment();
	billPayment.setId(null);
	when(billPaymentMapper.saveBillPayment(billPayment)).thenReturn(new BillPayment());

	ResponseEntity<BillPayment> response = billPaymentController.createBillPayment(billPayment);

	assertEquals(billPayment, response.getBody());
}

@Test
void shouldReturnAllBillPaymentsWhenRequested() {
	Page<BillPaymentEntity> billPayments = Page.empty();
	when(billPaymentMapper.getAllEntities(pageable)).thenReturn(billPayments);

	ResponseEntity<Page<BillPaymentEntity>> response =
		billPaymentController.getAllBillPayments(pageable);

	assertEquals(billPayments, response.getBody());
}

@Test
void shouldReturnBillPaymentWhenBillPaymentIdIsValid() {
	Long billPaymentId = 1L;
	when(billPaymentMapper.getEntity(billPaymentId)).thenReturn("billPayment");

	ResponseEntity<String> response = billPaymentController.getBillPayment(billPaymentId);

	assertEquals("billPayment", response.getBody());
}
}
