package com.rimalholdings.expensemanager.sync;

import lombok.Data;

/** DTO to convert billPayment to VendorPayment */
@Data
public class VendorPayment {
// {
//    "journalId": "45018d36-26e9-ed11-8848-0022482c9c4a",
//    "journalDisplayName": "BDC_PJ",
//    "vendorId": "cfc0816a-abcc-ed11-9a88-0022482c9c4a",
//    "vendorNumber": "V00010",
//    "postingDate": "2024-01-20",
//    "documentNumber": "108276",
//    "externalDocumentNumber": "VALID-15",
//    "amount": 4400.00,
//    "appliesToInvoiceId": "9e6b9747-315d-ee11-be6d-6045bdefd5a8",
//    "appliesToInvoiceNumber": "108276",
//    "description": "Rimal  Holdings, LLC-expMgr",
//    "comment": "TEST"
// }
private String journalId;
private String journalDisplayName;
private String vendorId;
private String vendorNumber;
private String postingDate;
private String documentNumber;
private String externalDocumentNumber;
private Double amount;
private String appliesToInvoiceId;
private String appliesToInvoiceNumber;
private String description;
private String comment;

public void setJournalDisplayName(String journalDisplayName) {
	// hardcode to BANK
	this.journalDisplayName = "BANK";
}
}
