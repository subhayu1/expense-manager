package com.rimalholdings.expensemanager.model.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.rimalholdings.expensemanager.data.dao.BillPaymentRepository;
import com.rimalholdings.expensemanager.data.dto.VendorPaymentResults;
import com.rimalholdings.expensemanager.service.BillPaymentService;
import com.rimalholdings.expensemanager.sync.MessageWrapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BillPayIntegrationHandlerTest {
@InjectMocks BillPayIntegrationHandler billPayIntegrationHandler;
@Mock BillPaymentRepository billPaymentRepository;
@Mock BillPaymentService billPaymentService;

@BeforeEach
void setUp() {
	MockitoAnnotations.openMocks(this);
}

@Test
@DisplayName("Allow bill payment integration")
void allowBillPaymentIntegration() {
	Long billPayId = 1L;
	Boolean allowIntegration = true;
	billPayIntegrationHandler.allowBillPaymentIntegration(allowIntegration, billPayId);
	verify(billPaymentRepository).allowBillPaymentIntegration(allowIntegration, billPayId);
}

@Test
@DisplayName("Should not allow bill payment integration")
void allowBillPaymentIntegrationFalse() {
	Long billPayId = 1L;
	Boolean allowIntegration = false;
	billPayIntegrationHandler.allowBillPaymentIntegration(allowIntegration, billPayId);
	verify(billPaymentRepository, times(1))
		.allowBillPaymentIntegration(allowIntegration, billPayId);
}

@Test
@DisplayName("Clear integration id")
void clearIntegrationId() {
	Long billPayId = 1L;
	billPayIntegrationHandler.clearIntegrationId(billPayId);
	verify(billPaymentRepository, times(1)).clearIntegrationId(billPayId);
}

@Test
void updateBillPayWithIntegrationId() {
	// String invoiceExternalDocumentNumber, Long orgId, String integrationId)
	String invoiceExternalDocumentNumber = "123";
	Long orgId = 1L;
	String integrationId = "123";
	when(billPaymentService.findBillPaymentIdByExternalInvoiceNumber(
			invoiceExternalDocumentNumber, orgId))
		.thenReturn(1L);
	when(billPaymentService.existsById(1L)).thenReturn(true);
	billPayIntegrationHandler.updateBillPayWithIntegrationId(
		invoiceExternalDocumentNumber, orgId, integrationId);
	verify(billPaymentService)
		.findBillPaymentIdByExternalInvoiceNumber(invoiceExternalDocumentNumber, orgId);
	verify(billPaymentService).updateBillPaymentIntegrationId(1L, integrationId);
}

@Test
@DisplayName("Map bill pay for sync service with valid orgId")
void mapBillPayForSyncService_ValidOrgId() {
	Long orgId = 1L;
	List<VendorPaymentResults> vendorPaymentResults = new ArrayList<>();
	when(billPaymentService.findExpenseAndVendorByBillPaymentId(orgId))
		.thenReturn(vendorPaymentResults);

	MessageWrapper<VendorPaymentResults> result =
		billPayIntegrationHandler.mapBillPayForSyncService(orgId);

	verify(billPaymentService).findExpenseAndVendorByBillPaymentId(orgId);
	assertEquals(vendorPaymentResults, result.getMessage());
	assertEquals(String.valueOf(orgId), result.getExternalOrgId());
	assertEquals("billPayments", result.getEntityName());
}

@Test
@DisplayName("Map bill pay for sync service with invalid orgId")
void mapBillPayForSyncService_InvalidOrgId() {
	Long orgId = -1L;
	when(billPaymentService.findExpenseAndVendorByBillPaymentId(orgId))
		.thenReturn(Collections.emptyList());

	MessageWrapper<VendorPaymentResults> result =
		billPayIntegrationHandler.mapBillPayForSyncService(orgId);

	verify(billPaymentService).findExpenseAndVendorByBillPaymentId(orgId);
	assertTrue(result.getMessage().isEmpty());
	assertEquals(String.valueOf(orgId), result.getExternalOrgId());
	assertEquals("billPayments", result.getEntityName());
}
}
