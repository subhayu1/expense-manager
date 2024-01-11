/* (C)1 */
package com.rimalholdings.expensemanager.service;

import com.rimalholdings.expensemanager.data.dao.BillPaymentRepository;
import com.rimalholdings.expensemanager.data.entity.BillPaymentEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BillPaymentService extends AbstractEntityService<BillPaymentEntity> {

private final BillPaymentRepository billPaymentRepository;

public BillPaymentService(BillPaymentRepository billPaymentRepository) {
	super(billPaymentRepository);
	this.billPaymentRepository = billPaymentRepository;
}
}
