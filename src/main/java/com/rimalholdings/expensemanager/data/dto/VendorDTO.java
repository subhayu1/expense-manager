package com.rimalholdings.expensemanager.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
 public class VendorDTO {
  private Long id;
  private String name;
  private String externalId;
  private Integer vendorType;
  private String address1;
  private String address2;
  private String city;
  private String state;
  private Integer zip;
  private String phone;
  private String email;


}
