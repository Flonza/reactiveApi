package com.accenture.test.api.controller;

import com.accenture.test.application.dto.request.BranchRequest;
import com.accenture.test.application.dto.response.BranchResponse;
import com.accenture.test.application.dto.response.FranchiseResponse;
import com.accenture.test.application.dto.response.GeneralResponse;
import com.accenture.test.application.dto.response.ProductResponse;
import com.accenture.test.application.usecase.branch.AddBranchUseCase;
import com.accenture.test.application.usecase.branch.UpdateBranchNameUseCase;
import com.accenture.test.domain.model.Franchise;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/franchises/{franchiseId}/branches")
@RequiredArgsConstructor
public class BranchController {

    private final AddBranchUseCase addBranchUseCase;
    private final UpdateBranchNameUseCase updateBranchNameUseCase;

    @PostMapping
    public Mono<ResponseEntity<GeneralResponse<FranchiseResponse>>> addBranch(
            @PathVariable String franchiseId,
            @Valid @RequestBody BranchRequest request) {
        return addBranchUseCase.execute(franchiseId, request)
                .map(franchise -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(GeneralResponse.created(toResponse(franchise))));
    }

    @PatchMapping("/{branchId}/name")
    public Mono<ResponseEntity<GeneralResponse<FranchiseResponse>>> updateName(
            @PathVariable String franchiseId,
            @PathVariable String branchId,
            @RequestParam String newName) {
        return updateBranchNameUseCase.execute(franchiseId, branchId, newName)
                .map(franchise -> ResponseEntity
                        .ok(GeneralResponse.success(toResponse(franchise))));
    }

    private FranchiseResponse toResponse(Franchise franchise) {
        return FranchiseResponse.builder()
                .id(franchise.getId())
                .name(franchise.getName())
                .branches(mapBranches(franchise.getBranches()))
                .build();
    }

    private List<BranchResponse> mapBranches(List<com.accenture.test.domain.model.Branch> branches) {
        if (branches == null) return List.of();
        return branches.stream()
                .map(this::mapBranch)
                .toList();
    }

    private BranchResponse mapBranch(com.accenture.test.domain.model.Branch branch) {
        return BranchResponse.builder()
                .id(branch.getId())
                .name(branch.getName())
                .products(mapProducts(branch.getProducts()))
                .build();
    }

    private List<ProductResponse> mapProducts(List<com.accenture.test.domain.model.Product> products) {
        if (products == null) return List.of();
        return products.stream()
                .map(this::mapProduct)
                .toList();
    }

    private ProductResponse mapProduct(com.accenture.test.domain.model.Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .stock(product.getStock())
                .build();
    }
}