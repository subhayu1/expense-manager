package com.rimalholdings.expensemanager.service;

import java.util.Optional;

import com.rimalholdings.expensemanager.Exception.ObjectNotFoundException;
import com.rimalholdings.expensemanager.data.dao.BaseRepository;
import com.rimalholdings.expensemanager.data.entity.VendorEntity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class VendorServiceTest {

@Mock private BaseRepository<VendorEntity> repository;

private VendorService vendorService;

@BeforeEach
public void setup() {
	MockitoAnnotations.openMocks(this);
	vendorService = new VendorService(repository);
}

@Test
public void shouldDeleteVendorWhenIdExists() {
	VendorEntity vendorEntity = new VendorEntity();
	Long id = 1L;
	vendorEntity.setId(id);
	when(repository.findById(id)).thenReturn(Optional.of(vendorEntity));

	vendorService.deleteById(id);

	verify(repository).deleteById(id);
}

@Test
public void shouldThrowExceptionWhenDeletingNonExistingVendor() {
	Long id = 1L;
	when(repository.findById(id)).thenReturn(Optional.empty());

	assertThrows(ObjectNotFoundException.class, () -> vendorService.deleteById(id));

	verify(repository, never()).deleteById(anyLong());
}
}
