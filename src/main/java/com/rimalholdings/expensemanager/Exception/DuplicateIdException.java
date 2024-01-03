package com.rimalholdings.expensemanager.Exception;

import java.sql.SQLIntegrityConstraintViolationException;

public class DuplicateIdException extends SQLIntegrityConstraintViolationException {
  public DuplicateIdException(String message) {
    super(message);
  }



}
