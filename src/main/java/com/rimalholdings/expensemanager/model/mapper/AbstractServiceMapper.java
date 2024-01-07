/* (C)1 */
package com.rimalholdings.expensemanager.model.mapper;

import java.util.List;

import com.rimalholdings.expensemanager.Exception.DuplicateIdException;
import com.rimalholdings.expensemanager.data.dto.BaseDTOInterface;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public abstract class AbstractServiceMapper<T> {

private final ObjectMapper objectMapper;

protected AbstractServiceMapper(ObjectMapper objectMapper) {
	this.objectMapper = objectMapper;
}

public String convertDtoToString(T dto) {
	try {
	// Convert the VendorDTO object to a JSON string
	return objectMapper.writeValueAsString(dto);
	} catch (JsonProcessingException e) {
	throw new RuntimeException(e);
	}
}

public List<T> convertDtoToString(List<T> dto) {
	try {
	return objectMapper.readValue(objectMapper.writeValueAsString(dto), List.class);
	} catch (JsonProcessingException e) {
	throw new RuntimeException(e);
	}
}

public abstract T mapToDTO(BaseDTOInterface dtoInterface);

public abstract void deleteEntity(Long id);

public abstract String getEntity(Long id);

public abstract String saveOrUpdateEntity(BaseDTOInterface dtoInterface)
	throws DuplicateIdException;

public abstract Page<T> getAllEntities(Pageable pageable);
}