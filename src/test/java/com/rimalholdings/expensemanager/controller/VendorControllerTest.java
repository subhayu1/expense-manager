package com.rimalholdings.expensemanager.controller;

import com.rimalholdings.expensemanager.data.dto.VendorDTO;
import com.rimalholdings.expensemanager.data.entity.VendorEntity;
import com.rimalholdings.expensemanager.exception.DuplicateIdException;
import com.rimalholdings.expensemanager.model.mapper.VendorServiceMapper;

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

class VendorControllerTest {
@Mock private VendorServiceMapper vendorMapper;
@Mock Pageable pageable;
@InjectMocks private VendorController vendorController;

@BeforeEach
void setUp() {
	MockitoAnnotations.openMocks(this);
}

@Test
void testShouldReturnCreatedVendorWhenVendorDTOIsValid() {
	VendorDTO vendorDTO = new VendorDTO();
	vendorDTO.setId(null);
	when(vendorMapper.saveOrUpdateEntity(vendorDTO)).thenReturn("createdVendor");
	ResponseEntity<String> responseEntity = null;
	try {
	responseEntity = vendorController.createVendor(vendorDTO);
	} catch (DuplicateIdException e) {
	throw new RuntimeException(e);
	}
	assertEquals("createdVendor", responseEntity.getBody());
}

@Test
void shouldThrowDuplicateIdExceptionWhenVendorDTOHasId() throws DuplicateIdException {
	VendorDTO vendorDTO = new VendorDTO();
	vendorDTO.setId(1L);
	when(vendorMapper.saveOrUpdateEntity(vendorDTO))
		.thenThrow(new DuplicateIdException("Expected message"));

	DuplicateIdException thrown =
		assertThrows(
			DuplicateIdException.class,
			() -> vendorController.createVendor(vendorDTO),
			"Expected createVendor() to throw, but it didn't");

	// You can then make assertions about the exception, if needed.
	// For example, you can check the message of the exception.
	assertTrue(thrown.getMessage().contains("Expected message"));
}

@Test
void testShouldReturnVendorWhenVendorIdIsValid() {
	when(vendorMapper.getEntity(1L)).thenReturn("vendor");
	ResponseEntity<String> responseEntity = vendorController.getVendor(1L);
	assertEquals("vendor", responseEntity.getBody());
}

@Test
void testShouldReturnAllVendors() {
	when(vendorMapper.getAllEntities(pageable)).thenReturn(null);
	ResponseEntity<Page<VendorEntity>> responseEntity = vendorController.getAllVendors(pageable);
	assertNull(responseEntity.getBody());
}
}
