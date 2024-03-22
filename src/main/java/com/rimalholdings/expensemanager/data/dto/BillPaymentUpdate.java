package com.rimalholdings.expensemanager.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BillPaymentUpdate {
String invoiceExternalDocumentNumber;
String integrationId;
String orgId;
Boolean allowIntegration;
Long billPayId;
}
