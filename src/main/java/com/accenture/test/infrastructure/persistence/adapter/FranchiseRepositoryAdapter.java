package com.accenture.test.infrastructure.persistence.adapter;

import com.accenture.test.api.exception.custom.BranchNotFoundException;
import com.accenture.test.api.exception.custom.FranchiseNotFoundException;
import com.accenture.test.api.exception.custom.InvalidStockException;
import com.accenture.test.api.exception.custom.ProductNotFoundException;
import com.accenture.test.domain.model.Branch;
import com.accenture.test.domain.model.Franchise;
import com.accenture.test.domain.model.Product;
import com.accenture.test.domain.model.ProductBranchResult;
import com.accenture.test.domain.repository.IFranchiseRepository;
import com.accenture.test.infrastructure.persistence.mapper.FranchiseMapper;
import com.accenture.test.infrastructure.persistence.repository.IFranchiseMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FranchiseRepositoryAdapter implements IFranchiseRepository {

    private final IFranchiseMongoRepository mongoRepository;
    private final FranchiseMapper mapper;

    @Override
    public Mono<Franchise> save(Franchise franchise) {
        return mongoRepository.save(mapper.toDocument(franchise))
                .map(mapper::toModel);
    }

    @Override
    public Mono<Franchise> findById(String franchiseId) {
        return mongoRepository.findById(franchiseId)
                .map(mapper::toModel)
                .switchIfEmpty(Mono.error(
                        new FranchiseNotFoundException(franchiseId)
                ));
    }

    @Override
    public Flux<Franchise> findAll() {
        return mongoRepository.findAll()
                .map(mapper::toModel);
    }

    @Override
    public Mono<Franchise> addBranch(String franchiseId, Branch branch) {
        return mongoRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(
                        new FranchiseNotFoundException(franchiseId)
                ))
                .flatMap(document -> {
                    Branch branchWithId = Branch.builder()
                            .id(UUID.randomUUID().toString())
                            .name(branch.getName())
                            .products(new ArrayList<>())
                            .build();

                    Franchise franchise = mapper.toModel(document);
                    franchise.getBranches().add(branchWithId);

                    return mongoRepository.save(mapper.toDocument(franchise));
                })
                .map(mapper::toModel);
    }

    @Override
    public Mono<Franchise> addProduct(String franchiseId, String branchId, Product product) {
        return mongoRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(
                        new FranchiseNotFoundException(franchiseId)
                ))
                .flatMap(document -> {
                    Franchise franchise = mapper.toModel(document);

                    Branch branch = franchise.getBranches().stream()
                            .filter(b -> b.getId().equals(branchId))
                            .findFirst()
                            .orElseThrow(() -> new BranchNotFoundException(branchId));

                    if (product.getStock() < 0) {
                        throw new InvalidStockException(product.getStock());
                    }

                    Product productWithId = Product.builder()
                            .id(UUID.randomUUID().toString())
                            .name(product.getName())
                            .stock(product.getStock())
                            .build();

                    branch.getProducts().add(productWithId);

                    return mongoRepository.save(mapper.toDocument(franchise));
                })
                .map(mapper::toModel);
    }

    @Override
    public Mono<Franchise> deleteProduct(String franchiseId, String branchId, String productId) {
        return mongoRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(
                        new FranchiseNotFoundException(franchiseId)
                ))
                .flatMap(document -> {
                    Franchise franchise = mapper.toModel(document);

                    Branch branch = franchise.getBranches().stream()
                            .filter(b -> b.getId().equals(branchId))
                            .findFirst()
                            .orElseThrow(() -> new BranchNotFoundException(branchId));

                    boolean productExists = branch.getProducts().stream()
                            .anyMatch(p -> p.getId().equals(productId));

                    if (!productExists) {
                        throw new ProductNotFoundException(productId);
                    }

                    List<Product> updatedProducts = branch.getProducts().stream()
                            .filter(p -> !p.getId().equals(productId))
                            .toList();

                    branch.setProducts(updatedProducts);

                    return mongoRepository.save(mapper.toDocument(franchise));
                })
                .map(mapper::toModel);
    }

    @Override
    public Mono<Franchise> updateProductStock(String franchiseId, String branchId, String productId, int newStock) {
        return mongoRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(
                        new FranchiseNotFoundException(franchiseId)
                ))
                .flatMap(document -> {
                    Franchise franchise = mapper.toModel(document);

                    Branch branch = franchise.getBranches().stream()
                            .filter(b -> b.getId().equals(branchId))
                            .findFirst()
                            .orElseThrow(() -> new BranchNotFoundException(branchId));

                    Product product = branch.getProducts().stream()
                            .filter(p -> p.getId().equals(productId))
                            .findFirst()
                            .orElseThrow(() -> new ProductNotFoundException(productId));

                    if (newStock < 0) {
                        throw new InvalidStockException(newStock);
                    }

                    product.setStock(newStock);

                    return mongoRepository.save(mapper.toDocument(franchise));
                })
                .map(mapper::toModel);
    }

    @Override
    public Flux<ProductBranchResult> getTopStockProductPerBranch(String franchiseId) {
        return mongoRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(
                        new FranchiseNotFoundException(franchiseId)
                ))
                .flatMapMany(document -> {
                    Franchise franchise = mapper.toModel(document);

                    List<ProductBranchResult> results = franchise.getBranches().stream()
                            .filter(branch -> branch.getProducts() != null && !branch.getProducts().isEmpty())
                            .map(branch -> {
                                Product topProduct = branch.getProducts().stream()
                                        .max(Comparator.comparingInt(Product::getStock))
                                        .orElseThrow(() -> new ProductNotFoundException(branch.getId()));

                                return ProductBranchResult.builder()
                                        .branchId(branch.getId())
                                        .branchName(branch.getName())
                                        .productId(topProduct.getId())
                                        .productName(topProduct.getName())
                                        .stock(topProduct.getStock())
                                        .build();
                            })
                            .toList();

                    return Flux.fromIterable(results);
                });
    }

    @Override
    public Mono<Franchise> updateFranchiseName(String franchiseId, String newName) {
        return mongoRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(
                        new FranchiseNotFoundException(franchiseId)
                ))
                .flatMap(document -> {
                    Franchise franchise = mapper.toModel(document);
                    franchise.setName(newName);
                    return mongoRepository.save(mapper.toDocument(franchise));
                })
                .map(mapper::toModel);
    }

    @Override
    public Mono<Franchise> updateBranchName(String franchiseId, String branchId, String newName) {
        return mongoRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(
                        new FranchiseNotFoundException(franchiseId)
                ))
                .flatMap(document -> {
                    Franchise franchise = mapper.toModel(document);

                    Branch branch = franchise.getBranches().stream()
                            .filter(b -> b.getId().equals(branchId))
                            .findFirst()
                            .orElseThrow(() -> new BranchNotFoundException(branchId));

                    branch.setName(newName);

                    return mongoRepository.save(mapper.toDocument(franchise));
                })
                .map(mapper::toModel);
    }

    @Override
    public Mono<Franchise> updateProductName(String franchiseId, String branchId, String productId, String newName) {
        return mongoRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(
                        new FranchiseNotFoundException(franchiseId)
                ))
                .flatMap(document -> {
                    Franchise franchise = mapper.toModel(document);

                    Branch branch = franchise.getBranches().stream()
                            .filter(b -> b.getId().equals(branchId))
                            .findFirst()
                            .orElseThrow(() -> new BranchNotFoundException(branchId));

                    Product product = branch.getProducts().stream()
                            .filter(p -> p.getId().equals(productId))
                            .findFirst()
                            .orElseThrow(() -> new ProductNotFoundException(productId));

                    product.setName(newName);

                    return mongoRepository.save(mapper.toDocument(franchise));
                })
                .map(mapper::toModel);
    }
}