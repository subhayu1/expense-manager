package com.rimalholdings.expensemanager.data.dto;

import java.math.BigDecimal;

public interface VendorPaymentResults {

String getVendorId();

String getVendorNumber();

String getDocumentNumber();

String getExternalDocumentNumber();

String getAppliesToInvoiceId();

String getAppliesToInvoiceNumber();

String getDescription();

BigDecimal getPaymentAmount();
}
