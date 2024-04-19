/* (C)1 */
package com.rimalholdings.expensemanager.model.mapper;

import java.math.BigDecimal;
import java.util.*;

import com.rimalholdings.expensemanager.data.dto.BaseDTOInterface;
import com.rimalholdings.expensemanager.data.dto.BillPayment;
import com.rimalholdings.expensemanager.data.dto.ExpensePayment;
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

protected BillPaymentEntity createBillPaymentEntity(BillPayment billpayment, Long vendorId) {
	BillPaymentEntity billPaymentEntity = new BillPaymentEntity();

	VendorEntity vendorEntity = new VendorEntity();
	ApPaymentEntity apPaymentEntity = new ApPaymentEntity();
	apPaymentEntity.setId(this.apPaymentId);

	billPaymentEntity.setPaymentMethod(billpayment.getPaymentMethod());
	billPaymentEntity.setPaymentReference(billpayment.getPaymentReference());
	billPaymentEntity.setPaymentDate(
		DateTimeUtil.convertISO8601ToTimestamp(billpayment.getPaymentDate()));
	billPaymentEntity.setCreatedDate(DateTimeUtil.getCurrentTimeInUTC());
	billPaymentEntity.setToSync(billpayment.getToSync());
	vendorEntity.setId(vendorId);
	billPaymentEntity.setVendor(vendorEntity);
	billPaymentEntity.setApPayment(apPaymentEntity);

	return billPaymentEntity;
}

protected BillPayment processExpensePayments(
	List<ExpensePayment> expensePayments, BillPayment billpayment) {
	for (ExpensePayment expensePayment : expensePayments) {
	BigDecimal paymentAmount = expensePayment.getPaymentAmount();
	validatePaymentAmount(expensePayments, billpayment);
	ExpenseEntity expenseEntity =
		getExpenseEntity(expensePayment.getExpenseId(), expensePayment.getVendorId());
	expenseEntity.setPaymentAmount(paymentAmount);
	expenseEntity.setApPaymentId(this.apPaymentId);
	handleOverPayment(paymentAmount, expenseEntity.getAmountDue());
	BillPaymentEntity billPaymentEntity =
		createBillPaymentEntity(billpayment, expensePayment.getVendorId());
	billPaymentEntity.setPaymentAmount(paymentAmount);
	billPaymentEntity.setExpense(expenseEntity);
	updatePaymentStatus(paymentAmount, expenseEntity, billPaymentEntity);
	updateDueAmount(paymentAmount, expenseEntity);
	// billpayment.getExpensePayments().add(expensePayment);
	billPaymentService.save(billPaymentEntity);
	}
	return billpayment;
}

public BillPayment mapBillPayment(BaseDTOInterface dtoInterface) {
	BillPayment billpayment = (BillPayment) dtoInterface;
	List<ExpensePayment> expensePayments = billpayment.getExpensePayments();

	if (!expensePayments.isEmpty()) {
	return processExpensePayments(expensePayments, billpayment);
	} else {
	handleEmptyExpensePayments();
	}
	return null;
}

protected void validatePaymentAmount(
	List<ExpensePayment> expensePayments, BillPayment billpayment) {
	BigDecimal sumOfExpensePaymentMapValues =
		expensePayments.stream()
			.map(ExpensePayment::getPaymentAmount)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
	if (sumOfExpensePaymentMapValues.compareTo(billpayment.getPaymentAmount()) != 0) {
	throw new IllegalArgumentException(
		"payment amount does not match sum of expense payment amounts");
	}
}

protected ExpenseEntity getExpenseEntity(Long expenseId, Long vendorId) {
	ExpenseEntity expenseEntity = expenseService.findById(expenseId);
	if (expenseEntity.getId() == null) {
	throw new EntityNotFoundException("Expense with id " + expenseId + " not found");
	}
	if (!Objects.equals(expenseEntity.getVendor().getId(), vendorId)) {

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
