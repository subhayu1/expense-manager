/* (C)1 */
package com.rimalholdings.expensemanager.data.dao;

import com.rimalholdings.expensemanager.data.entity.BillPaymentEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BillPaymentRepository extends BaseRepository<BillPaymentEntity> {

Page<BillPaymentEntity> findByVendorId(Long vendorId, Pageable pageable);
}
