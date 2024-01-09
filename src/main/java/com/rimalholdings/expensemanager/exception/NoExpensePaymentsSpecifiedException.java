package com.rimalholdings.expensemanager.exception;

public class NoExpensePaymentsSpecifiedException extends IllegalArgumentException
	implements ExceptionInterface {

public NoExpensePaymentsSpecifiedException(String message) {
	super(message);
}
}
