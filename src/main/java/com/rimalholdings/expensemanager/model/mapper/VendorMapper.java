package com.rimalholdings.expensemanager.model.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rimalholdings.expensemanager.Exception.DuplicateIdException;
import com.rimalholdings.expensemanager.data.entity.VendorEntity;
import com.rimalholdings.expensemanager.helper.VendorHelper;
import com.rimalholdings.expensemanager.data.dto.VendorDTO;
import com.rimalholdings.expensemanager.service.VendorService;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

@Service
public class VendorMapper extends AbstractMapper<VendorEntity> {

  private final VendorService vendorService;
  private final ObjectMapper objectMapper;

  private final Logger logger = Logger.getLogger(VendorMapper.class.getName());


  public VendorMapper(VendorService vendorService, ObjectMapper objectMapper) {
    super(vendorService, objectMapper);
    this.vendorService = vendorService;
    this.objectMapper = objectMapper;
  }

 public String saveOrUpdateVendor(VendorDTO vendorDTO) throws DuplicateIdException {
  VendorEntity vendorEntity = mapToDTO(vendorDTO);
  try {
    VendorEntity savedVendor = vendorService.save(vendorEntity);
    return convertDtoToString(savedVendor);
  } catch (SQLIntegrityConstraintViolationException e) {
    String errorMessage = "Error saving VendorDTO with ID " + vendorDTO.getId() + ": " + e.getMessage();
    logger.info(errorMessage);
    throw new DuplicateIdException(errorMessage);
  }
}

protected VendorEntity mapToDTO(VendorDTO vendor) {
  VendorEntity vendorEntity = new VendorEntity();
  vendorEntity.setId(vendor.getId());

  if(isCreateOperation(vendor)) {
    vendorEntity.setVendorId(VendorHelper.generateVendorId(vendor.getName()));
  } else {
    vendorEntity.setVendorId(getVendorDetails(vendor.getId()).getVendorId());
  }

  vendorEntity.setVendorType(vendor.getVendorType());
  vendorEntity.setName(vendor.getName());
  vendorEntity.setAddress1(vendor.getAddress1());
  vendorEntity.setAddress2(vendor.getAddress2());
  vendorEntity.setCity(vendor.getCity());
  vendorEntity.setState(vendor.getState());
  vendorEntity.setZip(vendor.getZip());

  vendorEntity.setPhone(VendorHelper
      .sanitizePhoneNumber(vendor.getPhone()));

  if(VendorHelper.isValidEmail(vendor.getEmail())) {
    vendorEntity.setEmail(vendor.getEmail());
  }

  return vendorEntity;
}

  public void deleteVendor(Long id) {
    vendorService.delete(id);
  }


  public String getVendor(Long id) {
    VendorEntity vendorEntity = vendorService.getVendorById(id);
    return convertDtoToString(vendorEntity);
  }

  public VendorEntity getVendorDetails(Long id) {
    return vendorService.getVendorById(id);
  }
  private boolean isCreateOperation(VendorDTO vendor) {
    return vendor.getId() == null;
  }

}