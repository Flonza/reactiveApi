package com.accenture.test.infrastructure.persistence.mapper;

import com.accenture.test.domain.model.Branch;
import com.accenture.test.domain.model.Franchise;
import com.accenture.test.domain.model.Product;
import com.accenture.test.infrastructure.persistence.entity.FranchiseDocument;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FranchiseMapper {

    // ─── Document → Model ───────────────────────────────────────────

    public Franchise toModel(FranchiseDocument document) {
        if (document == null) return null;
        return Franchise.builder()
                .id(document.getId())
                .name(document.getName())
                .branches(toBranchModelList(document.getBranches()))
                .build();
    }

    private List<Branch> toBranchModelList(List<FranchiseDocument.BranchDocument> branchDocuments) {
        if (branchDocuments == null) return Collections.emptyList();
        return branchDocuments.stream()
                .map(this::toBranchModel)
                .collect(Collectors.toList());
    }

    private Branch toBranchModel(FranchiseDocument.BranchDocument branchDocument) {
        if (branchDocument == null) return null;
        return Branch.builder()
                .id(branchDocument.getId())
                .name(branchDocument.getName())
                .products(toProductModelList(branchDocument.getProducts()))
                .build();
    }

    private List<Product> toProductModelList(List<FranchiseDocument.ProductDocument> productDocuments) {
        if (productDocuments == null) return Collections.emptyList();
        return productDocuments.stream()
                .map(this::toProductModel)
                .collect(Collectors.toList());
    }

    private Product toProductModel(FranchiseDocument.ProductDocument productDocument) {
        if (productDocument == null) return null;
        return Product.builder()
                .id(productDocument.getId())
                .name(productDocument.getName())
                .stock(productDocument.getStock())
                .build();
    }

    // ─── Model → Document ───────────────────────────────────────────

    public FranchiseDocument toDocument(Franchise franchise) {
        if (franchise == null) return null;
        return FranchiseDocument.builder()
                .id(franchise.getId())
                .name(franchise.getName())
                .branches(toBranchDocumentList(franchise.getBranches()))
                .build();
    }

    private List<FranchiseDocument.BranchDocument> toBranchDocumentList(List<Branch> branches) {
        if (branches == null) return Collections.emptyList();
        return branches.stream()
                .map(this::toBranchDocument)
                .collect(Collectors.toList());
    }

    private FranchiseDocument.BranchDocument toBranchDocument(Branch branch) {
        if (branch == null) return null;
        return FranchiseDocument.BranchDocument.builder()
                .id(branch.getId())
                .name(branch.getName())
                .products(toProductDocumentList(branch.getProducts()))
                .build();
    }

    private List<FranchiseDocument.ProductDocument> toProductDocumentList(List<Product> products) {
        if (products == null) return Collections.emptyList();
        return products.stream()
                .map(this::toProductDocument)
                .collect(Collectors.toList());
    }

    private FranchiseDocument.ProductDocument toProductDocument(Product product) {
        if (product == null) return null;
        return FranchiseDocument.ProductDocument.builder()
                .id(product.getId())
                .name(product.getName())
                .stock(product.getStock())
                .build();
    }
}