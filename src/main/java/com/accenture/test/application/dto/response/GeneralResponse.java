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
    private String message;
    private T data;

    // Static methods
    public static <T> GeneralResponse<T> success(T data) {
        return GeneralResponse.<T>builder()
                .status(200)
                .message("OK")
                .data(data)
                .build();
    }

    public static <T> GeneralResponse<T> created(T data) {
        return GeneralResponse.<T>builder()
                .status(201)
                .message("Created")
                .data(data)
                .build();
    }

    public static <T> GeneralResponse<T> error(int status, String message) {
        return GeneralResponse.<T>builder()
                .status(status)
                .message(message)
                .data(null)
                .build();
    }
}