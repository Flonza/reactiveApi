package com.accenture.test.application.usecase.product;

import com.accenture.test.api.exception.custom.FranchiseNotFoundException;
import com.accenture.test.domain.model.ProductBranchResult;
import com.accenture.test.domain.repository.IFranchiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetTopStockProductUseCaseTest {

    @Mock
    private IFranchiseRepository franchiseRepository;

    @InjectMocks
    private GetTopStockProductUseCase getTopStockProductUseCase;

    private List<ProductBranchResult> results;

    @BeforeEach
    void setUp() {
        results = List.of(
                ProductBranchResult.builder()
                        .branchId("branch-001")
                        .branchName("Sucursal Norte")
                        .productId("product-001")
                        .productName("Producto A")
                        .stock(100)
                        .build(),
                ProductBranchResult.builder()
                        .branchId("branch-002")
                        .branchName("Sucursal Sur")
                        .productId("product-002")
                        .productName("Producto B")
                        .stock(200)
                        .build()
        );
    }

    @Test
    @DisplayName("Debe retornar el producto con más stock por sucursal")
    void shouldReturnTopStockProductPerBranch() {
        when(franchiseRepository.getTopStockProductPerBranch("franchise-001"))
                .thenReturn(Flux.fromIterable(results));

        StepVerifier.create(getTopStockProductUseCase.execute("franchise-001"))
                .expectNextMatches(r ->
                        r.getBranchName().equals("Sucursal Norte") &&
                                r.getStock() == 100
                )
                .expectNextMatches(r ->
                        r.getBranchName().equals("Sucursal Sur") &&
                                r.getStock() == 200
                )
                .verifyComplete();

        verify(franchiseRepository, times(1)).getTopStockProductPerBranch("franchise-001");
    }

    @Test
    @DisplayName("Debe lanzar FranchiseNotFoundException cuando no existe la franquicia")
    void shouldThrowFranchiseNotFoundWhenFranchiseDoesNotExist() {
        when(franchiseRepository.getTopStockProductPerBranch("invalid-id"))
                .thenReturn(Flux.error(new FranchiseNotFoundException("invalid-id")));

        StepVerifier.create(getTopStockProductUseCase.execute("invalid-id"))
                .expectErrorMatches(ex -> ex instanceof FranchiseNotFoundException)
                .verify();
    }
}