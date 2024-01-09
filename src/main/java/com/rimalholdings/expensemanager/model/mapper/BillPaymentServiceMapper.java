/* (C)1 */
package com.rimalholdings.expensemanager.model.mapper;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Map;

import com.rimalholdings.expensemanager.data.dto.BaseDTOInterface;
import com.rimalholdings.expensemanager.data.dto.BillPayment;
import com.rimalholdings.expensemanager.data.entity.BillPaymentEntity;
import com.rimalholdings.expensemanager.data.entity.ExpenseEntity;
import com.rimalholdings.expensemanager.data.entity.VendorEntity;
import com.rimalholdings.expensemanager.exception.CannotOverpayExpenseException;
import com.rimalholdings.expensemanager.service.BillPaymentService;
import com.rimalholdings.expensemanager.service.ExpenseService;
import com.rimalholdings.expensemanager.util.DateTimeUtil;

import com.fasterxml.jackson.databind.ObjectMapper;
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
	BillPaymentEntity billPaymentEntity = new BillPaymentEntity();
	billPaymentEntity.setPaymentAmount(billpayment.getPaymentAmount());
	billPaymentEntity.setPaymentMethod(billpayment.getPaymentMethod());

	billPaymentEntity.setPaymentReference(billpayment.getPaymentReference());
	billPaymentEntity.setPaymentDate(Timestamp.valueOf(billpayment.getPaymentDate()));
	billPaymentEntity.setCreatedDate(DateTimeUtil.getCurrentTimeInUTC());
	VendorEntity vendorEntity = new VendorEntity();
	vendorEntity.setId(billpayment.getVendorId());
	billPaymentEntity.setVendor(vendorEntity);

	Map<String, BigDecimal> expensePaymentMap = billpayment.getExpensePayments();

	if (!expensePaymentMap.isEmpty()) {
	expensePaymentMap.forEach(
		(expenseId, paymentAmount) -> {
			ExpenseEntity expenseEntity = expenseService.findById(Long.parseLong(expenseId));
			expenseEntity.setPaymentAmount(paymentAmount);

			log.info("amount due: {}", expenseEntity.getAmountDue());
			log.info("payment amount: {}", paymentAmount);
			Integer paymentStatus =
				paymentApplicationStatus(paymentAmount, expenseEntity.getAmountDue());
			billPaymentEntity.setPaymentApplicationStatus(paymentStatus);
			expenseEntity.setPaymentStatus(
				setPaymentStatusOnExpense(paymentAmount, expenseEntity.getAmountDue()));
			if (expenseEntity.getAmountDue().compareTo(BigDecimal.ZERO) == ZERO) {
			// Handle the case when the expense is already paid in full
			throw new CannotOverpayExpenseException("Expense already paid in full");
			}
			if (expenseEntity.getAmountDue().compareTo(paymentAmount) == 0) {
			expenseEntity.setAmountDue(BigDecimal.ZERO);
			} else {
			expenseEntity.setAmountDue(expenseEntity.getTotalAmount().subtract(paymentAmount));
			}
			billPaymentEntity.getExpenses().add(expenseEntity);
		});

	} else {
	log.info("expensePaymentMap is null, throwing exception");
	// TODO: Handle situations where expensePaymentMap is null
	throw new RuntimeException("no expense payments specified. Please specify expense payments");
	}

	return billPaymentEntity;
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
	if (paymentAmount.compareTo(amountDue) == 0) {
	return 2;
	} else if (paymentAmount.compareTo(amountDue) == 1) {
	return 1;
	} else {
	return 3;
	}
}

private Integer setPaymentStatusOnExpense(BigDecimal paymentAmount, BigDecimal amountDue) {
	// paymentstatus int not null COMMENT '1=partially paid ,2=fully paid ,3=unpaid, 4=unknown'
	if (paymentAmount.compareTo(amountDue) == 0) {
	return 2;
	} else if (paymentAmount.compareTo(amountDue) == 1) {
	return 1;
	} else {
	return 3;
	}
}
}
