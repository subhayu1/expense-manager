package com.rimalholdings.expensemanager.controller.apiError;

import com.rimalholdings.expensemanager.Exception.DuplicateIdException;
import com.rimalholdings.expensemanager.Exception.ExceptionConstant;
import com.rimalholdings.expensemanager.Exception.IdNotSuppliedException;
import com.rimalholdings.expensemanager.Exception.ObjectNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice

@Slf4j
public class GlobalAPIErrorHandler {

  @ExceptionHandler(ObjectNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public Map<String, String> handleObjectNotFoundException(ObjectNotFoundException e) {
    Map<String, String> errorMap = new LinkedHashMap<>();
    errorMap.put("HttpStatus", HttpStatus.NOT_FOUND.toString());
    errorMap.put("status", String.valueOf(ExceptionConstant.ERROR));
    errorMap.put("message", e.getMessage());
    log.info("Handled NoSuchElementException: {}", e.getMessage());
    return errorMap;
  }

  @ExceptionHandler(IdNotSuppliedException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, String> handleIdNotSuppliedException(IdNotSuppliedException e) {
    Map<String, String> errorMap = new LinkedHashMap<>();
    errorMap.put("HttpStatus", HttpStatus.BAD_REQUEST.toString());
    errorMap.put("status", String.valueOf(ExceptionConstant.ERROR));
    errorMap.put("message", e.getMessage());
    log.info("Handled IdNotSuppliedException: {}", e.getMessage());
    return errorMap;
  }

  @ExceptionHandler(DuplicateIdException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, String> handleDuplicateIdException(DuplicateIdException e) {
    Map<String, String> errorMap = new LinkedHashMap<>();
    errorMap.put("HttpStatus", HttpStatus.BAD_REQUEST.toString());
    errorMap.put("status", String.valueOf(ExceptionConstant.ERROR));
    errorMap.put("message", e.getMessage());
    log.info("Handled DuplicateIdException: {}", e.getMessage());
    return errorMap;
  }


}
