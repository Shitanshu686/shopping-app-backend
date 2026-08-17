package com.shitanshu.shopping.exception;

public class InvalidOrderStatusException
        extends RuntimeException {

    public InvalidOrderStatusException(String message) {
        super(message);
    }
}