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
import com.shitanshu.shopping.exception.InvalidCredentialsException;
import com.shitanshu.shopping.exception.UserNotFoundException;
import com.shitanshu.shopping.exception.CartItemNotBelongToUserException;
import com.shitanshu.shopping.exception.WishlistItemNotFoundException;
import com.shitanshu.shopping.exception.WishlistItemNotBelongToUserException;
import com.shitanshu.shopping.exception.WishlistAlreadyExistsException;
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
	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<ApiResponse<String>> handleInvalidCredentials(
	        InvalidCredentialsException ex) {

	    ApiResponse<String> response =
	            new ApiResponse<>(
	                    false,
	                    ex.getMessage(),
	                    null,
	                    LocalDateTime.now());

	    return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);

	}
	@ExceptionHandler(InsufficientStockException.class)
	public ResponseEntity<ApiResponse<String>> handleInsufficientStock(
	        InsufficientStockException ex) {

	    ApiResponse<String> response =
	            new ApiResponse<>(
	                    false,
	                    ex.getMessage(),
	                    null,
	                    LocalDateTime.now());

	    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ApiResponse<String>> handleUserNotFound(
	        UserNotFoundException ex) {

	    ApiResponse<String> response =
	            new ApiResponse<>(
	                    false,
	                    ex.getMessage(),
	                    null,
	                    LocalDateTime.now());

	    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler(CartItemNotFoundException.class)
	public ResponseEntity<ApiResponse<String>> handleCartItemNotFound(
	        CartItemNotFoundException ex) {

	    ApiResponse<String> response =
	            new ApiResponse<>(
	                    false,
	                    ex.getMessage(),
	                    null,
	                    LocalDateTime.now());

	    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler(CartItemNotBelongToUserException.class)
	public ResponseEntity<ApiResponse<String>> handleCartItemNotBelongToUser(
	        CartItemNotBelongToUserException ex) {

	    ApiResponse<String> response =
	            new ApiResponse<>(
	                    false,
	                    ex.getMessage(),
	                    null,
	                    LocalDateTime.now());

	    return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
	}
	// =========================
	// WISHLIST ITEM NOT FOUND
	// =========================

	@ExceptionHandler(WishlistItemNotFoundException.class)
	public ResponseEntity<ApiResponse<String>> handleWishlistItemNotFound(
	        WishlistItemNotFoundException ex) {

	    ApiResponse<String> response =
	            new ApiResponse<>(
	                    false,
	                    ex.getMessage(),
	                    null,
	                    LocalDateTime.now());

	    return new ResponseEntity<>(
	            response,
	            HttpStatus.NOT_FOUND);
	}


	// =========================
	// WISHLIST ITEM NOT BELONG TO USER
	// =========================

	@ExceptionHandler(WishlistItemNotBelongToUserException.class)
	public ResponseEntity<ApiResponse<String>> handleWishlistItemNotBelongToUser(
	        WishlistItemNotBelongToUserException ex) {

	    ApiResponse<String> response =
	            new ApiResponse<>(
	                    false,
	                    ex.getMessage(),
	                    null,
	                    LocalDateTime.now());

	    return new ResponseEntity<>(
	            response,
	            HttpStatus.FORBIDDEN);
	}
	// =========================
	// WISHLIST ALREADY EXISTS
	// =========================

	@ExceptionHandler(WishlistAlreadyExistsException.class)
	public ResponseEntity<ApiResponse<String>> handleWishlistAlreadyExists(
	        WishlistAlreadyExistsException ex) {

	    ApiResponse<String> response =
	            new ApiResponse<>(
	                    false,
	                    ex.getMessage(),
	                    null,
	                    LocalDateTime.now());

	    return new ResponseEntity<>(
	            response,
	            HttpStatus.CONFLICT);
	}
	// =========================
	// ORDER NOT FOUND
	// =========================

	@ExceptionHandler(OrderNotFoundException.class)
	public ResponseEntity<ApiResponse<String>> handleOrderNotFound(
	        OrderNotFoundException ex) {

	    ApiResponse<String> response =
	            new ApiResponse<>(
	                    false,
	                    ex.getMessage(),
	                    null,
	                    LocalDateTime.now());

	    return new ResponseEntity<>(
	            response,
	            HttpStatus.NOT_FOUND);
	}
	// =========================
	// ORDER DOES NOT BELONG TO USER
	// =========================

	@ExceptionHandler(OrderNotBelongToUserException.class)
	public ResponseEntity<ApiResponse<String>> handleOrderNotBelongToUser(
	        OrderNotBelongToUserException ex) {

	    ApiResponse<String> response =
	            new ApiResponse<>(
	                    false,
	                    ex.getMessage(),
	                    null,
	                    LocalDateTime.now());

	    return new ResponseEntity<>(
	            response,
	            HttpStatus.FORBIDDEN);
	}
	// =========================
	// INVALID ORDER STATUS
	// =========================

	@ExceptionHandler(InvalidOrderStatusException.class)
	public ResponseEntity<ApiResponse<String>> handleInvalidOrderStatus(
	        InvalidOrderStatusException ex) {

	    ApiResponse<String> response =
	            new ApiResponse<>(
	                    false,
	                    ex.getMessage(),
	                    null,
	                    LocalDateTime.now());

	    return new ResponseEntity<>(
	            response,
	            HttpStatus.BAD_REQUEST);
	}
}