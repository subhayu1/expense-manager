/* (C)1 */
package com.rimalholdings.expensemanager.data.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "expense")
public class ExpenseEntity extends BaseEntity {

@JoinColumn(name = "vendorid", referencedColumnName = "id")
@ManyToOne
private VendorEntity vendor;

@Column(name = "totalamount")
private BigDecimal totalAmount;

@Column(name = "amountdue")
private BigDecimal amountDue;

@Column(name = "description")
private String description;

@Column(name = "duedate")
private Timestamp dueDate;

@ToString.Exclude
@EqualsAndHashCode.Exclude
@JsonBackReference
@ManyToMany(mappedBy = "expenses")
private List<BillPaymentEntity> payments = new ArrayList<>();

@Column(name = "paymentamount")
private BigDecimal paymentAmount;
}
