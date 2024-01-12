/* (C)1 */
package com.rimalholdings.expensemanager.data.dto;

import lombok.Data;

@Data
public class Vendor implements BaseDTOInterface {

private Long id;
private String name;
private String vendorNumber;
private Integer vendorType;
private String address1;
private String address2;
private String city;
private String state;
private String zip;
private String country;
private String phone;
private String email;
private String number;
private String integrationId;
private Integer orgId;
}
