package com.rimalholdings.expensemanager.service;

import com.rimalholdings.expensemanager.data.dao.BaseRepository;
import com.rimalholdings.expensemanager.data.dao.ExpenseRepository;
import com.rimalholdings.expensemanager.data.entity.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class ExpenseService extends AbstractEntityService<ExpenseEntity> {


  public ExpenseService(
      BaseRepository<ExpenseEntity> repository) {
    super(repository);
  }

  @Override
  public void deleteById(Long id) {

  }
}
