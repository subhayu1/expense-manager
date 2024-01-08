/* (C)1 */
package com.rimalholdings.expensemanager.model.mapper;

import java.math.BigDecimal;
import java.util.Map;

import com.rimalholdings.expensemanager.data.dto.BaseDTOInterface;
import com.rimalholdings.expensemanager.data.dto.BillPayment;
import com.rimalholdings.expensemanager.data.entity.BillPaymentEntity;
import com.rimalholdings.expensemanager.data.entity.ExpenseEntity;
import com.rimalholdings.expensemanager.data.entity.VendorEntity;
import com.rimalholdings.expensemanager.exception.CannotOverpayExpenseException;
import com.rimalholdings.expensemanager.service.BillPaymentService;
import com.rimalholdings.expensemanager.service.ExpenseService;

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
	VendorEntity vendorEntity = new VendorEntity();
	vendorEntity.setId(billpayment.getVendorId());
	billPaymentEntity.setVendor(vendorEntity);

	Map<String, BigDecimal> expensePaymentMap = billpayment.getExpensePayments();
	if (expensePaymentMap != null) {
	expensePaymentMap.forEach(
		(expenseId, paymentAmount) -> {
			ExpenseEntity expenseEntity = expenseService.findById(Long.parseLong(expenseId));
			expenseEntity.setPaymentAmount(paymentAmount);
			log.info("amount due: {}", expenseEntity.getAmountDue());
			log.info("payment amount: {}", paymentAmount);
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
}
