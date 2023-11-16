package com.switzerlandbank.api.resources.exceptions;

import java.time.Instant;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.switzerlandbank.api.services.exceptions.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ResourceExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
		String error = "Resource not found";
		HttpStatus status = HttpStatus.NOT_FOUND;
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<StandardError> invalidArgument(MethodArgumentNotValidException e, HttpServletRequest request) {
		String error = "Invalid input";
		HttpStatus status = HttpStatus.BAD_REQUEST;
		Map<String, String> errorMap = ErrorMapGenerator.generateErrorGenerator(e.getBindingResult());
		String formattedErrorMap = ErrorMapGenerator.formatErrorMap(errorMap);
		StandardError err = new StandardError(Instant.now(), status.value(), error, formattedErrorMap, request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}

}
