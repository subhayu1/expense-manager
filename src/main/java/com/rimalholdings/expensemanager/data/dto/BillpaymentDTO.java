package com.rimalholdings.expensemanager.data.dto;

import java.math.BigDecimal;

public class BillpaymentDTO implements BaseDTOInterface {

  private Long id;
  private Long vendorId;
  private BigDecimal totalAmount;
  private BigDecimal amountPaid;
  private BigDecimal amountDue;
  private String description;

}
