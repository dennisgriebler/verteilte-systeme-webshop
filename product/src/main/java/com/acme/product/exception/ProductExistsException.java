package com.acme.product.exception;

public class ProductExistsException extends RuntimeException {
    public ProductExistsException(String name) {
        super("Product " + name + " exists already");
    }
}
