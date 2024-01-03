package com.rimalholdings.expensemanager.data.dto;

import com.rimalholdings.expensemanager.data.entity.VendorEntity;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class ExpenseDTO {
  private Long id;
  private String vendorId;
  private String totalAmount;
  private BigDecimal amountPaid;
  private BigDecimal amountDue;
  private String description;
}
