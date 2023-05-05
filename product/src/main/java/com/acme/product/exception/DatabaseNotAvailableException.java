package com.acme.product.exception;

public class DatabaseNotAvailableException extends RuntimeException {
    public DatabaseNotAvailableException() {
        super("The database is currently not available");
    }
}
