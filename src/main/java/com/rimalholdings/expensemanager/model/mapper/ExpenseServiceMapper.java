/* (C)1 */
package com.rimalholdings.expensemanager.model.mapper;

import java.sql.Timestamp;

import com.rimalholdings.expensemanager.data.dto.BaseDTOInterface;
import com.rimalholdings.expensemanager.data.dto.ExpenseDTO;
import com.rimalholdings.expensemanager.data.entity.ExpenseEntity;
import com.rimalholdings.expensemanager.data.entity.VendorEntity;
import com.rimalholdings.expensemanager.service.ExpenseService;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "ExpenseServiceMapper")
public class ExpenseServiceMapper extends AbstractServiceMapper<ExpenseEntity> {

private final ExpenseService expenseService;

protected ExpenseServiceMapper(ObjectMapper objectMapper, ExpenseService expenseService) {
	super(objectMapper);
	this.expenseService = expenseService;
}

@Override
public ExpenseEntity mapToDTO(BaseDTOInterface dtoInterface) {
	ExpenseDTO expenseDTO = (ExpenseDTO) dtoInterface;
	VendorEntity vendorEntity = new VendorEntity();
	vendorEntity.setId(expenseDTO.getVendorId());

	ExpenseEntity expenseEntity = new ExpenseEntity();
	expenseEntity.setId(expenseDTO.getId());
	expenseEntity.setDueDate(Timestamp.valueOf(expenseDTO.getDueDate()));
	expenseEntity.setVendor(vendorEntity);
	expenseEntity.setTotalAmount(expenseDTO.getTotalAmount());
	expenseEntity.setAmountDue(expenseDTO.getTotalAmount());
	expenseEntity.setDescription(expenseDTO.getDescription());
	return expenseEntity;
}

@Override
public void deleteEntity(Long id) {
	expenseService.deleteById(id);
}

@Override
public String getEntity(Long id) {
	return convertDtoToString(expenseService.findById(id));
}

@Override
public String saveOrUpdateEntity(BaseDTOInterface dtoInterface) {
	if (!(dtoInterface instanceof ExpenseDTO expenseDTO)) {
	throw new IllegalArgumentException("Invalid DTO type");
	}
	ExpenseEntity expenseEntity = mapToDTO(expenseDTO);
	ExpenseEntity savedExpense = expenseService.save(expenseEntity);

	return convertDtoToString(savedExpense);
}

@Override
public Page<ExpenseEntity> getAllEntities(Pageable pageable) {
	return expenseService.findAll(pageable);
}
}
