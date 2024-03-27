package com.rimalholdings.expensemanager.data.dto;

import java.math.BigDecimal;
import java.sql.Date;

import lombok.Data;

@Data
public class ApPayment {
private Integer id;
private BigDecimal paymentAmount;
private Date paymentDate;
private Integer paymentMethod;
private String paymentReference;
private Integer externalOrgId;
private Date createdDate;
}
