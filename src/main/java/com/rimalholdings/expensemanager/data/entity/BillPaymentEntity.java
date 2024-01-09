/* (C)1 */
package com.rimalholdings.expensemanager.data.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "billpayment")
public class BillPaymentEntity extends BaseEntity {

@ManyToMany(cascade = jakarta.persistence.CascadeType.PERSIST)
@JoinTable(
	name = "billpayment_expense",
	joinColumns = @JoinColumn(name = "billpaymentid", referencedColumnName = "id"),
	inverseJoinColumns = @JoinColumn(name = "expenseid", referencedColumnName = "id"))
@ToString.Exclude
@EqualsAndHashCode.Exclude
@JsonManagedReference
private List<ExpenseEntity> expenses = new ArrayList<>();

@Column(name = "paymentamount", nullable = false)
private BigDecimal paymentAmount;

@Column(name = "paymentmethod")
private Integer paymentMethod;

@Column(name = "paymentreference")
private String paymentReference;

@Column(name = "createddate")
private Timestamp createdDate;

@Column(name = "paymentapplicationstatus")
private Integer paymentApplicationStatus;

@Column(name = "paymentdate")
private Timestamp paymentDate;

@JoinColumn(name = "vendorid", referencedColumnName = "id")
@ManyToOne
private VendorEntity vendor;
}
