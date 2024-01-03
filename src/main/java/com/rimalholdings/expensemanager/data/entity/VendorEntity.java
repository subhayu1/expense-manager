package com.rimalholdings.expensemanager.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "vendor")
public class VendorEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "vendorid", length=20,nullable = false, unique = true)
  private String vendorId;

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
}