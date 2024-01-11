/* (C)1 */
package com.rimalholdings.expensemanager.model.mapper;

import java.util.List;

import com.rimalholdings.expensemanager.data.dto.BaseDTOInterface;
import com.rimalholdings.expensemanager.data.dto.Vendor;
import com.rimalholdings.expensemanager.data.dto.sync.VendorResponse;
import com.rimalholdings.expensemanager.data.entity.VendorEntity;
import com.rimalholdings.expensemanager.exception.UpdateNotAllowedException;
import com.rimalholdings.expensemanager.helper.VendorHelper;
import com.rimalholdings.expensemanager.service.VendorService;
import com.rimalholdings.expensemanager.util.DateTimeUtil;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j(topic = "VendorServiceMapper")
public class VendorServiceMapper extends AbstractServiceMapper<VendorEntity> {

private final VendorService vendorService;

public VendorServiceMapper(VendorService vendorService, ObjectMapper objectMapper) {
	super(objectMapper);
	this.vendorService = vendorService;
}

// check the dto to see which fields are empty and if they are empty, then use the existing
// entity's values for those fields

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
	VendorEntity vendorEntity = getOrCreateVendorEntity(vendor);

	updateEntityIfNotNull(vendorEntity, vendor);

	return vendorEntity;
}

private VendorEntity getOrCreateVendorEntity(Vendor vendor) {
	VendorEntity vendorEntity;
	vendorEntity = getEntityForUpdate(vendor.getId(), vendor.getIntegrationId());
	if (vendorEntity == null) {
	vendorEntity = new VendorEntity();
	vendorEntity.setCreatedDate(DateTimeUtil.getCurrentTimeInUTC());
	vendorEntity.setUpdatedDate(DateTimeUtil.getCurrentTimeInUTC());
	}
	// its returning a null vendorEntity here if id is null but integId is not null. Maybe we need
	// to check for integId first or implement a find id by integId method

	return vendorEntity;
}

private void updateEntityIfNotNull(VendorEntity vendorEntity, Vendor vendor) {
	setIfNotNull(vendor::getId, vendorEntity::setId);
	setIfNotNull(vendor::getName, vendorEntity::setName);
	setIfNotNull(vendor::getVendorType, vendorEntity::setVendorType);
	setIfNotNull(vendor::getAddress1, vendorEntity::setAddress1);
	setIfNotNull(vendor::getAddress2, vendorEntity::setAddress2);
	setIfNotNull(vendor::getCity, vendorEntity::setCity);
	setIfNotNull(vendor::getState, vendorEntity::setState);
	setIfNotNull(vendor::getZip, vendorEntity::setZip);
	setIfNotNull(vendor::getCountry, vendorEntity::setCountry);
	setIfNotNull(vendor::getIntegrationId, vendorEntity::setIntegrationId);
	setIfNotNull(vendor::getOrgId, vendorEntity::setExternalOrgId);

	if (vendor.getPhone() != null) {
	vendorEntity.setPhone(VendorHelper.sanitizePhoneNumber(vendor.getPhone()));
	}

	if (vendor.getEmail() != null && VendorHelper.isValidEmail(vendor.getEmail())) {
	vendorEntity.setEmail(vendor.getEmail());
	}

	handleExternalId(vendorEntity, vendor);
}

private void handleExternalId(VendorEntity vendorEntity, Vendor vendor) {
	if (vendor.getVendorNumber() == null
		&& vendor.getName() != null
		&& vendor.getZip() != null
		&& vendor.getNumber() == null) {
	vendorEntity.setExternalId(VendorHelper.generateVendorId(vendor.getName(), vendor.getZip()));
	} else if (vendor.getVendorNumber() != null) {
	vendorEntity.setExternalId(vendor.getVendorNumber());
	} else if (vendor.getNumber() != null) {
	vendorEntity.setExternalId(vendor.getNumber());
	}
	// No else part, as we don't update externalId if it's null in DTO
}

@Override
public void deleteEntity(Long id) {
	VendorEntity vendorEntity = getEntityForUpdate(id);
	if (vendorEntity != null && vendorEntity.getExpenses().isEmpty()) {
	vendorService.deleteById(id);
	} else {
	throw new UpdateNotAllowedException(
		"Vendor cannot be deleted because it is already associated with an expense");
	}
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

public VendorEntity getEntityForUpdate(Long id, String integrationId) {
	VendorEntity vendorEntity;
	try {
	return vendorService.findById(id);
	} catch (Exception e) {
	log.info("id is null, trying to find vendor by integrationId");
	// catch the exception, swallow it and then try to find the vendor by integrationId
	vendorEntity = vendorService.findByIntegrationId(integrationId);
	}
	return vendorEntity;
}

@Override
public VendorEntity getEntityForUpdate(Long id) {
	try {
	return vendorService.findById(id);
	} catch (Exception e) {
	return null;
	}
}

@Override
public String getEntityFromSyncService(Integer externalOrgId) {
	return null;
}

public void fetchAndSaveVendors(Integer externalOrgId) {
	RestTemplate restTemplate = new RestTemplate();

	UriComponentsBuilder builder =
		UriComponentsBuilder.fromHttpUrl("http://localhost:8090/api/v1/sync/get/vendors")
			.queryParam("organizationId", externalOrgId);

	try {
	restTemplate.getForEntity(builder.toUriString(), VendorResponse.class);
	} catch (NullPointerException npe) {
	throw new RuntimeException("No vendors found for organizationId: " + externalOrgId);
	}
	ResponseEntity<VendorResponse> response =
		restTemplate.getForEntity(builder.toUriString(), VendorResponse.class);
	if (response.getBody() == null) {
	throw new RuntimeException("No vendors found for organizationId: " + externalOrgId);
	}
	System.out.println(response.getBody());

	List<Vendor> vendors = response.getBody().getVendors();
	if (vendors.isEmpty()) {
	throw new RuntimeException("No vendors found for organizationId: " + externalOrgId);
	}

	for (Vendor vendor : vendors) {
	saveOrUpdateEntity(vendor);
	}
}
}
