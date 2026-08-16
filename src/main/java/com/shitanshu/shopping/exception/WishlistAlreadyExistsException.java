package com.shitanshu.shopping.exception;

public class WishlistAlreadyExistsException
        extends RuntimeException {

    public WishlistAlreadyExistsException(String message) {

        super(message);
    }
}