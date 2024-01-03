package com.rimalholdings.expensemanager.service;

import com.rimalholdings.expensemanager.Exception.ExceptionConstant;
import com.rimalholdings.expensemanager.Exception.ObjectNotFoundException;
import com.rimalholdings.expensemanager.data.dao.BaseRepository;
import com.rimalholdings.expensemanager.data.entity.BaseEntity;
import org.springframework.transaction.annotation.Transactional;

public abstract class AbstractEntityService<T extends BaseEntity> {
  private final BaseRepository<T> repository;

  public AbstractEntityService(BaseRepository<T> repository) {
    this.repository = repository;
  }
  protected BaseRepository<T> getRepository() {
  return repository;
}

  @Transactional
  public T save(T entity) {
   return repository.save(entity);
  }

  @Transactional
  public T findById(Long id) {
    return repository.findById(id).orElse(null);
  }

  @Transactional
  public void deleteById(Long id) {
    T entity = repository.findById(id)
        .orElseThrow(() -> new ObjectNotFoundException(
            String.format(
                ExceptionConstant.OBJECT_NOT_FOUND,ExceptionConstant.VENDOR, id
            )));
    repository.deleteById(entity.getId());
  }
}