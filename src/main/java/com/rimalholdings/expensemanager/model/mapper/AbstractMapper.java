package com.rimalholdings.expensemanager.model.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rimalholdings.expensemanager.service.VendorService;

public abstract class AbstractMapper<T> {

  private final VendorService vendorService;
  private final ObjectMapper objectMapper;

  protected AbstractMapper(VendorService vendorService, ObjectMapper objectMapper) {
    this.vendorService = vendorService;
    this.objectMapper = objectMapper;
  }

  public String convertDtoToString(T dto) {
    try {
      // Convert the VendorDTO object to a JSON string
      return objectMapper.writeValueAsString(dto);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }


}