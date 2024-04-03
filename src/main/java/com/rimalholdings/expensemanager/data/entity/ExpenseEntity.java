/* (C)1 */
package com.rimalholdings.expensemanager.data.entity;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "expense")
public class ExpenseEntity extends BaseEntity {

// create table expense
// (
//    id            bigint auto_increment
//        primary key,
//    vendorid      bigint         not null,
//    externalinvoicenumber varchar(255)   null,
//    vendorinvoicenumber varchar(255)   null,
//    integrationid varchar(255)   null,
//    externalorgid int null,
//    totalamount   decimal(38, 2) not null,
//    amountdue     decimal(38, 2) not null,
//    paymentamount decimal(38, 2) null,
//    invoicedate  datetime(6)    null,
//
//    duedate       datetime(6)    null,
//    description   varchar(255)   null,
//    createddate   timestamp      null,
//    updateddate   timestamp      null,
//    paymentstatus int            not null comment '1=partially paid ,2=fully paid ,3=unpaid
// 4=unknown'
// );

@JsonIdentityReference(
	alwaysAsId = true) // This annotation changes the serialization from an object to its id
@ManyToOne
@ToString.Exclude
// @JsonManagedReference(value = "vendorid")
@JoinColumn(name = "vendorid", referencedColumnName = "id")
private VendorEntity vendor;

@Column(name = "totalamount")
private BigDecimal totalAmount;

@Column(name = "amountdue")
private BigDecimal amountDue;

@Column(name = "description")
private String description;

@Column(name = "duedate")
private Date dueDate;

@Column(name = "paymentamount")
private BigDecimal paymentAmount;

@Column(name = "paymentstatus")
private Integer paymentStatus;

@Column(name = "createddate")
private Timestamp createdDate;

@Column(name = "updateddate")
private Timestamp updatedDate;

@Column(name = "invoicedate")
private Date invoiceDate;

@Column(name = "externalinvoicenumber")
private String externalInvoiceNumber;

@Column(name = "vendorinvoicenumber")
private String vendorInvoiceNumber;

@Column(name = "integrationid")
private String integrationId;

@Column(name = "externalorgid")
private Integer externalOrgId;

@Column(name = "appaymentid")
private Integer apPaymentId;
}
