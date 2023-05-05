package com.acme.product.exception;

public class CategoryNotFound extends RuntimeException {

    public CategoryNotFound(int categoryId) {
        super("Category with id " + categoryId + " not found");
    }
}
