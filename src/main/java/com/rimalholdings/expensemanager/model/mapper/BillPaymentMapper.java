package com.rimalholdings.expensemanager.model.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rimalholdings.expensemanager.Exception.CannotOverpayExpenseException;
import com.rimalholdings.expensemanager.data.dto.BaseDTOInterface;
import com.rimalholdings.expensemanager.data.dto.BillPaymentDTO;
import com.rimalholdings.expensemanager.data.entity.BillPaymentEntity;
import com.rimalholdings.expensemanager.data.entity.ExpenseEntity;
import com.rimalholdings.expensemanager.data.entity.VendorEntity;
import com.rimalholdings.expensemanager.service.BillPaymentService;
import com.rimalholdings.expensemanager.service.ExpenseService;
import java.math.BigDecimal;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "BILL_PAYMENT_MAPPER")
public class BillPaymentMapper extends AbstractMapper<BillPaymentEntity> {

  private static final Integer ZERO = 0;
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
    billPaymentEntity.setPaymentAmount(billpaymentDTO.getPaymentAmount());
    billPaymentEntity.setPaymentMethod(billpaymentDTO.getPaymentMethod());
    billPaymentEntity.setPaymentReference(billpaymentDTO.getPaymentReference());
    VendorEntity vendorEntity = new VendorEntity();
    vendorEntity.setId(billpaymentDTO.getVendorId());
    billPaymentEntity.setVendor(vendorEntity);

    Map<Long, BigDecimal> expensePaymentMap = billpaymentDTO.getExpensePayments();
    if (expensePaymentMap != null) {
      expensePaymentMap.forEach((expenseId, paymentAmount) -> {
        ExpenseEntity expenseEntity = expenseService.findById(expenseId);
        expenseEntity.setPaymentAmount(paymentAmount);
        log.info("amount due: {}", expenseEntity.getAmountDue());
        log.info("payment amount: {}", paymentAmount);
        if (expenseEntity.getAmountDue().compareTo(BigDecimal.ZERO) == ZERO) {
          // Handle the case when the expense is already paid in full
          throw new CannotOverpayExpenseException("Expense already paid in full");
        }
        if (expenseEntity.getAmountDue().compareTo(paymentAmount) == 0) {
          expenseEntity.setAmountDue(BigDecimal.ZERO);
        } else {
          expenseEntity.setAmountDue(expenseEntity.getTotalAmount().subtract(paymentAmount));
        }
        billPaymentEntity.getExpenses().add(expenseEntity);
      });
    }

    return billPaymentEntity;
  }


  @Override
  public void deleteEntity(Long id) {
  }

  @Override
  public String getEntity(Long id) {
    return billPaymentService.findById(id).toString();
  }

  @Override
  public String saveOrUpdateEntity(BaseDTOInterface dtoInterface) {
    BillPaymentDTO billpaymentDTO = (BillPaymentDTO) dtoInterface;
    BillPaymentEntity billPaymentEntity = mapToDTO(billpaymentDTO);

    BillPaymentEntity savedBillPaymentEntity = billPaymentService.save(billPaymentEntity);
    return convertDtoToString(savedBillPaymentEntity);

  }


  @Override
  public Page<BillPaymentEntity> getAllEntities(Pageable pageable) {
    return null;
  }
}
