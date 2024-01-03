package com.rimalholdings.expensemanager.model.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rimalholdings.expensemanager.data.dto.BaseDTOInterface;
import com.rimalholdings.expensemanager.data.dto.ExpenseDTO;
import com.rimalholdings.expensemanager.data.entity.ExpenseEntity;
import com.rimalholdings.expensemanager.data.entity.VendorEntity;
import com.rimalholdings.expensemanager.service.ExpenseService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "ExpenseMapper")
public class ExpenseMapper extends AbstractMapper<ExpenseEntity> {

  private final ExpenseService expenseService;


  protected ExpenseMapper(ObjectMapper objectMapper, ExpenseService expenseService) {
    super(objectMapper);
    this.expenseService = expenseService;
  }

  @Override
  public ExpenseEntity mapToDTO(BaseDTOInterface dtoInterface) {
    if (!(dtoInterface instanceof ExpenseDTO expenseDTO)) {
      throw new IllegalArgumentException("Invalid DTO type");
    }
    VendorEntity vendorEntity = new VendorEntity();
    vendorEntity.setId(expenseDTO.getVendorId());

    ExpenseEntity expenseEntity = new ExpenseEntity();
    expenseEntity.setId(expenseDTO.getId());
    expenseEntity.setVendor(vendorEntity);
    expenseEntity.setTotalAmount(expenseDTO.getTotalAmount());
    expenseEntity.setAmountPaid(expenseDTO.getAmountPaid());
    expenseEntity.setAmountDue(expenseDTO.getAmountDue());
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
  public List<ExpenseEntity> getAllEntities() {
    List<ExpenseEntity> expenseEntities = expenseService.findAll();
    return convertDtoToString(expenseEntities);
  }
}
