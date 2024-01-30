package com.rimalholdings.expensemanager.data.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
// private String vendorId;
//
// private String vendorNumber;
//
// private String documentNumber;
//
// private String externalDocumentNumber;
//
// private String appliesToInvoiceId;
//
// private String appliesToInvoiceNumber;
//
// private String description;
//
// private BigDecimal paymentAmount;
@Table(name = "billpayment")
@JsonInclude(JsonInclude.Include.NON_NULL)
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

@Column(name = "tosync")
private Boolean toSync;

@JoinColumn(name = "vendorid", referencedColumnName = "id")
@ManyToOne
@JsonIdentityReference(
	alwaysAsId = true) // This annotation changes the serialization from an object to its id
@ToString.Exclude
private VendorEntity vendor;
}
