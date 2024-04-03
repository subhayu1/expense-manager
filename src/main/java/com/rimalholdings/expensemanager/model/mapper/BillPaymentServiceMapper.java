/* (C)1 */
package com.rimalholdings.expensemanager.model.mapper;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

import com.rimalholdings.expensemanager.data.dto.BaseDTOInterface;
import com.rimalholdings.expensemanager.data.dto.BillPayment;
import com.rimalholdings.expensemanager.data.entity.ApPaymentEntity;
import com.rimalholdings.expensemanager.data.entity.BillPaymentEntity;
import com.rimalholdings.expensemanager.data.entity.ExpenseEntity;
import com.rimalholdings.expensemanager.data.entity.VendorEntity;
import com.rimalholdings.expensemanager.exception.CannotOverpayExpenseException;
import com.rimalholdings.expensemanager.exception.InvalidObjectException;
import com.rimalholdings.expensemanager.exception.NoExpensePaymentsSpecifiedException;
import com.rimalholdings.expensemanager.service.BillPaymentService;
import com.rimalholdings.expensemanager.service.ExpenseService;
import com.rimalholdings.expensemanager.util.DateTimeUtil;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j(topic = "BillPaymentServiceMapper")
public class BillPaymentServiceMapper<T> {

private static final Integer ZERO = 0;
private static final Integer PARTIALLY_PAID = 1;
private static final Integer PAID = 2;
private static final Integer UNPAID = 3;
private static final Integer UNAPPLIED = 3;
private static final Integer FULLY_APPLIED = 2;
private static final Integer PARTIALLY_APPLIED = 1;
private final BillPaymentService billPaymentService;
private final ExpenseService expenseService;
private final ApPaymentMapper apPaymentMapper;
private Integer apPaymentId;

protected BillPaymentServiceMapper(
	BillPaymentService billPaymentService,
	ExpenseService expenseService,
	ApPaymentMapper apPaymentMapper) {
	this.billPaymentService = billPaymentService;
	this.expenseService = expenseService;
	this.apPaymentMapper = apPaymentMapper;
}

// TODO: figure out how to add appPaymentId to the BillPaymentEntity.
// Problem: we need to first make sure billPaymentEntity is saved before we can get the id
// of the billPaymentEntity to create the apPaymentEntity and
// then add the apPaymentEntity id to the expenseEntity somehow.

private Long parseLongFromString(Map<String, Long> expenseId) {
	for (Map.Entry<String, Long> entry : expenseId.entrySet()) {
	return Long.parseLong(entry.getKey());
	}
	return null;
}

public BillPayment mapBillPayment(BaseDTOInterface dtoInterface) {
	BillPayment billpayment = (BillPayment) dtoInterface;
	Map<String, BigDecimal> expensePaymentMap = billpayment.getExpensePayments();

	if (!expensePaymentMap.isEmpty()) {
	return processExpensePayments(expensePaymentMap, billpayment);
	} else {
	handleEmptyExpensePayments();
	}
	return null;
}

protected BillPaymentEntity createBillPaymentEntity(BillPayment billpayment) {
	BillPaymentEntity billPaymentEntity = new BillPaymentEntity();

	VendorEntity vendorEntity = new VendorEntity();
	ApPaymentEntity apPaymentEntity = new ApPaymentEntity();
	apPaymentEntity.setId(this.apPaymentId);

	billPaymentEntity.setPaymentMethod(billpayment.getPaymentMethod());
	billPaymentEntity.setPaymentReference(billpayment.getPaymentReference());
	billPaymentEntity.setPaymentDate(Timestamp.valueOf(billpayment.getPaymentDate()));
	billPaymentEntity.setCreatedDate(DateTimeUtil.getCurrentTimeInUTC());
	billPaymentEntity.setToSync(billpayment.getToSync());
	vendorEntity.setId(billpayment.getVendorId());
	billPaymentEntity.setVendor(vendorEntity);
	billPaymentEntity.setApPayment(apPaymentEntity);

	return billPaymentEntity;
}

protected BillPayment processExpensePayments(
	Map<String, BigDecimal> expensePaymentMap, BillPayment billpayment) {
	expensePaymentMap.forEach(
		(expenseId, paymentAmount) -> {
		validatePaymentAmount(expensePaymentMap, billpayment);
		ExpenseEntity expenseEntity = getExpenseEntity(expenseId, billpayment);
		expenseEntity.setPaymentAmount(paymentAmount);
		expenseEntity.setApPaymentId(this.apPaymentId);

		log.info("amount due: {}", expenseEntity.getAmountDue());
		log.info("payment amount: {}", paymentAmount);

		// check to see if payment amount is greater than amount due and throw exception if it is
		handleOverPayment(paymentAmount, expenseEntity.getAmountDue());

		// Create a new BillPaymentEntity for each expense
		BillPaymentEntity billPaymentEntity = createBillPaymentEntity(billpayment);
		billPaymentEntity.setPaymentAmount(paymentAmount);
		billPaymentEntity.setExpense(expenseEntity);
		updatePaymentStatus(paymentAmount, expenseEntity, billPaymentEntity);
		updateDueAmount(paymentAmount, expenseEntity);
		billpayment.getExpensePayments().put(expenseId, paymentAmount);
		billpayment.setExpensePayments(expensePaymentMap);
		billpayment.setBillPaymentIds(Collections.singletonList(billPaymentEntity.getId()));

		// billPaymentEntity.getExpenses().add(expenseEntity);
		log.info(billPaymentEntity.toString());
		billPaymentService.save(billPaymentEntity);
		});
	return billpayment;
}

protected void validatePaymentAmount(
	Map<String, BigDecimal> expensePaymentMap, BillPayment billpayment) {
	BigDecimal sumOfExpensePaymentMapValues =
		expensePaymentMap.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
	if (sumOfExpensePaymentMapValues.compareTo(billpayment.getPaymentAmount()) != 0) {
	throw new IllegalArgumentException(
		"payment amount does not match sum of expense payment amounts");
	}
}

protected ExpenseEntity getExpenseEntity(String expenseId, BillPayment billpayment) {
	ExpenseEntity expenseEntity = expenseService.findById(Long.parseLong(expenseId));
	if (expenseEntity == null) {
	throw new EntityNotFoundException("Expense with id " + expenseId + " not found");
	}
	if (!Objects.equals(expenseEntity.getVendor().getId(), billpayment.getVendorId())) {

	throw new InvalidObjectException("expense id does not belong to vendor ");
	}
	return expenseEntity;
}

private void updatePaymentStatus(
	BigDecimal paymentAmount, ExpenseEntity expenseEntity, BillPaymentEntity billPaymentEntity) {
	Integer paymentStatus = paymentApplicationStatus(paymentAmount, expenseEntity.getAmountDue());
	billPaymentEntity.setPaymentApplicationStatus(paymentStatus);
	expenseEntity.setPaymentStatus(
		setPaymentStatusOnExpense(paymentAmount, expenseEntity.getAmountDue()));
}

private void updateDueAmount(BigDecimal paymentAmount, ExpenseEntity expenseEntity) {
	ExpenseEntity existingExpenseEntity = expenseService.findById(expenseEntity.getId());
	if (expenseEntity.getAmountDue().compareTo(BigDecimal.ZERO) == ZERO) {
	throw new CannotOverpayExpenseException(
		String.format("Expense: %s already paid in full", expenseEntity.getId()));
	}
	if (existingExpenseEntity.getAmountDue().compareTo(paymentAmount) == 0) {
	expenseEntity.setAmountDue(BigDecimal.ZERO);
	} else {
	expenseEntity.setAmountDue(existingExpenseEntity.getAmountDue().subtract(paymentAmount));
	}
}

private void handleEmptyExpensePayments() {
	log.info("expensePaymentMap is null, throwing exception");
	throw new NoExpensePaymentsSpecifiedException(
		"no expense payments specified. Please specify expense payments");
}

public String getEntity(Long id) {
	return convertDtoToString((T) billPaymentService.findById(id));
}

@Transactional
public BillPayment saveBillPayment(BaseDTOInterface dtoInterface) {
	BillPayment billpayment = (BillPayment) dtoInterface;

	this.apPaymentId = apPaymentMapper.createApPayment(billpayment);
	billpayment.setApPaymentId(this.apPaymentId);

	// BillPaymentEntity billPaymentEntity = mapBillPayment(billpayment);

	// BillPaymentEntity savedBillPaymentEntity = billPaymentService.save(billPaymentEntity);

	return mapBillPayment(billpayment);
}

private String convertDtoToString(T stringValue) {
	ObjectMapper objectMapper = new ObjectMapper();
	try {
	return objectMapper.writeValueAsString(stringValue);
	} catch (Exception e) {
	log.error("Error converting BillPayment to JSON string: {}", e.getMessage());
	}
	return null;
}

private BillPayment convertStringToDto(String billpayment) {
	ObjectMapper objectMapper = new ObjectMapper();
	try {
	return objectMapper.readValue(billpayment, BillPayment.class);
	} catch (Exception e) {
	log.error("Error converting JSON string to BillPayment: {}", e.getMessage());
	}
	return null;
}

public Page<BillPaymentEntity> getAllEntities(Pageable pageable) {
	return billPaymentService.findAll(pageable);
}

public List<BillPaymentEntity> getAllEntities() {
	return billPaymentService.findAll();
}

private Integer paymentApplicationStatus(BigDecimal paymentAmount, BigDecimal amountDue) {
	// paymentapplicationstatus int not null COMMENT '1=partially applied ,2=fully applied
	// ,3=unapplied'
	if (paymentAmount.equals(amountDue)) {
	return FULLY_APPLIED;
	} else if (paymentAmount.compareTo(amountDue) < 0) {
	return PARTIALLY_APPLIED;
	} else {
	return UNAPPLIED;
	}
}

private Integer setPaymentStatusOnExpense(BigDecimal paymentAmount, BigDecimal amountDue) {
	// paymentstatus int not null COMMENT '1=partially paid ,2=fully paid ,3=unpaid, 4=unknown'
	if (paymentAmount.equals(amountDue)) {
	return PAID;
	} else if (paymentAmount.compareTo(amountDue) < 0) {
	return PARTIALLY_PAID;
	} else {
	return UNPAID;
	}
}

private void handleOverPayment(BigDecimal paymentAmount, BigDecimal amountDue) {
	if (paymentAmount.compareTo(amountDue) > 0) {

	throw new CannotOverpayExpenseException(
		String.format(
			"Payment amount cannot be greater than amount due. Payment amount: %s, amount due: %s",
			paymentAmount, amountDue));
	}
}
}
