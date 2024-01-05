/* (C)1 */
package com.rimalholdings.expensemanager.Exception;

import java.util.NoSuchElementException;

public class ObjectNotFoundException extends NoSuchElementException
	implements ExpenseManagerExceptionInterface {

public ObjectNotFoundException(String message) {
	super(message);
}
}
