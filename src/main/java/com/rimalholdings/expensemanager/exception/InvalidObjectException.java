package com.rimalholdings.expensemanager.exception;

public class InvalidObjectException extends IllegalArgumentException implements ExceptionInterface {

public InvalidObjectException(String message) {
	super(message);
}
}
