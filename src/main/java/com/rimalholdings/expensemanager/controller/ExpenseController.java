package com.rimalholdings.expensemanager.controller;

import com.rimalholdings.expensemanager.data.dto.ExpenseDTO;
import com.rimalholdings.expensemanager.data.entity.ExpenseEntity;
import com.rimalholdings.expensemanager.model.mapper.ExpenseMapper;
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
  @PutMapping("/")
  public ResponseEntity<String> updateExpense(@RequestBody ExpenseDTO expenseDTO) {
    log.info("Updating expense: {}", expenseDTO);
    String updatedExpense = expenseMapper.saveOrUpdateEntity(expenseDTO);
    return ResponseEntity.ok(updatedExpense);
  }
  @GetMapping("/{expenseId}")
  public ResponseEntity<String> getExpense(@PathVariable Long expenseId) {
    String expense = expenseMapper.getEntity(expenseId);
    return ResponseEntity.ok(expense);
  }
  @GetMapping("/")
  public ResponseEntity<List<ExpenseEntity>> getAllExpenses() {
    List<ExpenseEntity> expenses = expenseMapper.getAllEntities();
    return ResponseEntity.ok(expenses);
  }

  @DeleteMapping("/{expenseId}")
  public ResponseEntity<String> deleteExpense(@PathVariable Long expenseId) {
    log.info("Deleting expense with ID {}", expenseId);
    expenseMapper.deleteEntity(expenseId);
    return ResponseEntity.ok("Expense deleted");
  }



}
