package com.accenture.test.api.exception.custom;

import com.accenture.test.api.exception.codes.ErrorCode;
import lombok.Getter;

@Getter
public class DuplicateNameException extends RuntimeException {

    private final ErrorCode errorCode;

    public DuplicateNameException(ErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
    }
}