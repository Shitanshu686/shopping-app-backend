package com.shitanshu.orderservice.exception;

public class OrderNotBelongToUserException
        extends RuntimeException {

    public OrderNotBelongToUserException(String message) {
        super(message);
    }
}