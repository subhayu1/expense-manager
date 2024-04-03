/* (C)1 */
package com.rimalholdings.expensemanager.data.dao;

import com.rimalholdings.expensemanager.data.entity.ExpenseEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ExpenseRepository
	extends JpaRepository<ExpenseEntity, Long>, JpaSpecificationExecutor<ExpenseEntity> {

@Modifying
@Transactional
@Query(
	value = "UPDATE expense e  SET e.appaymentid = :apPaymentId WHERE e.id = :expenseId",
	nativeQuery = true)
void updateExpenseWithApPaymentId(Integer expenseId, Integer apPaymentId);

ExpenseEntity findByIntegrationId(String integrationId);

boolean existsByIntegrationId(String integrationId);
}
