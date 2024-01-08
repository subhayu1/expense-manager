/* (C)1 */
package com.rimalholdings.expensemanager.exception;

public class UpdateNotAllowedException extends RuntimeException
	implements ExpenseManagerExceptionInterface {
public UpdateNotAllowedException(String message) {
	super(message);
}
}
