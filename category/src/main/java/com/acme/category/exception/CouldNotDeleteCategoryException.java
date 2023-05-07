package com.acme.category.exception;

public class CouldNotDeleteCategoryException extends RuntimeException {
    public CouldNotDeleteCategoryException(String msg) {
        super(msg);
    }
}
