package com.accenture.test.api.exception.custom;

import com.accenture.test.api.exception.codes.ErrorCode;
import lombok.Getter;

@Getter
public class BranchNotFoundException extends RuntimeException {

    private final ErrorCode errorCode;

    public BranchNotFoundException(String branchId) {
        super(ErrorCode.BRANCH_NOT_FOUND.getDefaultMessage() + ": " + branchId);
        this.errorCode = ErrorCode.BRANCH_NOT_FOUND;
    }
}