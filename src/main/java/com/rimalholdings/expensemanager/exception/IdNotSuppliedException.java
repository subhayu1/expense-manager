/* (C)1 */
package com.rimalholdings.expensemanager.exception;

public class IdNotSuppliedException extends IllegalArgumentException
	implements ExpenseManagerExceptionInterface {

public IdNotSuppliedException(String message) {
	super(message);
}
}
