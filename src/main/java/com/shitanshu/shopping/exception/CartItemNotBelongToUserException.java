package com.shitanshu.shopping.exception;

public class CartItemNotBelongToUserException extends RuntimeException {

    public CartItemNotBelongToUserException(String message) {
        super(message);
    }
}