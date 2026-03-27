package com.accenture.test.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeneralResponse<T> {

    private int status;
    private String errorCode;
    private String message;
    private T data;

    public static <T> GeneralResponse<T> success(T data) {
        return GeneralResponse.<T>builder()
                .status(200)
                .errorCode(null)
                .message("OK")
                .data(data)
                .build();
    }

    public static <T> GeneralResponse<T> created(T data) {
        return GeneralResponse.<T>builder()
                .status(201)
                .errorCode(null)
                .message("Created")
                .data(data)
                .build();
    }

    public static <T> GeneralResponse<T> error(int status, String errorCode, String message) {
        return GeneralResponse.<T>builder()
                .status(status)
                .errorCode(errorCode)
                .message(message)
                .data(null)
                .build();
    }
}