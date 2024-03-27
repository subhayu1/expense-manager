/* (C)1 */
package com.rimalholdings.expensemanager.data.dao;

import java.util.List;

import com.rimalholdings.expensemanager.data.dto.BillPaymentInvoice;
import com.rimalholdings.expensemanager.data.dto.VendorPaymentResults;
import com.rimalholdings.expensemanager.data.entity.BillPaymentEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface BillPaymentRepository extends BaseRepository<BillPaymentEntity> {

Page<BillPaymentEntity> findByVendorId(Long vendorId, Pageable pageable);

@Query(
	value =
		"SELECT bp.id AS billPayId, v.integrationId AS vendorId, v.vendornumber AS vendorNumber, "
			+ "e.externalInvoiceNumber AS documentNumber, e.vendorInvoiceNumber AS externalDocumentNumber, "
			+ "e.integrationId AS appliesToInvoiceId, e.externalInvoiceNumber AS appliesToInvoiceNumber, "
			+ "e.description AS description, bp.paymentamount AS amount "
			+ "FROM billpayment bp "
			+ "INNER JOIN appayment ap on bp.appaymentid = ap.id "
			+ "INNER JOIN expense e on ap.id = e.appaymentid "
			+ "INNER JOIN vendor v ON e.vendorId = v.id "
			+ "WHERE e.externalorgid = :orgId AND bp.toSync = 1",
	nativeQuery = true)
List<VendorPaymentResults> findExpenseAndVendorByBillPaymentId(@Param("orgId") Long id);

@Transactional
@Modifying
@Query(
	value =
		"UPDATE billpayment bp "
			+ "SET bp.integrationid = :integrationId, bp.tosync = 0 "
			+ "WHERE bp.id = :billPaymentId",
	nativeQuery = true)
void updateBillPaymentIntegrationId(
	@Param("billPaymentId") Long billPaymentId, @Param("integrationId") String integrationId);

@Query(
	value =
		"SELECT bp.id AS billpayId "
			+ "FROM billpayment bp "
			+ "INNER JOIN billpayment_expense bpe ON bp.id = bpe.billpaymentid "
			+ "INNER JOIN expense e ON bpe.expenseid = e.id "
			+ "WHERE e.externalorgid=:orgId "
			+ "AND e.externalinvoicenumber= :invoiceExternalDocumentNumber",
	nativeQuery = true)
Long findBillPaymentIdByExternalInvoiceNumber(
	@Param("invoiceExternalDocumentNumber") String invoiceExternalDocumentNumber,
	@Param("orgId") Long orgId);

@Transactional
@Modifying
@Query(
	value = "update billpayment bp set bp.tosync = :allowIntegration where bp.id = :billPayId",
	nativeQuery = true)
void allowBillPaymentIntegration(Boolean allowIntegration, Long billPayId);

@Transactional
@Modifying
@Query(
	value = "update billpayment bp set bp.integrationid = null where bp.id = :billPayId",
	nativeQuery = true)
void clearIntegrationId(Long billPayId);

@Query(
	// SELECT bp.id AS id, v.name AS vendorName, e.externalorgid AS externalOrgId,
	//       e.externalinvoicenumber AS externalInvoiceNumber,
	//      e.description AS description,e.totalamount AS totalAmount,
	//       e.paymentamount as paymentAmount,e.amountdue as amountDue
	// FROM billpayment bp
	// INNER JOIN billpayment_expense bpe ON bp.id = bpe.billpaymentid
	// INNER JOIN expense e ON bpe.expenseid = e.id
	// INNER JOIN vendor v ON e.vendorid = v.id;
	value =
		"SELECT bp.id AS id, v.name AS vendorName, e.externalorgid AS externalOrgId,"
			+ " e.externalinvoicenumber AS externalInvoiceNumber, e.description AS description,"
			+ "e.totalamount AS totalAmount, e.paymentamount as paymentAmount,e.amountdue as amountDue"
			+ " FROM billpayment bp INNER JOIN billpayment_expense bpe ON bp.id = bpe.billpaymentid "
			+ "INNER JOIN expense e ON bpe.expenseid = e.id INNER JOIN vendor v ON e.vendorid = v.id "
			+ "WHERE e.externalorgid = :orgId ",
	nativeQuery = true)
List<BillPaymentInvoice> getBillPayments(@Param("orgId") Long id);
}
