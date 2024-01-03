package com.rimalholdings.expensemanager.controller;


import com.rimalholdings.expensemanager.Exception.DuplicateIdException;
import com.rimalholdings.expensemanager.Exception.IdNotSuppliedException;
import com.rimalholdings.expensemanager.data.dto.VendorDTO;
import com.rimalholdings.expensemanager.data.entity.VendorEntity;
import com.rimalholdings.expensemanager.model.mapper.VendorMapper;
import java.util.List;
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
@RequestMapping("/api/v1/vendor")
@Slf4j(topic = "VendorController")
public class VendorController {

  private final VendorMapper vendorMapper;

  public VendorController(VendorMapper vendorMapper) {
    this.vendorMapper = vendorMapper;
  }

  @PostMapping("/")
  public ResponseEntity<String> createVendor(@RequestBody VendorDTO vendorDTO)
      throws DuplicateIdException {
    log.info("Creating new vendor: {}", vendorDTO);
    String createdVendor = vendorMapper.saveOrUpdateEntity(vendorDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdVendor);
  }

  @GetMapping("/{vendorId}")
  public ResponseEntity<String> getVendor(@PathVariable Long vendorId) {
    String vendor = vendorMapper.getEntity(vendorId);
    return ResponseEntity.ok(vendor);
  }
  @GetMapping("/")
  public ResponseEntity<List<VendorEntity>>getAllVendors() {
    List<VendorEntity> vendors = vendorMapper.getAllEntities();
    return ResponseEntity.ok(vendors);
  }

  @PutMapping("/")
  public ResponseEntity<String> updateVendor(@RequestBody VendorDTO vendorDTO)
      throws DuplicateIdException, IdNotSuppliedException {
    if(vendorDTO.getId() == null) throw new IdNotSuppliedException("Vendor ID not supplied");
    String updatedVendor = vendorMapper.saveOrUpdateEntity(vendorDTO);
    return ResponseEntity.ok(updatedVendor);
  }

  @DeleteMapping("/{vendorId}")
  public ResponseEntity<String> deleteVendor(@PathVariable Long vendorId) {
    log.info("Deleting vendor with ID {}", vendorId);
    vendorMapper.deleteEntity(vendorId);
    return ResponseEntity.ok("Vendor deleted");
  }

}
