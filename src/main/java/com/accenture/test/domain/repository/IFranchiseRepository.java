package com.accenture.test.domain.repository;


import com.accenture.test.domain.model.Franchise;
import com.accenture.test.domain.model.Product;
import com.accenture.test.domain.model.ProductBranchResult;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IFranchiseRepository {

    // Save a new franchise.
    Mono<Franchise> save(Franchise franchise);

    // Add a branch to a franchise.
    Mono<Franchise> addBranch(String franchiseId, com.accenture.test.domain.model.Branch branch);

    // Add a product to a branch.
    Mono<Franchise> addProduct(String franchiseId, String branchId, Product product);

    // Remove a product from a branch.
    Mono<Franchise> deleteProduct(String franchiseId, String branchId, String productId);

    // Update product stock in a branch.
    Mono<Franchise> updateProductStock(String franchiseId, String branchId, String productId, int newStock);

    // Get the highest stock product per branch.
    Flux<ProductBranchResult> getTopStockProductPerBranch(String franchiseId);

    // Update franchise name.
    Mono<Franchise> updateFranchiseName(String franchiseId, String newName);

    // Update branch name.
    Mono<Franchise> updateBranchName(String franchiseId, String branchId, String newName);

    // Update product name.
    Mono<Franchise> updateProductName(String franchiseId, String branchId, String productId, String newName);

    // Find a franchise by ID.
    Mono<Franchise> findById(String franchiseId);

    // Get all franchises.
    Flux<Franchise> findAll();
}