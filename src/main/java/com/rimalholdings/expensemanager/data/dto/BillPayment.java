/* (C)1 */
package com.rimalholdings.expensemanager.data.dto;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BillPayment implements BaseDTOInterface {
private Long id;
// private Long vendorId;
private BigDecimal paymentAmount;
private String paymentDate;
private Integer paymentMethod;
private String paymentReference;
private Boolean toSync;
private Integer apPaymentId;
private Integer externalOrgId;
private List<Long> billPaymentIds;
private List<ExpensePayment> expensePayments;
}
