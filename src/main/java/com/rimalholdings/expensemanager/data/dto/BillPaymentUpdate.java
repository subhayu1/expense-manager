package com.rimalholdings.expensemanager.data.dto;

import lombok.Data;


@Data
public class BillPaymentUpdate {
String invoiceExternalDocumentNumber;
String integrationId;
String orgId;
}
