package com.rimalholdings.expensemanager.Exception;

public class CannotOverpayExpenseException extends IllegalArgumentException{
  public CannotOverpayExpenseException(String message) {
    super(message);
  }

}
