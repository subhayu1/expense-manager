package com.rimalholdings.expensemanager.controller;

import com.rimalholdings.expensemanager.data.dto.ExpenseDTO;
import com.rimalholdings.expensemanager.model.mapper.ExpenseMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j(topic = "ExpenseController")
@RequestMapping("/api/v1/expense")

public class ExpenseController {
  private final ExpenseMapper expenseMapper;

  public ExpenseController(ExpenseMapper expenseMapper) {
    this.expenseMapper = expenseMapper;
  }

  @PostMapping("/")
  public ResponseEntity<String> createExpense(@RequestBody ExpenseDTO expenseDTO) {
    log.info("Creating new expense: {}", expenseDTO);
    String createdExpense = expenseMapper.saveOrUpdateEntity(expenseDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdExpense);


  }


}
