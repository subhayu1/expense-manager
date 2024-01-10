/* (C)1 */
package com.rimalholdings.expensemanager.model.mapper;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Objects;

import com.rimalholdings.expensemanager.data.dto.BaseDTOInterface;
import com.rimalholdings.expensemanager.data.dto.BillPayment;
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

@Service
@Slf4j(topic = "BillPaymentServiceMapper")
public class BillPaymentServiceMapper extends AbstractServiceMapper<BillPaymentEntity> {

private static final Integer ZERO = 0;
private final BillPaymentService billPaymentService;
private final ExpenseService expenseService;

private static final Integer PARTIALLY_PAID = 1;
private static final Integer PAID = 2;
private static final Integer UNPAID = 3;
private static final Integer UNAPPLIED = 3;
private static final Integer FULLY_APPLIED = 2;
private static final Integer PARTIALLY_APPLIED = 1;

protected BillPaymentServiceMapper(
	ObjectMapper objectMapper,
	BillPaymentService billPaymentService,
	ExpenseService expenseService) {
	super(objectMapper);
	this.billPaymentService = billPaymentService;
	this.expenseService = expenseService;
}

private Long parseLongFromString(Map<String, Long> expenseId) {
	for (Map.Entry<String, Long> entry : expenseId.entrySet()) {
	return Long.parseLong(entry.getKey());
	}
	return null;
}

@Override
public BillPaymentEntity mapToDTO(BaseDTOInterface dtoInterface) {
	BillPayment billpayment = (BillPayment) dtoInterface;
	BillPaymentEntity billPaymentEntity = createBillPaymentEntity(billpayment);
	Map<String, BigDecimal> expensePaymentMap = billpayment.getExpensePayments();

	if (!expensePaymentMap.isEmpty()) {
	processExpensePayments(expensePaymentMap, billpayment, billPaymentEntity);
	} else {
	handleEmptyExpensePayments();
	}

	return billPaymentEntity;
}

protected BillPaymentEntity createBillPaymentEntity(BillPayment billpayment) {
	BillPaymentEntity billPaymentEntity = new BillPaymentEntity();
	billPaymentEntity.setPaymentAmount(billpayment.getPaymentAmount());
	billPaymentEntity.setPaymentMethod(billpayment.getPaymentMethod());
	billPaymentEntity.setPaymentReference(billpayment.getPaymentReference());
	billPaymentEntity.setPaymentDate(Timestamp.valueOf(billpayment.getPaymentDate()));
	billPaymentEntity.setCreatedDate(DateTimeUtil.getCurrentTimeInUTC());

	VendorEntity vendorEntity = new VendorEntity();
	vendorEntity.setId(billpayment.getVendorId());
	billPaymentEntity.setVendor(vendorEntity);

	return billPaymentEntity;
}

protected void processExpensePayments(
	Map<String, BigDecimal> expensePaymentMap,
	BillPayment billpayment,
	BillPaymentEntity billPaymentEntity) {
	expensePaymentMap.forEach(
		(expenseId, paymentAmount) -> {
		validatePaymentAmount(expensePaymentMap, billpayment);
		ExpenseEntity expenseEntity = getExpenseEntity(expenseId, billpayment);
		expenseEntity.setPaymentAmount(paymentAmount);

		log.info("amount due: {}", expenseEntity.getAmountDue());
		log.info("payment amount: {}", paymentAmount);

		updatePaymentStatus(paymentAmount, expenseEntity, billPaymentEntity);
		updateDueAmount(paymentAmount, expenseEntity);
		billPaymentEntity.getExpenses().add(expenseEntity);
		});
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
	throw new CannotOverpayExpenseException("Expense already paid in full");
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

@Override
public void deleteEntity(Long id) {}

@Override
public String getEntity(Long id) {
	return convertDtoToString(billPaymentService.findById(id));
}

@Override
public String saveOrUpdateEntity(BaseDTOInterface dtoInterface) {
	BillPayment billpayment = (BillPayment) dtoInterface;
	BillPaymentEntity billPaymentEntity = mapToDTO(billpayment);
	BillPaymentEntity savedBillPaymentEntity = billPaymentService.save(billPaymentEntity);
	return convertDtoToString(savedBillPaymentEntity);
}

@Override
public Page<BillPaymentEntity> getAllEntities(Pageable pageable) {
	return billPaymentService.findAll(pageable);
}

@Override
public BillPaymentEntity getEntityForUpdate(Long id) {
	// Billpayments cannot be updated
	return null;
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
}
