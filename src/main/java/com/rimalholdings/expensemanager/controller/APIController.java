package com.rimalholdings.expensemanager.controller;


import com.rimalholdings.expensemanager.Exception.DuplicateIdException;
import com.rimalholdings.expensemanager.Exception.IdNotSuppliedException;
import com.rimalholdings.expensemanager.model.VendorDTO;
import com.rimalholdings.expensemanager.model.mapper.VendorMapper;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/api/v1")
@Slf4j(topic = "APIController")
public class APIController {

  private final VendorMapper vendorMapper;

  public APIController(VendorMapper vendorMapper) {
    this.vendorMapper = vendorMapper;
  }

  @PostMapping("/vendor")
  public ResponseEntity<String> createVendor(@RequestBody VendorDTO vendorDTO)
      throws DuplicateIdException {
    log.info("Creating new vendor: {}", vendorDTO);
    String vendor = vendorMapper.saveOrUpdateVendor(vendorDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(vendor);
  }

  @GetMapping("/vendor/{vendorId}")
  public ResponseEntity<String> getVendor(@PathVariable Long vendorId) {
    String vendor = vendorMapper.getVendor(vendorId);
    return ResponseEntity.ok(vendor);
  }

 @PutMapping("/vendor")
public ResponseEntity<String> updateVendor(@RequestBody VendorDTO vendorDTO)
     throws DuplicateIdException {
  if(vendorDTO.getId() == null) throw new IdNotSuppliedException("Vendor ID not supplied");
  return ResponseEntity.ok(vendorMapper.saveOrUpdateVendor(vendorDTO));
}
  @DeleteMapping("/vendor/{vendorId}")
  public ResponseEntity<String> deleteVendor(@PathVariable Long vendorId) {
    log.info("Deleting vendor with ID {}", vendorId);
    vendorMapper.deleteVendor(vendorId);
    return ResponseEntity.ok("Vendor deleted");
  }
}
