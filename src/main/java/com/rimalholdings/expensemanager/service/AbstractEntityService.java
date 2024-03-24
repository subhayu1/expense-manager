/* (C)1 */
package com.rimalholdings.expensemanager.service;

import com.rimalholdings.expensemanager.data.dao.BaseRepository;
import com.rimalholdings.expensemanager.data.entity.BaseEntity;
import com.rimalholdings.expensemanager.exception.ExceptionConstant;
import com.rimalholdings.expensemanager.exception.ObjectNotFoundException;
import com.rimalholdings.expensemanager.exception.UpdateNotAllowedException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
	return repository
		.findById(id)
		.orElseThrow(
			() ->
				new ObjectNotFoundException(String.format(ExceptionConstant.OBJECT_NOT_FOUND, id)));
}

@Transactional
public void deleteById(Long id) throws DataIntegrityViolationException {
	try {
	T entity =
		repository
			.findById(id)
			.orElseThrow(
				() ->
					new ObjectNotFoundException(
						String.format(ExceptionConstant.OBJECT_NOT_FOUND, id)));
	repository.deleteById(entity.getId());
	} catch (DataIntegrityViolationException e) {
	throw new UpdateNotAllowedException(
		String.format(ExceptionConstant.OBJECT_INTEGRITY_VIOLATION, id));
	}
}

public Page<T> findAll(Pageable pageable) {
	return repository.findAll(pageable);
}
public List<T> findAll() {
	return repository.findAll();
}

public boolean existsById(Long id) {
	return repository.existsById(id);
}
}
