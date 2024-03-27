package com.rimalholdings.expensemanager.data.dao;

import com.rimalholdings.expensemanager.data.entity.ApPaymentEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ApPaymentRepository
	extends JpaRepository<ApPaymentEntity, Integer>, JpaSpecificationExecutor<ApPaymentEntity> {}
