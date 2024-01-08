/* (C)1 */
package com.rimalholdings.expensemanager.controller;

import com.rimalholdings.expensemanager.data.dto.Vendor;
import com.rimalholdings.expensemanager.data.entity.VendorEntity;
import com.rimalholdings.expensemanager.exception.DuplicateIdException;
import com.rimalholdings.expensemanager.exception.IdNotSuppliedException;
import com.rimalholdings.expensemanager.model.mapper.VendorServiceMapper;

import java.sql.SQLIntegrityConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/vendor")
@Slf4j(topic = "VendorController")
public class VendorController implements APIControllerInterface {

private final VendorServiceMapper vendorMapper;

public VendorController(VendorServiceMapper vendorMapper) {
	this.vendorMapper = vendorMapper;
}

@PostMapping("/")
public ResponseEntity<String> createVendor(@RequestBody Vendor vendor)
	throws DuplicateIdException {
	log.info("Creating new vendor: {}", vendor);
	String createdVendor = vendorMapper.saveOrUpdateEntity(vendor);
	return ResponseEntity.status(HttpStatus.CREATED).body(createdVendor);
}

@GetMapping("/{vendorId}")
public ResponseEntity<String> getVendor(@PathVariable Long vendorId) {
	String vendor = vendorMapper.getEntity(vendorId);
	return ResponseEntity.ok(vendor);
}

@GetMapping("/")
public ResponseEntity<Page<VendorEntity>> getAllVendors(@ParameterObject Pageable pageable) {
	Page<VendorEntity> vendors = vendorMapper.getAllEntities(pageable);
	return ResponseEntity.ok(vendors);
}

@PutMapping("/")
public ResponseEntity<String> updateVendor(@RequestBody Vendor vendor)
	throws DuplicateIdException, IdNotSuppliedException {
	if (vendor.getId() == null) {
	throw new IdNotSuppliedException("Vendor ID not supplied");
	}
	String updatedVendor = vendorMapper.saveOrUpdateEntity(vendor);
	return ResponseEntity.ok(updatedVendor);
}

@DeleteMapping("/{vendorId}")
public ResponseEntity<String> deleteVendor(@PathVariable Long vendorId) {
	log.info("Deleting vendor with ID {}", vendorId);
		vendorMapper.deleteEntity(vendorId);

	return ResponseEntity.ok("Vendor deleted");
}
}
