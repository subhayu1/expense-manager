/* (C)1 */
package com.rimalholdings.expensemanager.data.dao;

import com.rimalholdings.expensemanager.data.entity.ExpenseEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ExpenseRepository
	extends JpaRepository<ExpenseEntity, Long>, JpaSpecificationExecutor<ExpenseEntity> {}
