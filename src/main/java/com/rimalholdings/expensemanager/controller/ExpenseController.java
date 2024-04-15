/* (C)1 */
package com.rimalholdings.expensemanager.controller;

import com.rimalholdings.expensemanager.data.dto.Expense;
import com.rimalholdings.expensemanager.data.entity.ExpenseEntity;
import com.rimalholdings.expensemanager.model.mapper.ExpenseServiceMapper;

import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j(topic = "ExpenseController")
@CrossOrigin
@RequestMapping(value = "/api/v1/expense", produces = MediaType.APPLICATION_JSON_VALUE)
public class ExpenseController implements APIControllerInterface {

private final ExpenseServiceMapper expenseMapper;

public ExpenseController(ExpenseServiceMapper expenseMapper) {
	this.expenseMapper = expenseMapper;
}

@PostMapping("/")
public ResponseEntity<String> createExpense(@RequestBody Expense expense) {
	log.info("Creating new expense: {}", expense);
	String createdExpense = expenseMapper.saveOrUpdateEntity(expense);

	return ResponseEntity.status(HttpStatus.CREATED).body(createdExpense);
}

@PutMapping("/")
public ResponseEntity<String> updateExpense(@RequestBody Expense expense) {
	log.info("Updating expense: {}", expense);
	String updatedExpense = expenseMapper.saveOrUpdateEntity(expense);
	return ResponseEntity.ok(updatedExpense);
}

@GetMapping(value = "/{expenseId}")
public ResponseEntity<String> getExpense(@PathVariable Long expenseId) {
	String expense = expenseMapper.getEntity(expenseId);
	return ResponseEntity.ok(expense);
}

@GetMapping("/")
public ResponseEntity<Page<ExpenseEntity>> getAllExpenses(
	@RequestParam(value = "externalOrgId", required = false) Integer externalOrgId,
	@ParameterObject Pageable pageable) {
	Page<ExpenseEntity> expenses = expenseMapper.getAllEntities(pageable, externalOrgId);
	return ResponseEntity.ok(expenses);
}

@DeleteMapping("/{expenseId}")
public ResponseEntity<String> deleteExpense(@PathVariable Long expenseId) {
	log.info("Deleting expense with ID {}", expenseId);
	expenseMapper.deleteEntity(expenseId);
	return ResponseEntity.ok("Expense deleted");
}
}
