package com.rimalholdings.expensemanager.service;

import com.rimalholdings.expensemanager.data.dao.BillPaymentRepository;
import com.rimalholdings.expensemanager.data.entity.BillPaymentEntity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BillPaymentServiceTest {

@Mock private BillPaymentRepository billPaymentRepository;

private BillPaymentService billPaymentService;

@BeforeEach
public void setup() {
	MockitoAnnotations.openMocks(this);
	billPaymentService = new BillPaymentService(billPaymentRepository);
}

@Test
public void shouldCallRepositoryWhenSavingBillPayment() {
	BillPaymentEntity billPaymentEntity = new BillPaymentEntity();
	when(billPaymentRepository.save(billPaymentEntity)).thenReturn(billPaymentEntity);

	billPaymentService.save(billPaymentEntity);

	verify(billPaymentRepository).save(billPaymentEntity);
}

@Test
public void shouldCallRepositoryWhenFindingBillPaymentById() {
	Long id = 1L;
	when(billPaymentRepository.findById(id))
		.thenReturn(java.util.Optional.of(new BillPaymentEntity()));

	billPaymentService.findById(id);

	verify(billPaymentRepository).findById(id);
}
}
