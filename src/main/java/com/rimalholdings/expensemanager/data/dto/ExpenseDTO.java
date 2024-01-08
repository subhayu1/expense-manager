/* (C)1 */
package com.rimalholdings.expensemanager.data.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ExpenseDTO implements BaseDTOInterface {

private Long id;
private Long vendorId;
private BigDecimal totalAmount;
private String description;
private String dueDate;
}
