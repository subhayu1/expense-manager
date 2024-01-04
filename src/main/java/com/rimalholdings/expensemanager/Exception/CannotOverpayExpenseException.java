package com.rimalholdings.expensemanager.Exception;

public class CannotOverpayExpenseException extends IllegalArgumentException implements ExpenseManagerExceptionInterface {

  public CannotOverpayExpenseException(String message) {
    super(message);
  }

}
