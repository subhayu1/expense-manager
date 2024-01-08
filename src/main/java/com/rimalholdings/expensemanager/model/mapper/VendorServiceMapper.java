/* (C)1 */
package com.rimalholdings.expensemanager.model.mapper;

import com.rimalholdings.expensemanager.data.dto.BaseDTOInterface;
import com.rimalholdings.expensemanager.data.dto.VendorDTO;
import com.rimalholdings.expensemanager.data.entity.VendorEntity;
import com.rimalholdings.expensemanager.helper.VendorHelper;
import com.rimalholdings.expensemanager.service.VendorService;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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

@Override
public String saveOrUpdateEntity(BaseDTOInterface dtoInterface) {
	VendorDTO vendorDTO = (VendorDTO) dtoInterface;
	VendorEntity vendorEntity = mapToDTO(vendorDTO);
	VendorEntity savedVendor = vendorService.save(vendorEntity);
	return convertDtoToString(savedVendor);
}

@Override
public VendorEntity mapToDTO(BaseDTOInterface dtoInterface) {
	VendorDTO vendorDTO = (VendorDTO) dtoInterface;

	VendorEntity vendorEntity = new VendorEntity();

	// Map fields from VendorDTO to VendorEntity
	vendorEntity.setId(vendorDTO.getId());
	vendorEntity.setName(vendorDTO.getName());

	if (vendorDTO.getExternalId() == null) {
	vendorEntity.setExternalId(
		VendorHelper.generateVendorId(vendorDTO.getName(), vendorDTO.getZip()));
	} else {
	vendorEntity.setExternalId(vendorDTO.getExternalId());
	}
	vendorEntity.setVendorType(vendorDTO.getVendorType());
	vendorEntity.setAddress1(vendorDTO.getAddress1());
	vendorEntity.setAddress2(vendorDTO.getAddress2());
	vendorEntity.setCity(vendorDTO.getCity());
	vendorEntity.setState(vendorDTO.getState());
	vendorEntity.setZip(vendorDTO.getZip());
	if (vendorDTO.getPhone() != null) {
	vendorEntity.setPhone(VendorHelper.sanitizePhoneNumber(vendorDTO.getPhone()));
	}

	if (vendorDTO.getEmail() != null) {
	if (VendorHelper.isValidEmail(vendorDTO.getEmail())) {
		vendorEntity.setEmail(vendorDTO.getEmail());
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
