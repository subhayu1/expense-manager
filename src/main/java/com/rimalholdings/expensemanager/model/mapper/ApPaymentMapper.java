package com.rimalholdings.expensemanager.model.mapper;

import com.rimalholdings.expensemanager.data.dao.ApPaymentRepository;
import com.rimalholdings.expensemanager.data.dao.ExpenseRepository;
import com.rimalholdings.expensemanager.data.dto.BillPayment;
import com.rimalholdings.expensemanager.data.entity.ApPaymentEntity;
import com.rimalholdings.expensemanager.data.entity.BillPaymentEntity;
import com.rimalholdings.expensemanager.util.DateTimeUtil;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "ApPaymentMapper")
public class ApPaymentMapper {
private final ApPaymentRepository apPaymentRepository;
private final ExpenseRepository expenseRepository;

public ApPaymentMapper(
	ApPaymentRepository apPaymentRepository, ExpenseRepository expenseRepository) {
	this.apPaymentRepository = apPaymentRepository;
	this.expenseRepository = expenseRepository;
}

public Integer createApPayment(BillPaymentEntity billPayment) {
	ApPaymentEntity paymentEntity = new ApPaymentEntity();
	paymentEntity.setPaymentAmount(billPayment.getPaymentAmount());
	paymentEntity.setPaymentDate(DateTimeUtil.getCurrentDate());
	paymentEntity.setPaymentMethod(billPayment.getPaymentMethod());
	paymentEntity.setPaymentReference(billPayment.getPaymentReference());
	paymentEntity.setCreatedDate(DateTimeUtil.getCurrentDate());

	ApPaymentEntity response = apPaymentRepository.saveAndFlush(paymentEntity);
	return response.getId();
}

public Integer createApPayment(BillPayment billPayment) {
	ApPaymentEntity paymentEntity = new ApPaymentEntity();
	paymentEntity.setPaymentAmount(billPayment.getPaymentAmount());
	paymentEntity.setPaymentDate(DateTimeUtil.getCurrentDate());
	paymentEntity.setPaymentMethod(billPayment.getPaymentMethod());
	paymentEntity.setPaymentReference(billPayment.getPaymentReference());
	paymentEntity.setExternalOrgId(billPayment.getExternalOrgId());
	paymentEntity.setCreatedDate(DateTimeUtil.getCurrentDate());

	ApPaymentEntity response = apPaymentRepository.saveAndFlush(paymentEntity);
	return response.getId();
}

public void updateExpenseWithApPaymentId(Integer expenseId, Integer apPaymentId) {
	ApPaymentEntity apPaymentEntity = apPaymentRepository.findById(apPaymentId).orElseThrow();
	// Update expense with apPaymentId
	if ((apPaymentEntity).getId() != null) {
	log.info("Updating expense with apPaymentId: {}", apPaymentId);
	expenseRepository.updateExpenseWithApPaymentId(expenseId, apPaymentId);

	// Update expense with apPaymentId
	} else {
	throw new EntityNotFoundException("ApPayment with id " + apPaymentId + " not found");
	}
}
}
