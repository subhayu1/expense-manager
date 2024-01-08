/* (C)1 */
package com.rimalholdings.expensemanager.exception;

public class CannotOverpayExpenseException extends IllegalArgumentException
	implements ExpenseManagerExceptionInterface {

public CannotOverpayExpenseException(String message) {
	super(message);
}
}
