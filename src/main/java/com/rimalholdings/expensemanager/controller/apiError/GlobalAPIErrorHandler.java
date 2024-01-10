/* (C)1 */
package com.rimalholdings.expensemanager.controller.apiError;

import java.util.LinkedHashMap;
import java.util.Map;

import com.rimalholdings.expensemanager.exception.CannotOverpayExpenseException;
import com.rimalholdings.expensemanager.exception.DuplicateIdException;
import com.rimalholdings.expensemanager.exception.ExceptionConstant;
import com.rimalholdings.expensemanager.exception.IdNotSuppliedException;
import com.rimalholdings.expensemanager.exception.ObjectNotFoundException;
import com.rimalholdings.expensemanager.exception.UpdateNotAllowedException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalAPIErrorHandler {

@ExceptionHandler(ObjectNotFoundException.class)
@ResponseStatus(HttpStatus.NOT_FOUND)
public Map<String, String> handleObjectNotFoundException(ObjectNotFoundException e) {
	Map<String, String> errorMap = new LinkedHashMap<>();
	errorMap.put("status", String.valueOf(ExceptionConstant.ERROR));
	errorMap.put("message", e.getMessage());
	log.info("Handled NoSuchElementException: {}", e.getMessage());
	log.info("Stacktrace: {}", e.getMessage(), e);

	return errorMap;
}

@ExceptionHandler({
	IdNotSuppliedException.class,
	DuplicateIdException.class,
	CannotOverpayExpenseException.class,
	IllegalArgumentException.class
})
@ResponseStatus(HttpStatus.BAD_REQUEST)
public Map<String, String> handleException(RuntimeException e) {
	Map<String, String> errorMap = new LinkedHashMap<>();
	errorMap.put("status", String.valueOf(ExceptionConstant.ERROR));
	errorMap.put("message", e.getMessage());
	log.info("Handled Exception: {}", e.getMessage());
	log.info("Stacktrace: {}", e.getMessage(), e);
	return errorMap;
}

@ExceptionHandler(UpdateNotAllowedException.class)
@ResponseStatus(HttpStatus.CONFLICT)
public Map<String, String> handleDataIntegrityViolationException(
	DataIntegrityViolationException e) {
	Map<String, String> errorMap = new LinkedHashMap<>();
	errorMap.put("status", String.valueOf(ExceptionConstant.ERROR));
	errorMap.put("message", e.getMessage());
	log.info("Handled DataIntegrityViolationException: {}", e.getMessage());
	log.info("Stacktrace: {}", e.getMessage(), e);
	return errorMap;
}
}
