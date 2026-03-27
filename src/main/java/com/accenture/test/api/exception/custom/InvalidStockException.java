package com.accenture.test.api.exception.custom;

import com.accenture.test.api.exception.codes.ErrorCode;
import lombok.Getter;

@Getter
public class InvalidStockException extends RuntimeException {

    private final ErrorCode errorCode;

    public InvalidStockException(int stock) {
        super(ErrorCode.PRODUCT_INVALID_STOCK.getDefaultMessage() + ". Valor recibido: " + stock);
        this.errorCode = ErrorCode.PRODUCT_INVALID_STOCK;
    }
}