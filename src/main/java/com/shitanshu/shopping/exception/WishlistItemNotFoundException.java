package com.shitanshu.shopping.exception;

public class WishlistItemNotFoundException extends RuntimeException {

    public WishlistItemNotFoundException(String message) {

        super(message);
    }
}