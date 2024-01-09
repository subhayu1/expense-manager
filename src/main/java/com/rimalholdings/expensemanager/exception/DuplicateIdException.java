/* (C)1 */
package com.rimalholdings.expensemanager.exception;

import java.sql.SQLIntegrityConstraintViolationException;

public class DuplicateIdException extends SQLIntegrityConstraintViolationException
	implements ExceptionInterface {

public DuplicateIdException(String message) {
	super(message);
}
}
