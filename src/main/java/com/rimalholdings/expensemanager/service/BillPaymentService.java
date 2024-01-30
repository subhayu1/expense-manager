/* (C)1 */
package com.rimalholdings.expensemanager.service;

import java.util.List;

import com.rimalholdings.expensemanager.data.dao.BillPaymentRepository;
import com.rimalholdings.expensemanager.data.dto.VendorPaymentResults;
import com.rimalholdings.expensemanager.data.entity.BillPaymentEntity;

import org.springframework.stereotype.Service;

@Service
public class BillPaymentService extends AbstractEntityService<BillPaymentEntity> {

private final BillPaymentRepository billPaymentRepository;

public BillPaymentService(BillPaymentRepository billPaymentRepository) {
	super(billPaymentRepository);
	this.billPaymentRepository = billPaymentRepository;
}

public List<VendorPaymentResults> findExpenseAndVendorByBillPaymentId(Long id) {
	return billPaymentRepository.findExpenseAndVendorByBillPaymentId(id);
}
}
