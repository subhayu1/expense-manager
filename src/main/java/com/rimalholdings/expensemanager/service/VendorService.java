/* (C)1 */
package com.rimalholdings.expensemanager.service;

import com.rimalholdings.expensemanager.data.dao.BaseRepository;
import com.rimalholdings.expensemanager.data.entity.VendorEntity;
import com.rimalholdings.expensemanager.exception.ExceptionConstant;
import com.rimalholdings.expensemanager.exception.ObjectNotFoundException;

import java.sql.SQLIntegrityConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class VendorService extends AbstractEntityService<VendorEntity> {

public VendorService(BaseRepository<VendorEntity> repository) {
	super(repository);
}

@Override
public void deleteById(Long vendorId) throws DataIntegrityViolationException {
	VendorEntity vendorEntity =
		getRepository()
			.findById(vendorId)
			.orElseThrow(
				() ->
					new ObjectNotFoundException(
						String.format(
							ExceptionConstant.OBJECT_NOT_FOUND,
							ExceptionConstant.VENDOR,
							vendorId)));
	getRepository().deleteById(vendorEntity.getId());
}
}
