package com.accenture.test.api.controller;

import com.accenture.test.application.dto.request.FranchiseRequest;
import com.accenture.test.application.dto.response.*;
import com.accenture.test.application.usecase.franchise.CreateFranchiseUseCase;
import com.accenture.test.application.usecase.franchise.UpdateFranchiseNameUseCase;
import com.accenture.test.application.usecase.product.GetTopStockProductUseCase;
import com.accenture.test.domain.model.Branch;
import com.accenture.test.domain.model.Franchise;
import com.accenture.test.domain.model.Product;
import com.accenture.test.domain.model.ProductBranchResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/franchises")
@RequiredArgsConstructor
public class FranchiseController {

    private final CreateFranchiseUseCase createFranchiseUseCase;
    private final UpdateFranchiseNameUseCase updateFranchiseNameUseCase;
    private final GetTopStockProductUseCase getTopStockProductUseCase;


    @PostMapping
    public Mono<ResponseEntity<GeneralResponse<FranchiseResponse>>> create(
            @Valid @RequestBody FranchiseRequest request) {
        return createFranchiseUseCase.execute(request)
                .map(franchise -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(GeneralResponse.created(toResponse(franchise))));
    }


    @PatchMapping("/{franchiseId}/name")
    public Mono<ResponseEntity<GeneralResponse<FranchiseResponse>>> updateName(
            @PathVariable String franchiseId,
            @RequestParam String newName) {
        return updateFranchiseNameUseCase.execute(franchiseId, newName)
                .map(franchise -> ResponseEntity
                        .ok(GeneralResponse.success(toResponse(franchise))));
    }


    @GetMapping("/{franchiseId}/top-stock")
    public Mono<ResponseEntity<GeneralResponse<List<ProductBranchResponse>>>> getTopStock(
            @PathVariable String franchiseId) {
        return getTopStockProductUseCase.execute(franchiseId)
                .map(this::toProductBranchResponse)
                .collectList()
                .map(list -> ResponseEntity
                        .ok(GeneralResponse.success(list)));
    }



    private FranchiseResponse toResponse(Franchise franchise) {

        List<Branch> branches = franchise.getBranches() == null
                ? List.of()
                : franchise.getBranches();

        return FranchiseResponse.builder()
                .id(franchise.getId())
                .name(franchise.getName())
                .branches(
                        branches.stream()
                                .map(branch -> {

                                    List<Product> products = branch.getProducts() == null
                                            ? List.of()
                                            : branch.getProducts();

                                    return BranchResponse.builder()
                                            .id(branch.getId())
                                            .name(branch.getName())
                                            .products(
                                                    products.stream()
                                                            .map(product -> ProductResponse.builder()
                                                                    .id(product.getId())
                                                                    .name(product.getName())
                                                                    .stock(product.getStock())
                                                                    .build())
                                                            .toList()
                                            )
                                            .build();
                                })
                                .toList()
                )
                .build();
    }

    private ProductBranchResponse toProductBranchResponse(ProductBranchResult result) {
        return ProductBranchResponse.builder()
                .branchId(result.getBranchId())
                .branchName(result.getBranchName())
                .productId(result.getProductId())
                .productName(result.getProductName())
                .stock(result.getStock())
                .build();
    }
}