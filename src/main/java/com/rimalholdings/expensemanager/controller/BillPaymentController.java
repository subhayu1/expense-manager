package com.rimalholdings.expensemanager.controller;

import com.rimalholdings.expensemanager.data.dto.BillPaymentDTO;
import com.rimalholdings.expensemanager.model.mapper.BillPaymentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j(topic = "BillPaymentController")
@RequestMapping("/api/v1/bill-payment")
public class BillPaymentController {
  private final BillPaymentMapper billPaymentMapper;

  public BillPaymentController(BillPaymentMapper billPaymentMapper) {
    this.billPaymentMapper = billPaymentMapper;
  }
  @PostMapping("/")
  public ResponseEntity<String> createBillPayment(@RequestBody BillPaymentDTO billPaymentDTO) {
    log.info("Creating new bill payment: {}", billPaymentDTO);
    String createdBillPayment = billPaymentMapper.saveOrUpdateEntity(billPaymentDTO);
    return ResponseEntity.ok(createdBillPayment);
  }
}
