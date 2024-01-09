/* (C)1 */
package com.rimalholdings.expensemanager.exception;

public class CannotOverpayExpenseException extends IllegalArgumentException
	implements ExceptionInterface {

public CannotOverpayExpenseException(String message) {
	super(message);
}
}
