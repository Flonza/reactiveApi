package com.accenture.test.application.usecase.product;

import com.accenture.test.domain.model.ProductBranchResult;
import com.accenture.test.domain.repository.IFranchiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class GetTopStockProductUseCase {

    private final IFranchiseRepository franchiseRepository;

    public Flux<ProductBranchResult> execute(String franchiseId) {
        return franchiseRepository.getTopStockProductPerBranch(franchiseId);
    }
}