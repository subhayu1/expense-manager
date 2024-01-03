package com.rimalholdings.expensemanager.model;

import lombok.Data;

@Data
public class VendorDTO {
  private Long id;
  private String vendorId;
  private String name;
  private Integer vendorType;
  private String address1;
  private String address2;
  private String city;
  private String state;
  private Integer zip;
  private String phone;
  private String email;

}
