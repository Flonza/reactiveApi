package com.accenture.test.application.usecase.product;

import com.accenture.test.domain.model.Franchise;
import com.accenture.test.domain.model.Product;
import com.accenture.test.domain.repository.IFranchiseRepository;
import com.accenture.test.application.dto.request.ProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AddProductUseCase {

    private final IFranchiseRepository franchiseRepository;

    public Mono<Franchise> execute(String franchiseId, String branchId, ProductRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .stock(request.getStock())
                .build();
        return franchiseRepository.addProduct(franchiseId, branchId, product);
    }
}