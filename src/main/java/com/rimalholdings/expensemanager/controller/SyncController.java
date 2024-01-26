package com.rimalholdings.expensemanager.controller;

import com.rimalholdings.expensemanager.model.mapper.VendorServiceMapper;
import com.rimalholdings.expensemanager.queue.RabbitMQSender;

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
private static final String DEFAULT_LAST_MODIFIED_DATE_TIME = "1970-01-01 00:00:00";
private final VendorServiceMapper vendorMapper;
private final RabbitMQSender rabbitMQSender;

public SyncController(VendorServiceMapper vendorMapper, RabbitMQSender rabbitMQSender) {
	this.vendorMapper = vendorMapper;
	this.rabbitMQSender = rabbitMQSender;
}

@GetMapping("/getVendors")
public ResponseEntity<String> getVendors(
	@RequestParam Integer externalOrgId,
	@RequestParam(required = false) String lastModifiedDateTime) {
	// if lastModifiedDateTime is null, then set it to 1970-01-01 00:00:00
	if (lastModifiedDateTime == null) {
	lastModifiedDateTime = DEFAULT_LAST_MODIFIED_DATE_TIME;
	}

	vendorMapper.fetchAndSaveVendors(externalOrgId, lastModifiedDateTime);
	return ResponseEntity.ok("Vendors fetched and saved successfully");
}

@GetMapping("/start")
public ResponseEntity<String> sync(@RequestParam Integer organizationId) {
	log.info("Syncing organization with ID: {}", organizationId);
	rabbitMQSender.send(organizationId);
	return ResponseEntity.ok("Syncing organization with ID: " + organizationId);
}
}
