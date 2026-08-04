package com.shitanshu.shopping.exception;
import com.shitanshu.shopping.response.ApiResponse;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;
import com.shitanshu.shopping.exception.ResourceAlreadyExistsException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ProductNotFoundException.class)
	public ResponseEntity<ApiResponse<String>> handleProductNotFound(ProductNotFoundException ex) {

	    ApiResponse<String> response =
	            new ApiResponse<>(
	                    false,
	                    ex.getMessage(),
	                    null,
	                    LocalDateTime.now());

	    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
    
	@ExceptionHandler(ResourceAlreadyExistsException.class)
	public ResponseEntity<ApiResponse<String>> handleResourceAlreadyExists(
	        ResourceAlreadyExistsException ex) {

	    ApiResponse<String> response =
	            new ApiResponse<>(
	                    false,
	                    ex.getMessage(),
	                    null,
	                    LocalDateTime.now());

	    return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}
    
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(
	        MethodArgumentNotValidException ex) {

	    Map<String, String> errors = new HashMap<>();

	    for (FieldError error : ex.getBindingResult().getFieldErrors()) {

	        errors.put(error.getField(), error.getDefaultMessage());

	    }

	    ApiResponse<Map<String, String>> response =
	            new ApiResponse<>(
	                    false,
	                    "Validation Failed",
	                    errors,
	                    LocalDateTime.now());

	    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

	}

}