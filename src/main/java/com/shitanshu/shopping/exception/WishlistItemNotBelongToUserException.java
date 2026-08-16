package com.shitanshu.shopping.exception;

public class WishlistItemNotBelongToUserException
        extends RuntimeException {

    public WishlistItemNotBelongToUserException(
            String message) {

        super(message);
    }
}