package com.rimalholdings.expensemanager.exception;

public class InvalidObjectException extends IllegalArgumentException
	implements ExpenseManagerExceptionInterface {

public InvalidObjectException(String message) {
	super(message);
}
}
