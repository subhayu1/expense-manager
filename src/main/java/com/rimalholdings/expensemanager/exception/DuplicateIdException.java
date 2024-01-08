/* (C)1 */
package com.rimalholdings.expensemanager.exception;

import java.sql.SQLIntegrityConstraintViolationException;

public class DuplicateIdException extends SQLIntegrityConstraintViolationException
	implements ExpenseManagerExceptionInterface {

public DuplicateIdException(String message) {
	super(message);
}
}