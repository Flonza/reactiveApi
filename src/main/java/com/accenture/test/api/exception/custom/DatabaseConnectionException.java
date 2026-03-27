package com.accenture.test.api.exception.custom;

import com.accenture.test.api.exception.codes.ErrorCode;
import lombok.Getter;

@Getter
public class DatabaseConnectionException extends RuntimeException {

    private final ErrorCode errorCode;

    public DatabaseConnectionException(String detail) {
        super(ErrorCode.DATABASE_CONNECTION_ERROR.getDefaultMessage() + ": " + detail);
        this.errorCode = ErrorCode.DATABASE_CONNECTION_ERROR;
    }
}