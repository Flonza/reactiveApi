package com.accenture.test.infrastructure.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "franchises")
public class FranchiseDocument {

    @Id
    private String id;
    private String name;
    private List<BranchDocument> branches;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BranchDocument {
        private String id;
        private String name;
        private List<ProductDocument> products;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductDocument {
        private String id;
        private String name;
        private int stock;
    }
}