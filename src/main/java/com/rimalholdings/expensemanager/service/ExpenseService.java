/* (C)1 */
package com.rimalholdings.expensemanager.service;

import java.util.List;

import com.rimalholdings.expensemanager.data.dao.ExpenseRepository;
import com.rimalholdings.expensemanager.data.dao.VendorRepository;
import com.rimalholdings.expensemanager.data.entity.ExpenseEntity;
import com.rimalholdings.expensemanager.data.entity.VendorEntity;
import com.rimalholdings.expensemanager.exception.ObjectNotFoundException;
import com.rimalholdings.expensemanager.specification.ExpenseSpecification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExpenseService {
private final VendorRepository vendorRepository;
private final ExpenseRepository expenseRepository;

public ExpenseService(ExpenseRepository expenseRepository, VendorRepository vendorRepository) {
	this.vendorRepository = vendorRepository;
	this.expenseRepository = expenseRepository;
}

public void deleteById(Long id) {
	ExpenseEntity expenseEntity =
		expenseRepository
			.findById(id)
			.orElseThrow(
				() ->
					new ObjectNotFoundException(String.format("Expense with id %s not found", id)));
	expenseRepository.deleteById(expenseEntity.getId());
}

@Transactional
public void saveAll(List<ExpenseEntity> expenseEntities) {
	expenseRepository.saveAll(expenseEntities);
}

public Long getVendorIdFromVendorIntegrationId(String vendorIntegrationId) {

	VendorEntity vendorEntity = vendorRepository.findByIntegrationId(vendorIntegrationId);
	if (vendorEntity == null) {
	throw new ObjectNotFoundException(
		String.format("Vendor with integration id %s not found", vendorIntegrationId));
	}
	return vendorEntity.getId();
}

public Page<ExpenseEntity> getExpensesByExternalOrgId(Pageable pageable, Integer externalOrgId) {
	Specification<ExpenseEntity> spec = ExpenseSpecification.hasExternalOrgId(externalOrgId);
	if (externalOrgId != null) {
	spec = spec.and(ExpenseSpecification.hasExternalOrgId(externalOrgId));
	}
	return expenseRepository.findAll(spec, pageable);
}

public ExpenseEntity findById(Long id) {
	return expenseRepository
		.findById(id)
		.orElseThrow(
			() -> new ObjectNotFoundException(String.format("Expense with id %s not found", id)));
}

public ExpenseEntity save(ExpenseEntity expenseEntity) {
	return expenseRepository.save(expenseEntity);
}

public Page<ExpenseEntity> findAll(Pageable pageable) {
	return expenseRepository.findAll(pageable);
}

public ExpenseEntity findByIntegrationId(String integrationId) {
	return expenseRepository.findByIntegrationId(integrationId);
}

public boolean existsById(Long id) {
	return expenseRepository.existsById(id);
}

public boolean existsByIntegrationId(String integrationId) {
	return expenseRepository.existsByIntegrationId(integrationId);
}
}
