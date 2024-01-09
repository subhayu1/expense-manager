package com.rimalholdings.expensemanager.controller;

import com.rimalholdings.expensemanager.data.dto.Expense;
import com.rimalholdings.expensemanager.data.entity.ExpenseEntity;
import com.rimalholdings.expensemanager.model.mapper.ExpenseServiceMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ExpenseControllerTest {
@Mock private ExpenseServiceMapper expenseServiceMapper;
@Mock private Pageable pageable;
@InjectMocks private ExpenseController expenseController;

@BeforeEach
void setUp() {
	MockitoAnnotations.openMocks(this);
}

@Test
void testShouldReturnCreatedExpenseWhenExpenseDTOIsValid() {
	Expense expense = new Expense();
	expense.setId(null);
	when(expenseServiceMapper.saveOrUpdateEntity(expense)).thenReturn("createdExpense");
	ResponseEntity<String> response = expenseController.createExpense(expense);
	assertEquals("createdExpense", response.getBody());
}

@Test
void testShouldReturnAllExpenses() {
	Page<ExpenseEntity> expenses = Page.empty();
	when(expenseServiceMapper.getAllEntities(pageable)).thenReturn(expenses);
	ResponseEntity<Page<ExpenseEntity>> response = expenseController.getAllExpenses(pageable);
	assertEquals(expenses, response.getBody());
}

@Test
void testShouldReturnExpenseWhenExpenseIdIsValid() {
	Long expenseId = 1L;
	when(expenseServiceMapper.getEntity(expenseId)).thenReturn("expense");
	ResponseEntity<String> response = expenseController.getExpense(expenseId);
	assertEquals("expense", response.getBody());
}

@Test
void testShouldReturnUpdatedExpenseWhenExpenseDTOIsValid() {
	Expense expense = new Expense();
	expense.setId(1L);
	when(expenseServiceMapper.saveOrUpdateEntity(expense)).thenReturn("updatedExpense");
	ResponseEntity<String> response = expenseController.updateExpense(expense);
	assertEquals("updatedExpense", response.getBody());
}

@Test
void testShouldReturnDeletedExpenseWhenExpenseIdIsValid() {
	Long expenseId = 1L;
	ResponseEntity<String> response = expenseController.deleteExpense(expenseId);
	assertEquals("Expense deleted", response.getBody());
}
}
