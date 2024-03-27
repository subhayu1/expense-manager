package com.rimalholdings.expensemanager.model.mapper;

import java.util.Collections;
import java.util.List;

import com.rimalholdings.expensemanager.data.dao.BillPaymentRepository;
import com.rimalholdings.expensemanager.data.dto.BillPaymentInvoice;
import com.rimalholdings.expensemanager.data.dto.VendorPaymentResults;
import com.rimalholdings.expensemanager.exception.ObjectNotFoundException;
import com.rimalholdings.expensemanager.service.BillPaymentService;
import com.rimalholdings.expensemanager.sync.MessageWrapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j(topic = "BillPayIntegrationHandler")
public class BillPayIntegrationHandler {
private final BillPaymentRepository billPaymentRepository;
private final BillPaymentService billPaymentService;

public BillPayIntegrationHandler(
	BillPaymentRepository billPaymentRepository, BillPaymentService billPaymentService) {
	this.billPaymentRepository = billPaymentRepository;
	this.billPaymentService = billPaymentService;
}

public void allowBillPaymentIntegration(Boolean allowIntegration, Long billPayId) {
	billPaymentRepository.allowBillPaymentIntegration(allowIntegration, billPayId);
}

public void clearIntegrationId(Long billPayId) {
	billPaymentRepository.clearIntegrationId(billPayId);
}

public void updateBillPayWithIntegrationId(
	String invoiceExternalDocumentNumber, Long orgId, String integrationId) {
	Long billPaymentIdFromDb =
		billPaymentService.findBillPaymentIdByExternalInvoiceNumber(
			invoiceExternalDocumentNumber, orgId);
	if (billPaymentService.existsById(billPaymentIdFromDb)) {
	log.info("Updating bill payment with integration id: {}", integrationId);
	billPaymentService.updateBillPaymentIntegrationId(billPaymentIdFromDb, integrationId);
	} else {
	throw new ObjectNotFoundException("Bill payment with id " + orgId + " not found");
	}
}

public MessageWrapper<VendorPaymentResults> mapBillPayForSyncService(Long orgId) {
	MessageWrapper<VendorPaymentResults> mappedBillPay = new MessageWrapper<>();
	List<VendorPaymentResults> vendorPaymentResults =
		billPaymentService.findExpenseAndVendorByBillPaymentId(orgId);
	log.info("billPayments: {}", Collections.singletonList(vendorPaymentResults));
	mappedBillPay.setMessage(vendorPaymentResults);
	mappedBillPay.setExternalOrgId(String.valueOf(orgId));
	mappedBillPay.setEntityName("billPayments");
	return mappedBillPay;
}

public MessageWrapper<BillPaymentInvoice> getBillPaymentInvoices(Long orgId) {
	MessageWrapper<BillPaymentInvoice> BillPaymentInvoiceData = new MessageWrapper<>();
	List<BillPaymentInvoice> billPaymentInvoices = billPaymentRepository.getBillPayments(orgId);
	BillPaymentInvoiceData.setMessage(billPaymentInvoices);
	BillPaymentInvoiceData.setExternalOrgId(String.valueOf(orgId));
	BillPaymentInvoiceData.setEntityName("billPayments");
	return BillPaymentInvoiceData;
}
}
