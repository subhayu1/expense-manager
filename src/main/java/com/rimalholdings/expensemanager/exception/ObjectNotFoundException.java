/* (C)1 */
package com.rimalholdings.expensemanager.exception;

import java.util.NoSuchElementException;

public class ObjectNotFoundException extends NoSuchElementException implements ExceptionInterface {

public ObjectNotFoundException(String message) {
	super(message);
}
}
