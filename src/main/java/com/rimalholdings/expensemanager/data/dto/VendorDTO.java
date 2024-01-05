/* (C)1 */
package com.rimalholdings.expensemanager.data.dto;

import lombok.Data;

@Data
public class VendorDTO implements BaseDTOInterface {

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
