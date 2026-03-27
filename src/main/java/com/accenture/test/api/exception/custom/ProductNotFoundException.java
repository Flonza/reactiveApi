package com.accenture.test.api.exception.custom;

import com.accenture.test.api.exception.codes.ErrorCode;
import lombok.Getter;

@Getter
public class ProductNotFoundException extends RuntimeException {

    private final ErrorCode errorCode;

    public ProductNotFoundException(String productId) {
        super(ErrorCode.PRODUCT_NOT_FOUND.getDefaultMessage() + ": " + productId);
        this.errorCode = ErrorCode.PRODUCT_NOT_FOUND;
    }
}