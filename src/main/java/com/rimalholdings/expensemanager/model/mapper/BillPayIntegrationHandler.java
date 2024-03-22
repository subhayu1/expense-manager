package com.rimalholdings.expensemanager.model.mapper;

import com.rimalholdings.expensemanager.data.dao.BillPaymentRepository;

import org.springframework.stereotype.Component;

@Component
public class BillPayIntegrationHandler {
private final BillPaymentRepository billPaymentRepository;

public BillPayIntegrationHandler(BillPaymentRepository billPaymentRepository) {
	this.billPaymentRepository = billPaymentRepository;
}

public void allowBillPaymentIntegration(Boolean allowIntegration, Long billPayId) {
	billPaymentRepository.allowBillPaymentIntegration(allowIntegration, billPayId);
}
}
