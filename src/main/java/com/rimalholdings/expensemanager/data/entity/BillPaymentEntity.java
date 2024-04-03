package com.rimalholdings.expensemanager.data.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "billpayment")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BillPaymentEntity extends BaseEntity {
@Id
@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
private Long id;

@ToString.Exclude
@EqualsAndHashCode.Exclude
@JsonManagedReference
@JsonIdentityReference(alwaysAsId = true)
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

@Column(name = "integrationid")
private String integrationId;

@JoinColumn(name = "vendorid", referencedColumnName = "id")
@ManyToOne
@JsonIdentityReference(
	alwaysAsId = true) // This annotation changes the serialization from an object to its id
@ToString.Exclude
private VendorEntity vendor;

// private List<ExpenseEntity> expenses = new ArrayList<>();

@ManyToOne
@JoinColumn(name = "appaymentid", referencedColumnName = "id")
@JsonIdentityReference(alwaysAsId = true)
private ApPaymentEntity apPayment;

@OneToOne
@JoinColumn(name = "expenseid", referencedColumnName = "id")
@JsonIdentityReference(alwaysAsId = true)
private ExpenseEntity expense;
}
