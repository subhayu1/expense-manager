package com.rimalholdings.expensemanager.controller.apiError;

import com.rimalholdings.expensemanager.Exception.CannotOverpayExpenseException;
import com.rimalholdings.expensemanager.Exception.DuplicateIdException;
import com.rimalholdings.expensemanager.Exception.ExceptionConstant;
import com.rimalholdings.expensemanager.Exception.IdNotSuppliedException;
import com.rimalholdings.expensemanager.Exception.ObjectNotFoundException;
import com.rimalholdings.expensemanager.Exception.UpdateNotAllowedException;
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
    errorMap.put("status", String.valueOf(ExceptionConstant.ERROR));
    errorMap.put("message", e.getMessage());
    log.info("Handled NoSuchElementException: {}", e.getMessage());
    log.info("Stacktrace: {}", e.getMessage(),e);

    return errorMap;
  }

  @ExceptionHandler({ObjectNotFoundException.class, IdNotSuppliedException.class,
      DuplicateIdException.class, UpdateNotAllowedException.class,
      CannotOverpayExpenseException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, String> handleException(RuntimeException e) {
    Map<String, String> errorMap = new LinkedHashMap<>();
    errorMap.put("status", String.valueOf(ExceptionConstant.ERROR));
    errorMap.put("message", e.getMessage());
    log.info("Handled Exception: {}", e.getMessage());
    log.info("Stacktrace: {}", e.getMessage(),e);
    return errorMap;
  }
}

