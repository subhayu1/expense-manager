/* (C)1 */
package com.rimalholdings.expensemanager.model.mapper;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.rimalholdings.expensemanager.data.dto.BaseDTOInterface;
import com.rimalholdings.expensemanager.data.dto.Expense;
import com.rimalholdings.expensemanager.data.entity.ExpenseEntity;
import com.rimalholdings.expensemanager.data.entity.VendorEntity;
import com.rimalholdings.expensemanager.service.ExpenseService;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "ExpenseServiceMapper")
public class ExpenseServiceMapper extends AbstractServiceMapper<ExpenseEntity> {

private final ExpenseService expenseService;
private static final Integer UNPAID = 3;

protected ExpenseServiceMapper(ObjectMapper objectMapper, ExpenseService expenseService) {
	super(objectMapper);
	this.expenseService = expenseService;
}

@Override
public ExpenseEntity mapToDTO(BaseDTOInterface dtoInterface) {
	// TODO: set paymentAmount to BigDecimal.ZERO by default when creating expense
	Expense expense = (Expense) dtoInterface;
	VendorEntity vendorEntity = new VendorEntity();
	vendorEntity.setId(expense.getVendorId());

	ExpenseEntity expenseEntity = getEntityForUpdate(expense.getId());
	if (expenseEntity == null) {
	expenseEntity = new ExpenseEntity();
	expenseEntity.setCreatedDate(new Timestamp(System.currentTimeMillis()));
	}
	expenseEntity.setPaymentAmount(BigDecimal.ZERO);

	expenseEntity.setId(expense.getId());
	expenseEntity.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
	expenseEntity.setVendor(vendorEntity);
	expenseEntity.setTotalAmount(expense.getTotalAmount());
	expenseEntity.setAmountDue(expense.getTotalAmount());
	expenseEntity.setDescription(expense.getDescription());
	// set payment status to unpaid by default when creating expense
	expenseEntity.setPaymentStatus(UNPAID);

	if (expense.getDueDate() != null) {
	expenseEntity.setDueDate(Timestamp.valueOf(expense.getDueDate()));
	}

	return expenseEntity;
}

protected ExpenseEntity getExistingEntity(Long id) {
	try {
	return expenseService.findById(id);

	} catch (Exception e) {
	return null;
	}
}

@Override
public void deleteEntity(Long id) {
	expenseService.deleteById(id);
}

@Override
public String getEntity(Long id) {
	return convertDtoToString(expenseService.findById(id));
}

@Override
public String saveOrUpdateEntity(BaseDTOInterface dtoInterface) {
	if (!(dtoInterface instanceof Expense expense)) {
	throw new IllegalArgumentException("Invalid DTO type");
	}
	ExpenseEntity expenseEntity = mapToDTO(expense);
	ExpenseEntity savedExpense = expenseService.save(expenseEntity);

	return convertDtoToString(savedExpense);
}

@Override
public Page<ExpenseEntity> getAllEntities(Pageable pageable) {
	return expenseService.findAll(pageable);
}

@Override
public ExpenseEntity getEntityForUpdate(Long id) {
	try {
	return expenseService.findById(id);
	} catch (Exception e) {
	return null;
	}
}
}
