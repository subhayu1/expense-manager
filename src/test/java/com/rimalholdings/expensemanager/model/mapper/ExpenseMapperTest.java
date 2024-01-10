/* (C)1 */
package com.rimalholdings.expensemanager.model.mapper;

import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Timestamp;

import com.rimalholdings.expensemanager.data.dto.BaseDTOInterface;
import com.rimalholdings.expensemanager.data.dto.Expense;
import com.rimalholdings.expensemanager.data.entity.ExpenseEntity;
import com.rimalholdings.expensemanager.exception.UpdateNotAllowedException;
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
	Expense expense = new Expense();
	expense.setDueDate("2022-01-01 12:00:00");
	BigDecimal totalAmount = BigDecimal.valueOf(100).round(new MathContext(2));
	expense.setTotalAmount(totalAmount);
	expense.setId(1L);
	expense.setDueDate("2022-01-01 12:00:00");
	expense.setVendorId(1L);
	expense.setDescription("Test Expense");
	// Act
	ExpenseEntity expenseEntity = expenseMapper.mapToDTO(expense);

	// Assert
	assertNotNull(expenseEntity);
	assertEquals(expense.getId(), expenseEntity.getId());
	assertEquals(Timestamp.valueOf(expense.getDueDate()), expenseEntity.getDueDate());
	assertEquals(expense.getVendorId(), expenseEntity.getVendor().getId());
	assertEquals(expense.getTotalAmount(), expenseEntity.getTotalAmount());
	assertEquals(expense.getDescription(), expenseEntity.getDescription());
}

@Test
void mapToDTO_InvalidDTOType_ThrowsIllegalArgumentException() {
	// Arrange
	BaseDTOInterface invalidDTO = mock(BaseDTOInterface.class);

	// Act & Assert
	assertThrows(ClassCastException.class, () -> expenseMapper.mapToDTO(invalidDTO));
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

	Expense expense = new Expense();
	expense.setDueDate("2022-01-01 12:00:00");
	BigDecimal totalAmount = BigDecimal.valueOf(100).round(new MathContext(2));
	expense.setTotalAmount(totalAmount);
	ExpenseEntity expenseEntity = new ExpenseEntity();

	expenseEntity.setDueDate(Timestamp.valueOf(expense.getDueDate()));
	when(expenseService.save(any(ExpenseEntity.class))).thenReturn(expenseEntity);

	// Act
	String result = expenseMapper.saveOrUpdateEntity(expense);

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

@Test
void testUpdatingPartiallyOrFullyPaidExpenseThrowsUpdateNotAllowedException() {
	// Arrange
	ExpenseEntity expenseEntity = new ExpenseEntity();
	Expense expense = new Expense();
	expense.setId(1L);
	expense.setDueDate("2022-01-01 12:00:00");
	BigDecimal totalAmount = BigDecimal.valueOf(100).round(new MathContext(2));
	expense.setTotalAmount(totalAmount);
	expense.setVendorId(1L);
	expense.setDescription("Test Expense");

	expenseEntity.setPaymentStatus(1);
	expenseEntity.setAmountDue(BigDecimal.valueOf(100.00));
	when(expenseMapper.getEntityForUpdate(expense.getId())).thenReturn(expenseEntity);
	when(expenseService.save(expenseEntity)).thenReturn(expenseEntity);

	// Act & Assert
	assertThrows(UpdateNotAllowedException.class, () -> expenseMapper.saveOrUpdateEntity(expense));
}

@Test
void testDeletingPartiallyOrFullyPaidExpenseThrowsUpdateNotAllowedException() {
	// Arrange
	ExpenseEntity expenseEntity = new ExpenseEntity();
	Expense expense = new Expense();
	expense.setId(1L);
	expense.setDueDate("2022-01-01 12:00:00");
	BigDecimal totalAmount = BigDecimal.valueOf(100).round(new MathContext(2));
	expense.setTotalAmount(totalAmount);
	expense.setVendorId(1L);
	expenseEntity.setPaymentStatus(1);
	expense.setDescription("Test Expense");
	when(expenseService.findById(expense.getId())).thenReturn(expenseEntity);
	assertThrows(
		UpdateNotAllowedException.class, () -> expenseMapper.deleteEntity(expense.getId()));
}
}
