package com.acme.product.exception;

public class CouldNotDeleteProductException extends RuntimeException {
    public CouldNotDeleteProductException(String msg) {
        super(msg);
    }
}
