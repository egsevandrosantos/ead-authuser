package com.ead.authuser.api.exceptionhandlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.ead.authuser.domain.exceptions.EntityNotFoundException;
import com.ead.authuser.domain.exceptions.UniqueConstraintException;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<Void> handleEntityNotFound(EntityNotFoundException ex) {
		return ResponseEntity.notFound().build();
	}
	
	@ExceptionHandler(UniqueConstraintException.class)
	public ProblemDetail handleUniqueConstraint(UniqueConstraintException ex) {
		ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
		problemDetail.setTitle(ex.getMessage());
		
		return problemDetail;
	}
}
