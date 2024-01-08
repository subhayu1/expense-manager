/* (C)1 */
package com.rimalholdings.expensemanager.exception;

import java.util.NoSuchElementException;

public class ObjectNotFoundException extends NoSuchElementException
	implements ExpenseManagerExceptionInterface {

public ObjectNotFoundException(String message) {
	super(message);
}
}
