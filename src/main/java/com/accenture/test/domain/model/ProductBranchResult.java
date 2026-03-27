package com.accenture.test.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductBranchResult {
    private String branchId;
    private String branchName;
    private String productId;
    private String productName;
    private int stock;
}