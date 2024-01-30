package com.rimalholdings.expensemanager.data.dao;

// @Repository
// public interface BillPaySyncRepository extends JpaRepository<VendorPaymentResult,Long> {
//    @Query(value = """
//        SELECT
//            v.integrationId AS vendorId,
//            v.vendornumber AS vendorNumber,
//            e.externalInvoiceNumber AS documentNumber,
//            e.vendorInvoiceNumber AS externalDocumentNumber,
//            e.integrationId AS appliesToInvoiceId,
//            e.externalInvoiceNumber AS appliesToInvoiceNumber,
//            e.description as description,
//            bp.id AS comment,
//            bp.paymentamount as paymentAmount,
//            bp.createddate as createdDate
//        FROM
//            billpayment bp
//        INNER JOIN
//            billpayment_expense bpe
//                ON bp.id = bpe.billpaymentid
//        INNER JOIN
//            expense e
//                ON bpe.expenseid = e.id
//        INNER JOIN
//            vendor v
//                ON e.vendorid = v.id
//        WHERE
//            bp.id = :id
//            AND bp.toSync = 1
//    """, nativeQuery = true)
//    VendorPaymentResult findByIdWithExpenseAndVendor(@Param("id") Long id);
// }
