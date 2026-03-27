package com.accenture.test.application.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    @NotBlank(message = "The product name is required")
    private String name;

    @Min(value = 0, message = "Stock cannot be negative")
    private int stock;
}
