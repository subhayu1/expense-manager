/* (C)1 */
package com.rimalholdings.expensemanager.model.mapper;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.rimalholdings.expensemanager.data.dto.BillPaymentDTO;
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

class BillPaymentMapperTest {

@Mock private BillPaymentService billPaymentService;

@Mock private ExpenseService expenseService;

private BillPaymentServiceMapper billPaymentMapper;

private final ObjectMapper objectMapper = new ObjectMapper();

@BeforeEach
void setUp() {
	MockitoAnnotations.openMocks(this);
	billPaymentMapper =
		new BillPaymentServiceMapper(objectMapper, billPaymentService, expenseService);
}

@Test
void testMapToDTO() {
	// Arrange
	BillPaymentDTO billPaymentDTO = new BillPaymentDTO();
	billPaymentDTO.setPaymentAmount(BigDecimal.valueOf(100));
	billPaymentDTO.setPaymentMethod("Credit Card");
	billPaymentDTO.setPaymentReference("123456");
	billPaymentDTO.setVendorId(1L);

	Map<String, BigDecimal> expensePaymentMap = new HashMap<>();
	expensePaymentMap.put("1", BigDecimal.valueOf(50));
	expensePaymentMap.put("2", BigDecimal.valueOf(50));
	billPaymentDTO.setExpensePayments(expensePaymentMap);

	ExpenseEntity expenseEntity1 = new ExpenseEntity();
	expenseEntity1.setId(1L);
	expenseEntity1.setAmountDue(BigDecimal.valueOf(100));
	expenseEntity1.setTotalAmount(BigDecimal.valueOf(100));

	ExpenseEntity expenseEntity2 = new ExpenseEntity();
	expenseEntity2.setId(2L);
	expenseEntity2.setAmountDue(BigDecimal.valueOf(200));
	expenseEntity2.setTotalAmount(BigDecimal.valueOf(200));

	when(expenseService.findById(1L)).thenReturn(expenseEntity1);
	when(expenseService.findById(2L)).thenReturn(expenseEntity2);

	// Act
	BillPaymentEntity billPaymentEntity = billPaymentMapper.mapToDTO(billPaymentDTO);

	// Assert
	assertEquals(BigDecimal.valueOf(100), billPaymentEntity.getPaymentAmount());
	assertEquals("Credit Card", billPaymentEntity.getPaymentMethod());
	assertEquals("123456", billPaymentEntity.getPaymentReference());

	VendorEntity vendorEntity = billPaymentEntity.getVendor();
	assertNotNull(vendorEntity);
	assertEquals(1L, vendorEntity.getId());

	assertEquals(2, billPaymentEntity.getExpenses().size());

	ExpenseEntity mappedExpenseEntity1 = billPaymentEntity.getExpenses().get(0);
	assertEquals(1L, mappedExpenseEntity1.getId());
	assertEquals(BigDecimal.valueOf(50), mappedExpenseEntity1.getPaymentAmount());
	assertEquals(BigDecimal.valueOf(50), mappedExpenseEntity1.getAmountDue());

	ExpenseEntity mappedExpenseEntity2 = billPaymentEntity.getExpenses().get(1);
	assertEquals(2L, mappedExpenseEntity2.getId());
	assertEquals(BigDecimal.valueOf(50), mappedExpenseEntity2.getPaymentAmount());
	assertEquals(BigDecimal.valueOf(150), mappedExpenseEntity2.getAmountDue());

	verify(expenseService, times(2)).findById(anyLong());
}

@Test
void testSaveOrUpdateEntity() {
	// Your test code here

	// Arrange
	BillPaymentDTO billPaymentDTO = new BillPaymentDTO();
	billPaymentDTO.setPaymentAmount(BigDecimal.valueOf(100));
	billPaymentDTO.setPaymentMethod("Credit Card");
	billPaymentDTO.setPaymentReference("123456");
	billPaymentDTO.setVendorId(1L);

	BillPaymentEntity billPaymentEntity = new BillPaymentEntity();
	billPaymentEntity.setPaymentAmount(BigDecimal.valueOf(100));
	billPaymentEntity.setPaymentMethod("Credit Card");
	billPaymentEntity.setPaymentReference("123456");
	billPaymentEntity.setVendor(new VendorEntity());
	billPaymentEntity.getVendor().setId(1L);

	String billPaymentEntityString = billPaymentMapper.convertDtoToString(billPaymentEntity);

	when(billPaymentService.save(any(BillPaymentEntity.class))).thenReturn(billPaymentEntity);

	// Act
	String result = billPaymentMapper.saveOrUpdateEntity(billPaymentDTO);

	// Assert
	assertNotNull(result);
	assertEquals(billPaymentEntityString, result);

	verify(billPaymentService, times(1)).save(any(BillPaymentEntity.class));
}
}
