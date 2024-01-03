package com.rimalholdings.expensemanager.model.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rimalholdings.expensemanager.Exception.DuplicateIdException;
import com.rimalholdings.expensemanager.data.dto.BaseDTOInterface;
import com.rimalholdings.expensemanager.data.entity.VendorEntity;
import com.rimalholdings.expensemanager.helper.VendorHelper;
import com.rimalholdings.expensemanager.data.dto.VendorDTO;
import com.rimalholdings.expensemanager.service.VendorService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "VendorMapper")
public class VendorMapper extends AbstractMapper<VendorEntity> {

  private final VendorService vendorService;


  public VendorMapper(VendorService vendorService, ObjectMapper objectMapper) {
    super(objectMapper);
    this.vendorService = vendorService;
  }

  @Override
 public String saveOrUpdateEntity(BaseDTOInterface dtoInterface) {
    if(!(dtoInterface instanceof VendorDTO vendorDTO)){
      throw new IllegalArgumentException("Invalid DTO type");
    }
  VendorEntity vendorEntity = mapToDTO(vendorDTO);
   VendorEntity savedVendor = vendorService.save(vendorEntity);
   return convertDtoToString(savedVendor);
 }

@Override
public VendorEntity mapToDTO(BaseDTOInterface dtoInterface) {
  if (!(dtoInterface instanceof VendorDTO vendorDTO)) {
    throw new IllegalArgumentException("Invalid DTO type");
  }

  VendorEntity vendorEntity = new VendorEntity();

  // Map fields from VendorDTO to VendorEntity
  vendorEntity.setId(vendorDTO.getId());
  vendorEntity.setName(vendorDTO.getName());

  if (vendorDTO.getExternalId()==null) {
    vendorEntity.setExternalId(VendorHelper.generateVendorId(vendorDTO.getName(),vendorDTO.getZip()));
  }else {
    vendorEntity.setExternalId(vendorDTO.getExternalId());
  }
  vendorEntity.setVendorType(vendorDTO.getVendorType());
  vendorEntity.setAddress1(vendorDTO.getAddress1());
  vendorEntity.setAddress2(vendorDTO.getAddress2());
  vendorEntity.setCity(vendorDTO.getCity());
  vendorEntity.setState(vendorDTO.getState());
  vendorEntity.setZip(vendorDTO.getZip());
  vendorEntity.setPhone(VendorHelper.sanitizePhoneNumber(vendorDTO.getPhone()));

  if (VendorHelper.isValidEmail(vendorDTO.getEmail())) {
    vendorEntity.setEmail(vendorDTO.getEmail());
  }

  return vendorEntity;
}

@Override
  public void deleteEntity(Long id) {
    vendorService.deleteById(id);
  }


  @Override
  public String getEntity(Long id) {
    VendorEntity vendorEntity = vendorService.getVendorById(id);
    return convertDtoToString(vendorEntity);
  }
  @Override
  public List<VendorEntity> getAllEntities() {
    List<VendorEntity> vendorEntities = vendorService.findAll();
    return convertDtoToString(vendorEntities);
  }

  public VendorEntity getVendorDetails(Long id) {
    return vendorService.getVendorById(id);
  }
  private boolean isCreateOperation(VendorDTO vendor) {
    return vendor.getId() == null;
  }

}