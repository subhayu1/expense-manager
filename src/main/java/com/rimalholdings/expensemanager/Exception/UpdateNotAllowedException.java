package com.rimalholdings.expensemanager.Exception;

public class UpdateNotAllowedException extends RuntimeException
  implements ExpenseManagerExceptionInterface {
  public UpdateNotAllowedException(String message) {
    super(message);
  }
}
