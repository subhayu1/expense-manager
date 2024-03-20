package com.rimalholdings.expensemanager.service;

import java.util.Optional;

import com.rimalholdings.expensemanager.data.dao.ExpenseRepository;
import com.rimalholdings.expensemanager.data.dao.VendorRepository;
import com.rimalholdings.expensemanager.data.entity.ExpenseEntity;
import com.rimalholdings.expensemanager.exception.ObjectNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ExpenseServiceTest {

@Mock private VendorRepository vendorRepository;
@Mock ExpenseRepository expenseRepository;

private ExpenseService expenseService;

@BeforeEach
public void setup() {
	MockitoAnnotations.openMocks(this);
	expenseService = new ExpenseService(expenseRepository, vendorRepository);
}

@Test
public void shouldDeleteExpenseWhenIdExists() {
	Long id = 1L;
	ExpenseEntity expenseEntity = new ExpenseEntity();
	expenseEntity.setId(id); // Set the ID of the ExpenseEntity object
	when(expenseRepository.findById(id)).thenReturn(Optional.of(expenseEntity));

	expenseService.deleteById(id);

	verify(expenseRepository).deleteById(id);
}

@Test
public void shouldThrowExceptionWhenDeletingNonExistingExpense() {
	Long id = 1L;
	when(expenseRepository.findById(id)).thenReturn(Optional.empty());

	assertThrows(ObjectNotFoundException.class, () -> expenseService.deleteById(id));

	verify(expenseRepository, never()).deleteById(anyLong());
}
}
