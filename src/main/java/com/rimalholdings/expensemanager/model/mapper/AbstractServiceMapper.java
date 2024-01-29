/* (C)1 */
package com.rimalholdings.expensemanager.model.mapper;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.rimalholdings.expensemanager.data.dto.BaseDTOInterface;
import com.rimalholdings.expensemanager.exception.DuplicateIdException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Slf4j(topic = "AbstractServiceMapper")
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
	log.debug("Error converting VendorDTO to JSON string: {}", e.getMessage());
	}
	return null;
}

public List<T> convertDtoToString(List<T> dto) {
	try {
	return objectMapper.readValue(objectMapper.writeValueAsString(dto), List.class);
	} catch (JsonProcessingException e) {
	throw new RuntimeException(e);
	}
}

public <T> void setIfNotNull(Supplier<T> getter, Consumer<T> setter) {
	Optional.ofNullable(getter.get()).ifPresent(setter);
}

public abstract T mapToDTO(BaseDTOInterface dtoInterface);

public abstract void deleteEntity(Long id);

public abstract String getEntity(Long id);

public abstract String saveOrUpdateEntity(BaseDTOInterface dtoInterface)
	throws DuplicateIdException;

public abstract Page<T> getAllEntities(Pageable pageable);

public abstract T getEntityForUpdate(Long id);
}
