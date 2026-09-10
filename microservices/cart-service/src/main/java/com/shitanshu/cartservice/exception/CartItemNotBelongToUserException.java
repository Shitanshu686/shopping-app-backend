package com.shitanshu.cartservice.exception;

public class CartItemNotBelongToUserException extends RuntimeException {

    public CartItemNotBelongToUserException(String message) {
        super(message);
    }
}