package com.rimalholdings.expensemanager.data.dto.sync;

import java.util.List;

import com.rimalholdings.expensemanager.data.dto.BaseDTOInterface;
import com.rimalholdings.expensemanager.data.dto.Vendor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VendorResponse implements BaseDTOInterface {
@JsonProperty("content")
private List<Vendor> vendors;

@Override
public Long getId() {
	return null;
}

@Override
public void setId(Long id) {}
}
