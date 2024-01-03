package com.rimalholdings.expensemanager.service;

import com.rimalholdings.expensemanager.Exception.ExceptionConstant;
import com.rimalholdings.expensemanager.Exception.ObjectNotFoundException;
import com.rimalholdings.expensemanager.data.dao.BaseRepository;
import com.rimalholdings.expensemanager.data.dao.VendorRepository;
import com.rimalholdings.expensemanager.data.entity.VendorEntity;
import org.springframework.stereotype.Service;

@Service
public class VendorService extends AbstractEntityService<VendorEntity> {

  public VendorService(BaseRepository<VendorEntity> repository) {
    super(repository);
  }

  public VendorEntity getVendorById(Long vendorId) {
    return getRepository().findById(vendorId)
        .orElseThrow(() -> new ObjectNotFoundException(
            String.format(
                ExceptionConstant.OBJECT_NOT_FOUND, ExceptionConstant.VENDOR, vendorId
            )));
  }

  @Override
  public void deleteById(Long vendorId) {
    VendorEntity vendorEntity = getRepository().findById(vendorId)
        .orElseThrow(() -> new ObjectNotFoundException(
            String.format(
                ExceptionConstant.OBJECT_NOT_FOUND, ExceptionConstant.VENDOR, vendorId
            )));
    getRepository().deleteById(vendorEntity.getId());
  }

  public VendorEntity getVendorByVendorId(String vendorId) {
    return ((VendorRepository) getRepository()).getVendorByExternalId(vendorId)
        .orElseThrow(() -> new ObjectNotFoundException(
            String.format(
                ExceptionConstant.OBJECT_NOT_FOUND, ExceptionConstant.VENDOR, vendorId
            )));
  }

}