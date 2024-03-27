package com.rimalholdings.expensemanager.data.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "appayment")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApPaymentEntity {
// CREATE TABLE appayment (
//                           id INTEGER PRIMARY KEY AUTO_INCREMENT,
//                           paymentamount DECIMAL(10,2) NOT NULL,
//                           paymentdate DATE NOT NULL,
//                           paymentmethod INTEGER NOT NULL, -- Removed (255) as it's not
// applicable
//                           paymentreference VARCHAR(255) NOT NULL,
//                           externalorgid INTEGER ,
//                           createddate TIMESTAMP DEFAULT CURRENT_TIMESTAMP
@Id
@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
@Column(name = "id")
private Integer id;

@Column(name = "paymentamount", nullable = false)
private BigDecimal paymentAmount;

@Column(name = "paymentdate", nullable = false)
private java.sql.Date paymentDate;

@Column(name = "paymentmethod", nullable = false)
private Integer paymentMethod;

@Column(name = "paymentreference", nullable = false)
private String paymentReference;

@Column(name = "externalorgid")
private Integer externalOrgId;

@Column(name = "createddate", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
private java.sql.Date createdDate;
}
