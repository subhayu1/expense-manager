package com.rimalholdings.expensemanager.data.dto;
/**
 * Interface for BillPaymentInvoice. This is used to get the bill payment invoice details using native query.

 */

public interface BillPaymentInvoice {
    //SELECT bp.id AS id, v.name AS vendorName, e.externalorgid AS externalOrgId," +
    //			" e.externalinvoicenumber AS externalInvoiceNumber, e.description AS description," +
    //			"e.totalamount AS totalAmount, e.paymentamount as paymentAmount,e.amountdue as amountDue" +
    //			" FROM billpayment bp INNER JOIN billpayment_expense bpe ON bp.id = bpe.billpaymentid " +
    //			"INNER JOIN expense e ON bpe.expenseid = e.id INNER JOIN vendor v ON e.vendorid = v.id " +
    //			"WHERE e.externalorgid = :orgId ",
    Long getId();
    String getVendorName();
    Long getExternalOrgId();
    String getExternalInvoiceNumber();
    String getDescription();
    Double getTotalAmount();
    Double getPaymentAmount();
    Double getAmountDue();


}
