package com.rimalholdings.expensemanager.model.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rimalholdings.expensemanager.data.dto.BaseDTOInterface;
import com.rimalholdings.expensemanager.data.dto.BillPaymentDTO;
import com.rimalholdings.expensemanager.data.entity.BillPaymentEntity;
import com.rimalholdings.expensemanager.data.entity.ExpenseEntity;
import com.rimalholdings.expensemanager.data.entity.VendorEntity;
import com.rimalholdings.expensemanager.service.BillPaymentService;
import com.rimalholdings.expensemanager.service.ExpenseService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BillPaymentMapper extends AbstractMapper<BillPaymentEntity>{
  private final BillPaymentService billPaymentService;
  private final ExpenseService expenseService;

  protected BillPaymentMapper(ObjectMapper objectMapper, BillPaymentService billPaymentService,
      ExpenseService expenseService) {
    super(objectMapper);
    this.billPaymentService = billPaymentService;
    this.expenseService = expenseService;
  }

 @Override
public BillPaymentEntity mapToDTO(BaseDTOInterface dtoInterface) {
  BillPaymentDTO billpaymentDTO = (BillPaymentDTO) dtoInterface;
  BillPaymentEntity billPaymentEntity = new BillPaymentEntity();
  billPaymentEntity.setId(billpaymentDTO.getId());
  billPaymentEntity.setPaymentAmount(billpaymentDTO.getPaymentAmount());
  billPaymentEntity.setPaymentMethod(billpaymentDTO.getPaymentMethod());
  billPaymentEntity.setPaymentReference(billpaymentDTO.getPaymentReference());

  VendorEntity vendorEntity = new VendorEntity();
  vendorEntity.setId(billpaymentDTO.getVendorId());
  billPaymentEntity.setVendor(vendorEntity);

  billpaymentDTO.getExpensePaymentMap().forEach((expenseId, paymentAmount) -> {
    ExpenseEntity expenseEntity = expenseService.findById(expenseId);
    expenseEntity.setPaymentAmount(paymentAmount);
    expenseEntity.setAmountDue(expenseEntity.getTotalAmount().subtract(paymentAmount));
    billPaymentEntity.getExpenses().add(expenseEntity);
  });

  return billPaymentEntity;
}


  @Override
  public void deleteEntity(Long id) {

  }

  @Override
  public String getEntity(Long id) {
    return null;
  }

  @Override
  public String saveOrUpdateEntity(BaseDTOInterface dtoInterface) {
    if(!(dtoInterface instanceof BillPaymentDTO billpaymentDTO)){
      throw new IllegalArgumentException("Invalid DTO type");
    }
    BillPaymentEntity billPaymentEntity = mapToDTO(billpaymentDTO);
    BillPaymentEntity savedBillPaymentEntity = billPaymentService.save(billPaymentEntity);
    return convertDtoToString(savedBillPaymentEntity);

  }

  @Override
  public Page<BillPaymentEntity> getAllEntities(Pageable pageable) {
    return null;
  }
}
