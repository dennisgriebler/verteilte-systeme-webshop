package com.acme.category.exception;

public class CategoryExistsException extends RuntimeException {
    public CategoryExistsException(String name) {
        super("Kategorie " + name + " existiert schon");
    }
}
