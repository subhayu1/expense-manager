package com.rimalholdings.expensemanager.model.mapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.rimalholdings.expensemanager.data.dto.BillPayment;
import com.rimalholdings.expensemanager.data.dto.ExpensePayment;
import com.rimalholdings.expensemanager.data.entity.ExpenseEntity;
import com.rimalholdings.expensemanager.data.entity.VendorEntity;
import com.rimalholdings.expensemanager.exception.NoExpensePaymentsSpecifiedException;
import com.rimalholdings.expensemanager.service.BillPaymentService;
import com.rimalholdings.expensemanager.service.ExpenseService;

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
private VendorEntity vendorEntity;
private ExpenseEntity expenseEntity1;
private ExpenseEntity expenseEntity2;
List<ExpenseEntity> expenseEntities = new ArrayList<>();
private ApPaymentMapper apPaymentMapper;

@BeforeEach
void setUp() {
	MockitoAnnotations.openMocks(this);
	billPaymentMapper =
		new BillPaymentServiceMapper(billPaymentService, expenseService, apPaymentMapper);

	billPayment = createBillPayment();
	vendorEntity = createVendorEntity();
	expenseEntity1 = createExpenseEntity(1L, BigDecimal.valueOf(100));
	expenseEntity2 = createExpenseEntity(2L, BigDecimal.valueOf(200));

	when(expenseService.findById(1L)).thenReturn(expenseEntity1);
	when(expenseService.findById(2L)).thenReturn(expenseEntity2);
}

@Test
void shouldMapDtoToEntityAndProcessExpensePaymentsWhenExpensePaymentsAreNotEmpty() {
	billPayment.setExpensePayments(expensePaymentList());

	BillPayment billPaymentEntity = billPaymentMapper.mapBillPayment(billPayment);

	assertNotNull(billPaymentEntity);
	assertEquals(BigDecimal.valueOf(100), billPaymentEntity.getPaymentAmount());
	assertEquals(2, billPaymentEntity.getPaymentMethod());
	assertEquals("123456", billPaymentEntity.getPaymentReference());
	//	assertEquals(1L, billPaymentEntity.getVendor().getId());
	// assertEquals(2, billPaymentEntity.getExpenses().size());
}

@Test
void testShouldThrowRuntimeExceptionWhenExpensePaymentsAreEmpty() {
	List<ExpensePayment> expensePayments = List.of();
	billPayment.setExpensePayments(expensePayments);

	assertThrows(
		NoExpensePaymentsSpecifiedException.class,
		() -> billPaymentMapper.mapBillPayment(billPayment));
}

// @Test
// void
//	testShouldThrowIllegalArgumentExceptionWhenExpensePaymentAmountIsGreaterThanExpenseAmountDue()
// {
//	expensePaymentMap = new HashMap<>();
//	expensePaymentMap.put("1", BigDecimal.valueOf(150));
//	expensePaymentMap.put("2", BigDecimal.valueOf(50));
//	billPayment.setExpensePayments(expensePaymentList());
//	billPayment.setPaymentAmount(BigDecimal.valueOf(100));
//
//	assertThrows(
//		IllegalArgumentException.class, () -> billPaymentMapper.mapBillPayment(billPayment));
// }

// @Test
// void testShouldThrowIllegalArgumentExceptionWhenExpenseIdIsInvalid() {
//	ExpensePayment expensePayment = new ExpensePayment();
//	expensePayment.setExpenseId(3L);
//	expensePayment.setPaymentAmount(BigDecimal.valueOf(50));
//	expensePaymentList().add(expensePayment);
//
//	billPayment.setExpensePayments(expensePaymentList());
//	when(expenseService.findById(3L)).thenReturn(null);
//
//	assertThrows(
//		IllegalArgumentException.class, () -> billPaymentMapper.mapBillPayment(billPayment));
// }

@Test
void testPaymentAmountAndExpensePaymentMismatchShouldThrowIllegalArgumentException() {
	ExpensePayment expensePayment1 = new ExpensePayment();
	expensePayment1.setExpenseId(1L);
	expensePayment1.setPaymentAmount(BigDecimal.valueOf(50));
	ExpensePayment expensePayment2 = new ExpensePayment();
	expensePayment2.setExpenseId(2L);
	expensePayment2.setPaymentAmount(BigDecimal.valueOf(50));
	expensePaymentList().add(expensePayment1);
	expensePaymentList().add(expensePayment2);
	billPayment.setPaymentAmount(BigDecimal.valueOf(150));

	assertThrows(RuntimeException.class, () -> billPaymentMapper.mapBillPayment(billPayment));
}

// @Test
// void testShouldThrowCannotOverpayExceptionWhenPaymentAmountIsGreaterThanTotalAmount() {
//	expensePaymentMap.put("1", BigDecimal.valueOf(50));
//	expensePaymentMap.put("2", BigDecimal.valueOf(50));
//	billPayment.setPaymentAmount(BigDecimal.valueOf(300));
//
//	assertThrows(RuntimeException.class, () -> billPaymentMapper.mapBillPayment(billPayment));
// }

private BillPayment createBillPayment() {
	BillPayment billPayment = new BillPayment();
	billPayment.setPaymentAmount(BigDecimal.valueOf(100));
	billPayment.setPaymentMethod(2);
	billPayment.setPaymentReference("123456");
	// billPayment.setVendorId(1L);
	billPayment.setPaymentDate("2021-09-01 00:00:00.0");
	return billPayment;
}

private List<ExpensePayment> expensePaymentList() {
	ExpensePayment expensePayment1 = new ExpensePayment();
	expensePayment1.setExpenseId(1L);
	expensePayment1.setPaymentAmount(BigDecimal.valueOf(50));
	expensePayment1.setVendorId(1L);

	ExpensePayment expensePayment2 = new ExpensePayment();
	expensePayment2.setExpenseId(2L);
	expensePayment2.setPaymentAmount(BigDecimal.valueOf(50));
	expensePayment2.setVendorId(2L);

	List<ExpensePayment> expensePaymentList = new ArrayList<>();
	expensePaymentList.add(expensePayment1);
	expensePaymentList.add(expensePayment2);

	return expensePaymentList;
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
