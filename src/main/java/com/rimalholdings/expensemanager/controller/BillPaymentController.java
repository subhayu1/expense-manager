/* (C)1 */
package com.rimalholdings.expensemanager.controller;

import java.util.HashMap;
import java.util.List;

import com.rimalholdings.expensemanager.data.dto.BillPayment;
import com.rimalholdings.expensemanager.data.entity.BillPaymentEntity;
import com.rimalholdings.expensemanager.exception.UpdateNotAllowedException;
import com.rimalholdings.expensemanager.model.mapper.BillPaymentServiceMapper;

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
public class BillPaymentController implements APIControllerInterface {

private final BillPaymentServiceMapper billPaymentMapper;

public BillPaymentController(BillPaymentServiceMapper billPaymentMapper) {
	this.billPaymentMapper = billPaymentMapper;
}

@PostMapping("/")
public ResponseEntity<String> createBillPayment(@RequestBody BillPayment billPayment) {
	log.info("Creating new bill payment: {}", billPayment);
	if (billPayment.getId() != null) {
	throw new UpdateNotAllowedException("Not allowed to update bill payment");
	}
	String createdBillPayment = billPaymentMapper.saveOrUpdateEntity(billPayment);
	return ResponseEntity.status(HttpStatus.CREATED).body(createdBillPayment);
}

@GetMapping("/")
public ResponseEntity<Page<BillPaymentEntity>> getAllBillPayments(
	@ParameterObject Pageable pageable) {
	log.info("Getting all bill payments");
	Page<BillPaymentEntity> allBillPayments = billPaymentMapper.getAllEntities(pageable);
	return ResponseEntity.ok(allBillPayments);
}

@GetMapping("/{billPaymentId}")
public ResponseEntity<String> getBillPayment(@PathVariable Long billPaymentId) {
	log.info("Getting bill payment with id: {}", billPaymentId);
	String billPayment = billPaymentMapper.getEntity(billPaymentId);
	return ResponseEntity.ok(billPayment);
}

@GetMapping("/prepareObject")
public ResponseEntity<List<HashMap<String, Object>>> prepareObject(@RequestParam Integer id) {
	log.info("Getting bill payment with id: {}", id);
	List<HashMap<String, Object>> billPayment =
		billPaymentMapper.prepareBillPayObjectForSync(Long.valueOf(id));
	return ResponseEntity.ok(billPayment);
}
}
