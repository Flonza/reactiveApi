package com.accenture.test.api.exception.custom;

import com.accenture.test.api.exception.codes.ErrorCode;
import lombok.Getter;

@Getter
public class FranchiseNotFoundException extends RuntimeException {

    private final ErrorCode errorCode;

    public FranchiseNotFoundException(String franchiseId) {
        super(ErrorCode.FRANCHISE_NOT_FOUND.getDefaultMessage() + ": " + franchiseId);
        this.errorCode = ErrorCode.FRANCHISE_NOT_FOUND;
    }
}