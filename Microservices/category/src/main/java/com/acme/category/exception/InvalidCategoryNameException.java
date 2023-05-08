package com.acme.category.exception;

public class InvalidCategoryNameException extends RuntimeException{
    public InvalidCategoryNameException(String msg) {
        super(msg);
    }
}
