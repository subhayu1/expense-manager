/* (C)1 */
package com.rimalholdings.expensemanager.model.mapper;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

import com.rimalholdings.expensemanager.data.dto.BaseDTOInterface;
import com.rimalholdings.expensemanager.data.dto.Expense;
import com.rimalholdings.expensemanager.data.entity.ExpenseEntity;
import com.rimalholdings.expensemanager.data.entity.VendorEntity;
import com.rimalholdings.expensemanager.exception.UpdateNotAllowedException;
import com.rimalholdings.expensemanager.service.ExpenseService;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "ExpenseServiceMapper")
public class ExpenseServiceMapper extends AbstractServiceMapper<ExpenseEntity> {

private static final Integer PARTIALLY_PAID = 1;
private static final Integer PAID = 2;
private static final Integer UNPAID = 3;
private final ExpenseService expenseService;

protected ExpenseServiceMapper(ObjectMapper objectMapper, ExpenseService expenseService) {
	super(objectMapper);
	this.expenseService = expenseService;
}

public ExpenseEntity mapToDTO(BaseDTOInterface dtoInterface) {
	Expense expense = (Expense) dtoInterface;

	ExpenseEntity expenseEntity = getOrCreateExpenseEntity(expense);
	setExpenseEntityFields(expenseEntity, expense);

	return expenseEntity;
}

private ExpenseEntity getOrCreateExpenseEntity(Expense expense) {
	ExpenseEntity expenseEntity = getEntityForUpdate(expense.getId(), expense.getIntegrationId());
	if (expenseEntity == null) {
	expenseEntity = new ExpenseEntity();
	expenseEntity.setCreatedDate(new Timestamp(System.currentTimeMillis()));
	expenseEntity.setPaymentAmount(BigDecimal.ZERO);
	expenseEntity.setPaymentStatus(UNPAID);
	} else {
	dontAllowPartiallyPaidOrPaidExpensesToBeUpdated(expenseEntity);
	}
	expenseEntity.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
	return expenseEntity;
}

private void setExpenseEntityFields(ExpenseEntity expenseEntity, Expense expense) {
	VendorEntity vendorEntity = new VendorEntity();
	if (expense.getVendorId() == null) {
	// If vendorId is not set, then we need to find the vendorId from the vendorIntegrationId
	vendorEntity.setId(getVendorIdFromVendorIntegrationId(expense.getVendorIntegrationId()));
	} else {
	vendorEntity.setId(expense.getVendorId());
	}
	expenseEntity.setVendor(vendorEntity);

	setIfNotNull(expense::getId, expenseEntity::setId);
	setIfNotNull(expense::getTotalAmount, expenseEntity::setTotalAmount);
	setIfNotNull(expense::getDescription, expenseEntity::setDescription);
	setIfNotNull(expense::getExternalOrgId, expenseEntity::setExternalOrgId);
	setIfNotNull(expense::getVendorInvoiceNumber, expenseEntity::setVendorInvoiceNumber);
	setIfNotNull(expense::getExternalInvoiceNumber, expenseEntity::setExternalInvoiceNumber);
	setIfNotNull(expense::getIntegrationId, expenseEntity::setIntegrationId);

	setInvoiceAndDueDate(expenseEntity, expense);

	// Assuming Amount Due is always equal to Total Amount
	expenseEntity.setAmountDue(expenseEntity.getTotalAmount());
}

private void setInvoiceAndDueDate(ExpenseEntity expenseEntity, Expense expense) {
	if (expense.getInvoiceDate() != null) {
	expenseEntity.setInvoiceDate(Date.valueOf(expense.getInvoiceDate()));
	}
	if (expense.getDueDate() != null) {
	expenseEntity.setDueDate(Date.valueOf(expense.getDueDate()));
	}
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
	log.info(
		"Expense with ID {} cannot be deleted because it is {}",
		id,
		expenseEntity.getPaymentStatus().equals(PARTIALLY_PAID) ? "partially paid" : "paid");
	throw new UpdateNotAllowedException(
		String.format(
			"Expense with ID %s cannot be deleted because it is %s",
			id,
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
	return expenseService.getExpensesByExternalOrgId(pageable, null);
}

@Override
public ExpenseEntity getEntityForUpdate(Long id) {
	return null;
}

public Page<ExpenseEntity> getAllEntities(Pageable pageable, Integer externalOrgId) {
	return expenseService.getExpensesByExternalOrgId(pageable, externalOrgId);
}

public ExpenseEntity getEntityForUpdate(Long id, String integrationId) {
	// 1. check if Id is null, if not null, then get the entity by Id
	// then check if findByIntegrationId is null, if not null, then get the entity by IntegrationId
	// and then update the entity
	if (id != null && expenseService.existsById(id)) {
	return expenseService.findById(id);
	} else if (integrationId != null && expenseService.existsByIntegrationId(integrationId)) {
	return expenseService.findByIntegrationId(integrationId);
	}
	return null;
}

public ExpenseEntity getExpenseEntityByIntegrationId(String integrationId) {
	try {
	return expenseService.findByIntegrationId(integrationId);
	} catch (Exception e) {
	return null;
	}
}

public void saveExpenses(List<Expense> expenses) {
	// Convert the List<Expense> to the correct type
	List<Expense> expenseEntities = convertMessageToExpenseResponse(expenses);
	for (Expense expense : expenseEntities) {
	try {
		saveOrUpdateEntity(expense);
	} catch (Exception e) {
		log.error("Error while saving expense: {}", e.getMessage(), e);
	}
	}
}

// find vendorId from vendorIntegrationId
public Long getVendorIdFromVendorIntegrationId(String vendorIntegrationId) {
	return expenseService.getVendorIdFromVendorIntegrationId(vendorIntegrationId);
}

private List<Expense> convertMessageToExpenseResponse(List<Expense> messageWrapper) {
	ObjectMapper objectMapper = new ObjectMapper();
	try {
	return objectMapper.convertValue(messageWrapper, new TypeReference<>() {});
	} catch (Exception e) {
	log.error("Error while mapping json to Object: {}", e.getMessage(), e);
	throw new RuntimeException("Failed to map json to Expense", e);
	}
}

public List<ExpenseEntity> getUnpaidExpenses() {
	return expenseService.getUnpaidExpenses();
}
}
