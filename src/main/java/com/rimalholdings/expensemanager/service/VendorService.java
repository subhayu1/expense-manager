package com.rimalholdings.expensemanager.service;

import com.rimalholdings.expensemanager.Exception.DuplicateIdException;
import com.rimalholdings.expensemanager.Exception.ExceptionConstant;
import com.rimalholdings.expensemanager.Exception.ObjectNotFoundException;
import com.rimalholdings.expensemanager.data.dao.VendorRepository;
import com.rimalholdings.expensemanager.data.entity.VendorEntity;
import java.sql.SQLIntegrityConstraintViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VendorService {

  private final VendorRepository vendorRepository;

  public VendorService(VendorRepository vendorRepository) {
    this.vendorRepository = vendorRepository;
  }

  @Transactional
  public VendorEntity save(VendorEntity vendorEntity) throws DuplicateIdException {
    return vendorRepository.save(vendorEntity);
  }

  public VendorEntity getVendorById(Long vendorId) {
    return vendorRepository.findById(vendorId)
        .orElseThrow(() -> new ObjectNotFoundException(
            String.format(
                ExceptionConstant.OBJECT_NOT_FOUND,ExceptionConstant.VENDOR, vendorId
            )));
  }

  public void delete(Long vendorId) {
    VendorEntity vendorEntity = vendorRepository.findById(vendorId)
        .orElseThrow(() -> new ObjectNotFoundException(
        String.format(
            ExceptionConstant.OBJECT_NOT_FOUND,ExceptionConstant.VENDOR, vendorId
        )));
    vendorRepository.deleteById(vendorEntity.getId());
  }
}
