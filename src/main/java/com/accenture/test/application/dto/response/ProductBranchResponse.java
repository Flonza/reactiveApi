package com.accenture.test.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductBranchResponse {
    private String branchId;
    private String branchName;
    private String productId;
    private String productName;
    private int stock;
}