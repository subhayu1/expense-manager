/* (C)1 */
package com.rimalholdings.expensemanager.controller;

import com.rimalholdings.expensemanager.data.dto.BillPayment;
import com.rimalholdings.expensemanager.data.dto.BillPaymentInvoice;
import com.rimalholdings.expensemanager.data.dto.BillPaymentUpdate;
import com.rimalholdings.expensemanager.data.dto.VendorPaymentResults;
import com.rimalholdings.expensemanager.data.entity.BillPaymentEntity;
import com.rimalholdings.expensemanager.exception.UpdateNotAllowedException;
import com.rimalholdings.expensemanager.model.mapper.BillPayIntegrationHandler;
import com.rimalholdings.expensemanager.model.mapper.BillPaymentServiceMapper;
import com.rimalholdings.expensemanager.sync.MessageWrapper;

import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j(topic = "BillPaymentController")
@RequestMapping("/api/v1/bill-payment")

// @CrossOrigin(origins = "*", allowedHeaders = "*")
public class BillPaymentController implements APIControllerInterface {
private final BillPaymentServiceMapper billPaymentMapper;
private final BillPayIntegrationHandler billPayIntegrationHandler;

public BillPaymentController(
	BillPaymentServiceMapper billPaymentMapper,
	BillPayIntegrationHandler billPayIntegrationHandler) {
	this.billPaymentMapper = billPaymentMapper;
	this.billPayIntegrationHandler = billPayIntegrationHandler;
}

@PostMapping("/")
// @CrossOrigin(origins = "*", allowedHeaders = "*")
public ResponseEntity<BillPayment> createBillPayment(@RequestBody BillPayment billPayment) {
	log.info("Creating new bill payment: {}", billPayment);
	if (billPayment.getId() != null) {
	throw new UpdateNotAllowedException("Not allowed to update bill payment");
	}
	BillPayment createdBillPayment = billPaymentMapper.saveBillPayment(billPayment);
	return ResponseEntity.status(HttpStatus.CREATED).body(createdBillPayment);
}

@GetMapping("/")
public ResponseEntity<Page<BillPaymentEntity>> getAllBillPayments(
	@ParameterObject Pageable pageable) {
	log.info("Getting all bill payments");
	Page<BillPaymentEntity> allBillPayments = billPaymentMapper.getAllEntities(pageable);
	return ResponseEntity.ok(allBillPayments);
}

@GetMapping("/blps")
public ResponseEntity<MessageWrapper<BillPaymentInvoice>> getBillPayments(
	@RequestParam Integer orgId) {
	log.info("Getting bill payments");
	MessageWrapper<BillPaymentInvoice> billPayments =
		billPayIntegrationHandler.getBillPaymentInvoices(Long.valueOf(orgId));
	return ResponseEntity.ok(billPayments);
}

@GetMapping("/{billPaymentId}")
public ResponseEntity<String> getBillPayment(@PathVariable Long billPaymentId) {
	log.info("Getting bill payment with id: {}", billPaymentId);
	String billPayment = billPaymentMapper.getEntity(billPaymentId);
	return ResponseEntity.ok(billPayment);
}

@GetMapping("/prepareObjectForSync")
public ResponseEntity<MessageWrapper<VendorPaymentResults>> prepareObjectForSync(
	@RequestParam Integer orgId) {
	log.info("Getting bill payments");
	MessageWrapper<VendorPaymentResults> billPayment =
		billPayIntegrationHandler.mapBillPayForSyncService(Long.valueOf(orgId));
	return ResponseEntity.ok(billPayment);
}

@PutMapping("/updateIntegrationId")
public ResponseEntity<String> updateIntegrationId(
	@RequestBody BillPaymentUpdate billPaymentUpdate) {
	log.info("Updating integration id for bill payment with : {}", billPaymentUpdate);
	billPayIntegrationHandler.updateBillPayWithIntegrationId(
		billPaymentUpdate.getInvoiceExternalDocumentNumber(),
		Long.valueOf(billPaymentUpdate.getOrgId()),
		billPaymentUpdate.getIntegrationId());
	return ResponseEntity.ok("Integration id updated successfully");
}

@PostMapping("/allowIntegration")
public ResponseEntity<String> allowIntegration(@RequestBody BillPaymentUpdate billPaymentUpdate) {

	log.info(
		"Setting toSync to: {} for bill payment with id: {}",
		billPaymentUpdate.getAllowIntegration(),
		billPaymentUpdate.getBillPayId());
	billPayIntegrationHandler.allowBillPaymentIntegration(
		billPaymentUpdate.getAllowIntegration(), billPaymentUpdate.getBillPayId());
	return ResponseEntity.ok("Integration allowed successfully");
}

@PostMapping("/clearIntegrationId")
public ResponseEntity<String> clearIntegrationId(
	@RequestBody BillPaymentUpdate billPaymentUpdate) {
	log.info(
		"Clearing integration id for bill payment with id: {}", billPaymentUpdate.getBillPayId());
	billPayIntegrationHandler.clearIntegrationId(billPaymentUpdate.getBillPayId());
	return ResponseEntity.ok("Integration id cleared successfully");
}
}
