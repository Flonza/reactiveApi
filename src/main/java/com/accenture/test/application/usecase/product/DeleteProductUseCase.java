package com.accenture.test.application.usecase.product;

import com.accenture.test.domain.model.Franchise;
import com.accenture.test.domain.repository.IFranchiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class DeleteProductUseCase {

    private final IFranchiseRepository franchiseRepository;

    public Mono<Franchise> execute(String franchiseId, String branchId, String productId) {
        return franchiseRepository.deleteProduct(franchiseId, branchId, productId);
    }
}