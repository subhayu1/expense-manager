/* (C)1 */
package com.rimalholdings.expensemanager.service;

import java.util.List;

import com.rimalholdings.expensemanager.data.dao.BaseRepository;
import com.rimalholdings.expensemanager.data.dao.VendorRepository;
import com.rimalholdings.expensemanager.data.entity.ExpenseEntity;
import com.rimalholdings.expensemanager.data.entity.VendorEntity;
import com.rimalholdings.expensemanager.exception.ObjectNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExpenseService extends AbstractEntityService<ExpenseEntity> {
private final VendorRepository vendorRepository;

public ExpenseService(
	BaseRepository<ExpenseEntity> repository, VendorRepository vendorRepository) {
	super(repository);
	this.vendorRepository = vendorRepository;
}

@Override
public void deleteById(Long id) {
	ExpenseEntity expenseEntity =
		getRepository()
			.findById(id)
			.orElseThrow(
				() ->
					new ObjectNotFoundException(String.format("Expense with id %s not found", id)));
	getRepository().deleteById(expenseEntity.getId());
}

@Transactional
public void saveAll(List<ExpenseEntity> expenseEntities) {
	getRepository().saveAll(expenseEntities);
}

public Long getVendorIdFromVendorIntegrationId(String vendorIntegrationId) {

	VendorEntity vendorEntity = vendorRepository.findByIntegrationId(vendorIntegrationId);
	if (vendorEntity == null) {
	throw new ObjectNotFoundException(
		String.format("Vendor with integration id %s not found", vendorIntegrationId));
	}
	return vendorEntity.getId();
}
}
