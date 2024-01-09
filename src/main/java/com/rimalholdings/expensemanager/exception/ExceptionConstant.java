/* (C)1 */
package com.rimalholdings.expensemanager.exception;

public class ExceptionConstant {

public static final String OBJECT_NOT_FOUND = "Object with id:%s not found";
public static final String VENDOR = "vendor";
public static final String EXPENSE_NOT_FOUND = "Expense not found";

public static final Integer ERROR = 1;
public static final Integer SUCCESS = 0;
public static final String DUPLICATE_ID = "%s with id:%s already exists";
public static final String OBJECT_INTEGRITY_VIOLATION =
	"Object with id:%s cannot be deleted as it is referenced by other objects";
}
