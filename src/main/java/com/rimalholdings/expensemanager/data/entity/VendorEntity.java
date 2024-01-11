/* (C)1 */
package com.rimalholdings.expensemanager.data.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "vendor")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
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

@Column(name = "zip", length = 15)
private String zip;
@Column(name = "country", length = 100)
private String country;

@Column(name = "phone", length = 10)
private String phone;

@Column(name = "email")
private String email;

@Column(name = "createddate", nullable = false)
private java.sql.Timestamp createdDate;

@Column(name = "updateddate", nullable = false)
private java.sql.Timestamp updatedDate;

@Column(name="externalorgid")
private Integer externalOrgId;

@OneToMany(mappedBy = "vendor")
@ToString.Exclude
private List<ExpenseEntity> expenses = new ArrayList<>();
}
