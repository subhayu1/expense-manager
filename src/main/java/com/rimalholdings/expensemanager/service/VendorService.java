/* (C)1 */
package com.rimalholdings.expensemanager.service;

import com.rimalholdings.expensemanager.data.dao.BaseRepository;
import com.rimalholdings.expensemanager.data.dao.VendorRepository;
import com.rimalholdings.expensemanager.data.entity.VendorEntity;
import com.rimalholdings.expensemanager.exception.ExceptionConstant;
import com.rimalholdings.expensemanager.exception.ObjectNotFoundException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class VendorService extends AbstractEntityService<VendorEntity> {
private final VendorRepository vendorRepository;

public VendorService(BaseRepository<VendorEntity> repository, VendorRepository vendorRepository) {
	super(repository);
	this.vendorRepository = vendorRepository;
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

public VendorEntity findByIntegrationId(String integrationId) {
	return vendorRepository.findByIntegrationId(integrationId);
}
}
