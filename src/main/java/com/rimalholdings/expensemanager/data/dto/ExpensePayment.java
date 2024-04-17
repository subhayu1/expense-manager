package com.rimalholdings.expensemanager.data.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ExpensePayment {
private Long vendorId;
private Long expenseId;
private BigDecimal paymentAmount;
}
