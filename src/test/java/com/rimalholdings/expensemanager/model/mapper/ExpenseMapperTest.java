/* (C)1 */
package com.rimalholdings.expensemanager.model.mapper;

import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Timestamp;

import com.rimalholdings.expensemanager.Exception.CannotOverpayExpenseException;
import com.rimalholdings.expensemanager.data.dto.BaseDTOInterface;
import com.rimalholdings.expensemanager.data.dto.ExpenseDTO;
import com.rimalholdings.expensemanager.data.entity.ExpenseEntity;
import com.rimalholdings.expensemanager.service.ExpenseService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExpenseMapperTest {

private ExpenseServiceMapper expenseMapper;

@Mock private ExpenseService expenseService;
private final ObjectMapper objectMapper = new ObjectMapper();

@BeforeEach
void setUp() {
	MockitoAnnotations.openMocks(this);
	expenseMapper = new ExpenseServiceMapper(objectMapper, expenseService);
}

@Test
void mapToDTO_ValidExpenseDTO_ReturnsExpenseEntity() {
	ExpenseDTO expenseDTO = new ExpenseDTO();
	expenseDTO.setDueDate("2022-01-01 12:00:00");
	BigDecimal totalAmount = BigDecimal.valueOf(100).round(new MathContext(2));
	BigDecimal paymentAmount = BigDecimal.valueOf(50).round(new MathContext(2));
	expenseDTO.setTotalAmount(totalAmount);
	expenseDTO.setPaymentAmount(paymentAmount);
	expenseDTO.setId(1L);
	expenseDTO.setDueDate("2022-01-01 12:00:00");
	expenseDTO.setVendorId(1L);
	expenseDTO.setDescription("Test Expense");
	// Act
	ExpenseEntity expenseEntity = expenseMapper.mapToDTO(expenseDTO);

	// Assert
	assertNotNull(expenseEntity);
	assertEquals(expenseDTO.getId(), expenseEntity.getId());
	assertEquals(Timestamp.valueOf(expenseDTO.getDueDate()), expenseEntity.getDueDate());
	assertEquals(expenseDTO.getVendorId(), expenseEntity.getVendor().getId());
	assertEquals(expenseDTO.getTotalAmount(), expenseEntity.getTotalAmount());
	assertEquals(expenseDTO.getDescription(), expenseEntity.getDescription());
}

@Test
void testCalculateValidAmountDue() {
	// Arrange
	BigDecimal totalAmount = BigDecimal.valueOf(100.0);
	BigDecimal paymentAmount = BigDecimal.valueOf(50.0);

	// Act
	BigDecimal result = expenseMapper.calculateAmountDue(totalAmount, paymentAmount);

	// Assert
	assertEquals(BigDecimal.valueOf(50.0), result);
}

@Test
void testCalculateInvalidAmountDue() {
	// Arrange
	BigDecimal totalAmount = BigDecimal.valueOf(100.0);
	BigDecimal paymentAmount = BigDecimal.valueOf(150.0);

	// Act & Assert
	assertThrows(
		CannotOverpayExpenseException.class,
		() -> expenseMapper.calculateAmountDue(totalAmount, paymentAmount));
}

@Test
void mapToDTO_InvalidDTOType_ThrowsIllegalArgumentException() {
	// Arrange
	BaseDTOInterface invalidDTO = mock(BaseDTOInterface.class);

	// Act & Assert
	assertThrows(IllegalArgumentException.class, () -> expenseMapper.mapToDTO(invalidDTO));
}

@Test
void deleteEntity_ValidId_CallsExpenseServiceDeleteById() {
	// Arrange
	Long id = 1L;

	// Act
	expenseMapper.deleteEntity(id);

	// Assert
	verify(expenseService, times(1)).deleteById(id);
}

@Test
void getEntity_ValidId_ReturnsConvertedDtoToString() {
	// Arrange
	Long id = 1L;
	ExpenseEntity expenseEntity = new ExpenseEntity();
	when(expenseService.findById(id)).thenReturn(expenseEntity);

	// Act
	String result = expenseMapper.getEntity(id);

	// Assert
	assertEquals(expenseMapper.convertDtoToString(expenseEntity), result);
}

@Test
void saveOrUpdateEntity_ValidExpenseDTO_ReturnsConvertedDtoToString() {

	ExpenseDTO expenseDTO = new ExpenseDTO();
	expenseDTO.setDueDate("2022-01-01 12:00:00");
	BigDecimal totalAmount = BigDecimal.valueOf(100).round(new MathContext(2));
	BigDecimal paymentAmount = BigDecimal.valueOf(50).round(new MathContext(2));
	expenseDTO.setTotalAmount(totalAmount);
	expenseDTO.setPaymentAmount(paymentAmount);
	ExpenseEntity expenseEntity = new ExpenseEntity();

	expenseEntity.setDueDate(Timestamp.valueOf(expenseDTO.getDueDate()));
	when(expenseService.save(any(ExpenseEntity.class))).thenReturn(expenseEntity);

	// Act
	String result = expenseMapper.saveOrUpdateEntity(expenseDTO);

	// Assert
	assertEquals(expenseMapper.convertDtoToString(expenseEntity), result);
}

@Test
void saveOrUpdateEntity_InvalidDTOType_ThrowsIllegalArgumentException() {
	// Arrange
	BaseDTOInterface invalidDTO = mock(BaseDTOInterface.class);

	// Act & Assert
	assertThrows(
		IllegalArgumentException.class, () -> expenseMapper.saveOrUpdateEntity(invalidDTO));
}

@Test
void getAllEntities_ValidPageable_ReturnsExpenseServiceFindAll() {
	// Arrange
	Pageable pageable = mock(Pageable.class);
	Page<ExpenseEntity> expectedPage = mock(Page.class);
	when(expenseService.findAll(pageable)).thenReturn(expectedPage);

	// Act
	Page<ExpenseEntity> result = expenseMapper.getAllEntities(pageable);

	// Assert
	assertEquals(expectedPage, result);
}
}
