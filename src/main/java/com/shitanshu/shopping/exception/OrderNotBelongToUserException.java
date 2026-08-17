package com.shitanshu.shopping.exception;

public class OrderNotBelongToUserException
        extends RuntimeException {

    public OrderNotBelongToUserException(String message) {
        super(message);
    }
}