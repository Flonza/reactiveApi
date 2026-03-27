package com.accenture.test.api.controller;

import com.accenture.test.application.dto.request.ProductRequest;
import com.accenture.test.application.dto.response.BranchResponse;
import com.accenture.test.application.dto.response.FranchiseResponse;
import com.accenture.test.application.dto.response.GeneralResponse;
import com.accenture.test.application.dto.response.ProductResponse;
import com.accenture.test.application.usecase.product.AddProductUseCase;
import com.accenture.test.application.usecase.product.DeleteProductUseCase;
import com.accenture.test.application.usecase.product.UpdateProductNameUseCase;
import com.accenture.test.application.usecase.product.UpdateProductStockUseCase;
import com.accenture.test.domain.model.Branch;
import com.accenture.test.domain.model.Franchise;
import com.accenture.test.domain.model.Product;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/franchises/{franchiseId}/branches/{branchId}/products")
@RequiredArgsConstructor
public class ProductController {

    private final AddProductUseCase addProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final UpdateProductStockUseCase updateProductStockUseCase;
    private final UpdateProductNameUseCase updateProductNameUseCase;

    @PostMapping
    public Mono<ResponseEntity<GeneralResponse<FranchiseResponse>>> addProduct(
            @PathVariable String franchiseId,
            @PathVariable String branchId,
            @Valid @RequestBody ProductRequest request) {
        return addProductUseCase.execute(franchiseId, branchId, request)
                .map(franchise -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(GeneralResponse.created(toResponse(franchise))));
    }

    @DeleteMapping("/{productId}")
    public Mono<ResponseEntity<GeneralResponse<FranchiseResponse>>> deleteProduct(
            @PathVariable String franchiseId,
            @PathVariable String branchId,
            @PathVariable String productId) {
        return deleteProductUseCase.execute(franchiseId, branchId, productId)
                .map(franchise -> ResponseEntity
                        .ok(GeneralResponse.success(toResponse(franchise))));
    }

    @PatchMapping("/{productId}/stock")
    public Mono<ResponseEntity<GeneralResponse<FranchiseResponse>>> updateStock(
            @PathVariable String franchiseId,
            @PathVariable String branchId,
            @PathVariable String productId,
            @RequestParam int newStock) {
        return updateProductStockUseCase.execute(franchiseId, branchId, productId, newStock)
                .map(franchise -> ResponseEntity
                        .ok(GeneralResponse.success(toResponse(franchise))));
    }

    @PatchMapping("/{productId}/name")
    public Mono<ResponseEntity<GeneralResponse<FranchiseResponse>>> updateName(
            @PathVariable String franchiseId,
            @PathVariable String branchId,
            @PathVariable String productId,
            @RequestParam String newName) {
        return updateProductNameUseCase.execute(franchiseId, branchId, productId, newName)
                .map(franchise -> ResponseEntity
                        .ok(GeneralResponse.success(toResponse(franchise))));
    }

    // ─── Mapper local ────────────────────────────────────────────────

    private FranchiseResponse toResponse(Franchise franchise) {
        return FranchiseResponse.builder()
                .id(franchise.getId())
                .name(franchise.getName())
                .branches(mapBranches(franchise.getBranches()))
                .build();
    }

    private List<BranchResponse> mapBranches(List<Branch> branches) {
        if (branches == null) return List.of();
        return branches.stream()
                .map(this::mapBranch)
                .toList();
    }

    private BranchResponse mapBranch(Branch branch) {
        return BranchResponse.builder()
                .id(branch.getId())
                .name(branch.getName())
                .products(mapProducts(branch.getProducts()))
                .build();
    }

    private List<ProductResponse> mapProducts(List<Product> products) {
        if (products == null) return List.of();
        return products.stream()
                .map(this::mapProduct)
                .toList();
    }

    private ProductResponse mapProduct(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .stock(product.getStock())
                .build();
    }
}