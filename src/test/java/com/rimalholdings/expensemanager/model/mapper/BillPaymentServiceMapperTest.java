package com.rimalholdings.expensemanager.model.mapper;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.rimalholdings.expensemanager.data.dto.BillPayment;
import com.rimalholdings.expensemanager.data.entity.BillPaymentEntity;
import com.rimalholdings.expensemanager.data.entity.ExpenseEntity;
import com.rimalholdings.expensemanager.data.entity.VendorEntity;
import com.rimalholdings.expensemanager.service.BillPaymentService;
import com.rimalholdings.expensemanager.service.ExpenseService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BillPaymentServiceMapperTest {

@Mock private BillPaymentService billPaymentService;

@Mock private ExpenseService expenseService;

private BillPaymentServiceMapper billPaymentMapper;

private BillPayment billPayment;
private Map<String, BigDecimal> expensePaymentMap;
private VendorEntity vendorEntity;
private ExpenseEntity expenseEntity1;
private ExpenseEntity expenseEntity2;

@BeforeEach
void setUp() {
	MockitoAnnotations.openMocks(this);
	billPaymentMapper =
		new BillPaymentServiceMapper(new ObjectMapper(), billPaymentService, expenseService);

	billPayment = createBillPayment();
	expensePaymentMap = createExpensePaymentMap();
	vendorEntity = createVendorEntity();
	expenseEntity1 = createExpenseEntity(1L, BigDecimal.valueOf(100));
	expenseEntity2 = createExpenseEntity(2L, BigDecimal.valueOf(200));

	when(expenseService.findById(1L)).thenReturn(expenseEntity1);
	when(expenseService.findById(2L)).thenReturn(expenseEntity2);
}

@Test
void shouldMapDtoToEntityAndProcessExpensePaymentsWhenExpensePaymentsAreNotEmpty() {
	billPayment.setExpensePayments(expensePaymentMap);

	BillPaymentEntity billPaymentEntity = billPaymentMapper.mapToDTO(billPayment);

	assertNotNull(billPaymentEntity);
	assertEquals(BigDecimal.valueOf(100), billPaymentEntity.getPaymentAmount());
	assertEquals(2, billPaymentEntity.getPaymentMethod());
	assertEquals("123456", billPaymentEntity.getPaymentReference());
	assertEquals(1L, billPaymentEntity.getVendor().getId());
	assertEquals(2, billPaymentEntity.getExpenses().size());
}

@Test
void testShouldThrowRuntimeExceptionWhenExpensePaymentsAreEmpty() {
	billPayment.setExpensePayments(new HashMap<>());

	assertThrows(RuntimeException.class, () -> billPaymentMapper.mapToDTO(billPayment));
}

@Test
void testShouldThrowRuntimeExceptionWhenExpensePaymentAmountIsGreaterThanExpenseAmountDue() {
	expensePaymentMap.put("1", BigDecimal.valueOf(200));

	assertThrows(RuntimeException.class, () -> billPaymentMapper.mapToDTO(billPayment));
}

@Test
void testShouldThrowEntityNotFoundExceptionWhenExpenseIdIsInvalid() {
	expensePaymentMap.put("3", BigDecimal.valueOf(50));

	assertThrows(RuntimeException.class, () -> billPaymentMapper.mapToDTO(billPayment));
}

@Test
void testPaymentAmountAndExpensePaymentMismatchShouldThrowIllegalArgumentException() {
	expensePaymentMap.put("1", BigDecimal.valueOf(50));
	expensePaymentMap.put("2", BigDecimal.valueOf(50));
	billPayment.setPaymentAmount(BigDecimal.valueOf(150));

	assertThrows(RuntimeException.class, () -> billPaymentMapper.mapToDTO(billPayment));
}

private BillPayment createBillPayment() {
	BillPayment billPayment = new BillPayment();
	billPayment.setPaymentAmount(BigDecimal.valueOf(100));
	billPayment.setPaymentMethod(2);
	billPayment.setPaymentReference("123456");
	billPayment.setVendorId(1L);
	billPayment.setPaymentDate("2021-09-01 00:00:00.0");
	return billPayment;
}

private Map<String, BigDecimal> createExpensePaymentMap() {
	Map<String, BigDecimal> expensePaymentMap = new HashMap<>();
	expensePaymentMap.put("1", BigDecimal.valueOf(50));
	expensePaymentMap.put("2", BigDecimal.valueOf(50));
	return expensePaymentMap;
}

private VendorEntity createVendorEntity() {
	VendorEntity vendorEntity = new VendorEntity();
	vendorEntity.setId(1L);
	return vendorEntity;
}

private ExpenseEntity createExpenseEntity(Long id, BigDecimal amountDue) {
	ExpenseEntity expenseEntity = new ExpenseEntity();
	expenseEntity.setId(id);
	expenseEntity.setAmountDue(amountDue);
	expenseEntity.setTotalAmount(BigDecimal.valueOf(100));
	expenseEntity.setVendor(vendorEntity);
	return expenseEntity;
}
}
