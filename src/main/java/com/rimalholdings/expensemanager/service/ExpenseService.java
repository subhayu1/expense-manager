/* (C)1 */
package com.rimalholdings.expensemanager.service;

import com.rimalholdings.expensemanager.Exception.ObjectNotFoundException;
import com.rimalholdings.expensemanager.data.dao.BaseRepository;
import com.rimalholdings.expensemanager.data.entity.ExpenseEntity;

import org.springframework.stereotype.Service;

@Service
public class ExpenseService extends AbstractEntityService<ExpenseEntity> {

public ExpenseService(BaseRepository<ExpenseEntity> repository) {
	super(repository);
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
}
