/* (C)1 */
package com.rimalholdings.expensemanager.data.dto;

import java.math.BigDecimal;
import java.util.Map;

import lombok.Data;

@Data
public class BillPaymentDTO implements BaseDTOInterface {

private Long id;
private Long vendorId;
private BigDecimal paymentAmount;
private String paymentMethod;
private String paymentReference;
private Map<String, BigDecimal> expensePayments;
}
