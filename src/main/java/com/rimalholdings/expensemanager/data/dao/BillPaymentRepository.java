/* (C)1 */
package com.rimalholdings.expensemanager.data.dao;

import java.util.List;

import com.rimalholdings.expensemanager.data.dto.VendorPaymentResults;
import com.rimalholdings.expensemanager.data.entity.BillPaymentEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BillPaymentRepository extends BaseRepository<BillPaymentEntity> {

Page<BillPaymentEntity> findByVendorId(Long vendorId, Pageable pageable);

// List<VendorPaymentResult> findExpenseAndVendorByBillPaymentId(@Param("id") Long id);
@Query(
	value =
		"SELECT v.integrationId AS vendorId, v.vendornumber AS vendorNumber, "
			+ "e.externalInvoiceNumber AS documentNumber, e.vendorInvoiceNumber AS externalDocumentNumber, "
			+ "e.integrationId AS appliesToInvoiceId, e.externalInvoiceNumber AS appliesToInvoiceNumber, "
			+ "e.description AS description, bp.paymentamount AS paymentAmount "
			+ "FROM billpayment bp "
			+ "INNER JOIN billpayment_expense bpe ON bp.id = bpe.billpaymentid "
			+ "INNER JOIN expense e ON bpe.expenseid = e.id "
			+ "INNER JOIN vendor v ON e.vendorId = v.id "
			+ "WHERE bp.id = :id AND bp.toSync = 1",
	nativeQuery = true)
List<VendorPaymentResults> findExpenseAndVendorByBillPaymentId(@Param("id") Long id);
}
