package com.rimalholdings.expensemanager.controller;

import com.rimalholdings.expensemanager.model.mapper.VendorServiceMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sync")
@Slf4j(topic = "SyncController")
public class SyncController {
  private final VendorServiceMapper vendorMapper;


  public SyncController(VendorServiceMapper vendorMapper) {
    this.vendorMapper = vendorMapper;
  }

  @GetMapping("/getVendors")
  public ResponseEntity<String> getVendors(@RequestParam Integer externalOrgId) {
    vendorMapper.fetchAndSaveVendors(externalOrgId);
    return ResponseEntity.ok("Vendors fetched and saved successfully");
  }
//@GetMapping("/syncVendors")
//public ResponseEntity<String> syncVendors(@RequestParam Integer externalOrgId) {
//    vendorMapper.fetchAndSaveVendors(externalOrgId);
//    return ResponseEntity.ok("Vendors fetched and saved successfully");
//}

}
