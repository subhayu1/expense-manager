/* (C)1 */
package com.rimalholdings.expensemanager.model.mapper;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Optional;

import com.rimalholdings.expensemanager.data.dto.BaseDTOInterface;
import com.rimalholdings.expensemanager.data.dto.Expense;
import com.rimalholdings.expensemanager.data.entity.ExpenseEntity;
import com.rimalholdings.expensemanager.data.entity.VendorEntity;
import com.rimalholdings.expensemanager.exception.UpdateNotAllowedException;
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
private static final Integer PARTIALLY_PAID = 1;
private static final Integer PAID = 2;
private static final Integer UNPAID = 3;

protected ExpenseServiceMapper(ObjectMapper objectMapper, ExpenseService expenseService) {
	super(objectMapper);
	this.expenseService = expenseService;
}

@Override
public ExpenseEntity mapToDTO(BaseDTOInterface dtoInterface) {
	Expense expense = (Expense) dtoInterface;

	ExpenseEntity expenseEntity = getOrCreateExpenseEntity(expense);
	setExpenseEntityFields(expenseEntity, expense);

	return expenseEntity;
}

private ExpenseEntity getOrCreateExpenseEntity(Expense expense) {
	ExpenseEntity expenseEntity = getEntityForUpdate(expense.getId());
	if (expenseEntity == null) {
	expenseEntity = new ExpenseEntity();
	expenseEntity.setCreatedDate(new Timestamp(System.currentTimeMillis()));
	expenseEntity.setPaymentAmount(BigDecimal.ZERO);
	expenseEntity.setPaymentStatus(UNPAID); // Assuming UNPAID is a constant for default status
	} else {
	dontAllowPartiallyPaidOrPaidExpensesToBeUpdated(expenseEntity);
	}
	expenseEntity.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
	return expenseEntity;
}

private void setExpenseEntityFields(ExpenseEntity expenseEntity, Expense expense) {
	VendorEntity vendorEntity = new VendorEntity();
	vendorEntity.setId(expense.getVendorId()); // Assuming vendorId is always set
	expenseEntity.setVendor(vendorEntity);

	setIfNotNull(expense::getId, expenseEntity::setId);
	setIfNotNull(expense::getTotalAmount, expenseEntity::setTotalAmount);
	setIfNotNull(expense::getDescription, expenseEntity::setDescription);
	setIfNotNull(
		() -> Optional.ofNullable(expense.getDueDate()).map(Timestamp::valueOf).orElse(null),
		expenseEntity::setDueDate);

	// Assuming Amount Due is always equal to Total Amount
	expenseEntity.setAmountDue(expenseEntity.getTotalAmount());
}

private void dontAllowPartiallyPaidOrPaidExpensesToBeUpdated(ExpenseEntity expenseEntity) {
	if (Objects.equals(expenseEntity.getPaymentStatus(), PARTIALLY_PAID)
		|| Objects.equals(expenseEntity.getPaymentStatus(), PAID)) {
	// We don't allow updating of expenses that are already paid or partially paid
	throw new UpdateNotAllowedException(
		"Expense cannot be updated because it is already paid or partially paid");
	}
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
	ExpenseEntity expenseEntity = getExistingEntity(id);
	if (expenseEntity != null
		&& (Objects.equals(expenseEntity.getPaymentStatus(), PARTIALLY_PAID)
			|| Objects.equals(expenseEntity.getPaymentStatus(), PAID))) {
		log.info("Expense with ID {} cannot be deleted because it is {}", id,
        expenseEntity.getPaymentStatus().equals(PARTIALLY_PAID) ? "partially paid" : "paid");
	throw new UpdateNotAllowedException(
			String.format("Expense with ID %s cannot be deleted because it is %s", id,
			expenseEntity.getPaymentStatus().equals(PARTIALLY_PAID) ? "partially paid" : "paid"));
	}
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
