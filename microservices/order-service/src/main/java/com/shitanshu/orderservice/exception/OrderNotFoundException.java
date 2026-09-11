package com.shitanshu.orderservice.exception;

public class OrderNotFoundException
        extends RuntimeException {

    public OrderNotFoundException(String message) {
        super(message);
    }
}