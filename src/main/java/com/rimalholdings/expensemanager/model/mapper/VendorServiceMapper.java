/* (C)1 */
package com.rimalholdings.expensemanager.model.mapper;

import com.rimalholdings.expensemanager.data.dto.BaseDTOInterface;
import com.rimalholdings.expensemanager.data.dto.Vendor;
import com.rimalholdings.expensemanager.data.entity.VendorEntity;
import com.rimalholdings.expensemanager.exception.UpdateNotAllowedException;
import com.rimalholdings.expensemanager.helper.VendorHelper;
import com.rimalholdings.expensemanager.service.VendorService;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.SQLIntegrityConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "VendorServiceMapper")
public class VendorServiceMapper extends AbstractServiceMapper<VendorEntity> {

private final VendorService vendorService;

public VendorServiceMapper(VendorService vendorService, ObjectMapper objectMapper) {
	super(objectMapper);
	this.vendorService = vendorService;
}
//check the dto to see which fields are empty and if they are empty, then use the existing entity's values for those fields

@Override
public String saveOrUpdateEntity(BaseDTOInterface dtoInterface) {
	Vendor vendor = (Vendor) dtoInterface;
	VendorEntity vendorEntity = mapToDTO(vendor);
	VendorEntity savedVendor = vendorService.save(vendorEntity);
	return convertDtoToString(savedVendor);
}

@Override
public VendorEntity mapToDTO(BaseDTOInterface dtoInterface) {
	Vendor vendor = (Vendor) dtoInterface;

	VendorEntity vendorEntity = new VendorEntity();

	// Map fields from VendorDTO to VendorEntity
	vendorEntity.setId(vendor.getId());
	vendorEntity.setName(vendor.getName());

	if (vendor.getExternalId() == null) {
	vendorEntity.setExternalId(VendorHelper.generateVendorId(vendor.getName(), vendor.getZip()));
	} else {
	vendorEntity.setExternalId(vendor.getExternalId());
	}
	vendorEntity.setVendorType(vendor.getVendorType());
	vendorEntity.setAddress1(vendor.getAddress1());
	vendorEntity.setAddress2(vendor.getAddress2());
	vendorEntity.setCity(vendor.getCity());
	vendorEntity.setState(vendor.getState());
	vendorEntity.setZip(vendor.getZip());
	if (vendor.getPhone() != null) {
	vendorEntity.setPhone(VendorHelper.sanitizePhoneNumber(vendor.getPhone()));
	}

	if (vendor.getEmail() != null) {
	if (VendorHelper.isValidEmail(vendor.getEmail())) {
		vendorEntity.setEmail(vendor.getEmail());
	}
	}

	return vendorEntity;
}

@Override
public void deleteEntity(Long id) {
	vendorService.deleteById(id);

}

@Override
public String getEntity(Long id) {
	VendorEntity vendorEntity = vendorService.findById(id);
	return convertDtoToString(vendorEntity);
}

@Override
public Page<VendorEntity> getAllEntities(Pageable pageable) {
	return vendorService.findAll(pageable);
}
}
