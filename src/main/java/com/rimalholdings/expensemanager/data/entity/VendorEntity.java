/* (C)1 */
package com.rimalholdings.expensemanager.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "vendor")
public class VendorEntity extends BaseEntity {

@Column(name = "externalid", length = 30, nullable = false, unique = true)
private String externalId;

@Column(name = "name")
private String name;

@Column(name = "address1")
private String address1;

@Column(name = "vendortype")
private Integer vendorType;

@Column(name = "address2")
private String address2;

@Column(name = "city")
private String city;

@Column(name = "state", length = 2)
private String state;

@Column(name = "zip", length = 5)
private int zip;

@Column(name = "phone", length = 10)
private String phone;

@Column(name = "email")
private String email;

@Column(name = "createddate", nullable = false)
private java.sql.Timestamp createdDate;

@Column(name = "updateddate", nullable = false)
private java.sql.Timestamp updatedDate;
}
