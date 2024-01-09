/* (C)1 */
package com.rimalholdings.expensemanager.exception;

import org.springframework.dao.DataIntegrityViolationException;

public class UpdateNotAllowedException extends DataIntegrityViolationException
	implements ExpenseManagerExceptionInterface {
public UpdateNotAllowedException(String message) {
	super(message);
}
}
