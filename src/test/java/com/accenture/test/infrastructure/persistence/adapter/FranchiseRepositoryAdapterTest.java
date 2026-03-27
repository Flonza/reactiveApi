package com.accenture.test.infrastructure.persistence.adapter;

import com.accenture.test.api.exception.custom.BranchNotFoundException;
import com.accenture.test.api.exception.custom.FranchiseNotFoundException;
import com.accenture.test.api.exception.custom.InvalidStockException;
import com.accenture.test.api.exception.custom.ProductNotFoundException;
import com.accenture.test.domain.model.Branch;
import com.accenture.test.domain.model.Franchise;
import com.accenture.test.domain.model.Product;
import com.accenture.test.infrastructure.persistence.entity.FranchiseDocument;
import com.accenture.test.infrastructure.persistence.mapper.FranchiseMapper;
import com.accenture.test.infrastructure.persistence.repository.IFranchiseMongoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FranchiseRepositoryAdapterTest {

    @Mock
    private IFranchiseMongoRepository mongoRepository;

    @Mock
    private FranchiseMapper mapper;

    @InjectMocks
    private FranchiseRepositoryAdapter adapter;

    private Franchise franchise;
    private FranchiseDocument franchiseDocument;

    @BeforeEach
    void setUp() {
        Product product = Product.builder()
                .id("product-001")
                .name("Producto A")
                .stock(100)
                .build();

        Branch branch = Branch.builder()
                .id("branch-001")
                .name("Sucursal Norte")
                .products(new ArrayList<>(List.of(product)))
                .build();

        franchise = Franchise.builder()
                .id("franchise-001")
                .name("Franquicia Test")
                .branches(new ArrayList<>(List.of(branch)))
                .build();

        FranchiseDocument.ProductDocument productDoc =
                new FranchiseDocument.ProductDocument("product-001", "Producto A", 100);

        FranchiseDocument.BranchDocument branchDoc =
                new FranchiseDocument.BranchDocument("branch-001", "Sucursal Norte",
                        new ArrayList<>(List.of(productDoc)));

        franchiseDocument = new FranchiseDocument("franchise-001", "Franquicia Test",
                new ArrayList<>(List.of(branchDoc)));
    }

    @Test
    @DisplayName("Debe guardar una franquicia exitosamente")
    void shouldSaveFranchiseSuccessfully() {
        when(mapper.toDocument(franchise)).thenReturn(franchiseDocument);
        when(mongoRepository.save(franchiseDocument)).thenReturn(Mono.just(franchiseDocument));
        when(mapper.toModel(franchiseDocument)).thenReturn(franchise);

        StepVerifier.create(adapter.save(franchise))
                .expectNextMatches(result -> result.getId().equals("franchise-001"))
                .verifyComplete();

        verify(mongoRepository, times(1)).save(franchiseDocument);
    }

    @Test
    @DisplayName("Debe lanzar FranchiseNotFoundException cuando no existe")
    void shouldThrowFranchiseNotFoundWhenNotExists() {
        when(mongoRepository.findById("invalid-id")).thenReturn(Mono.empty());

        StepVerifier.create(adapter.findById("invalid-id"))
                .expectErrorMatches(ex -> ex instanceof FranchiseNotFoundException)
                .verify();
    }

    @Test
    @DisplayName("Debe lanzar BranchNotFoundException cuando la sucursal no existe")
    void shouldThrowBranchNotFoundWhenBranchDoesNotExist() {
        when(mongoRepository.findById("franchise-001")).thenReturn(Mono.just(franchiseDocument));
        when(mapper.toModel(franchiseDocument)).thenReturn(franchise);

        StepVerifier.create(adapter.addProduct("franchise-001", "invalid-branch", Product.builder()
                        .name("Producto")
                        .stock(10)
                        .build()))
                .expectErrorMatches(ex -> ex instanceof BranchNotFoundException)
                .verify();
    }

    @Test
    @DisplayName("Debe lanzar InvalidStockException cuando el stock es negativo")
    void shouldThrowInvalidStockExceptionWhenStockIsNegative() {
        when(mongoRepository.findById("franchise-001")).thenReturn(Mono.just(franchiseDocument));
        when(mapper.toModel(franchiseDocument)).thenReturn(franchise);

        StepVerifier.create(adapter.addProduct("franchise-001", "branch-001", Product.builder()
                        .name("Producto")
                        .stock(-5)
                        .build()))
                .expectErrorMatches(ex -> ex instanceof InvalidStockException)
                .verify();
    }

    @Test
    @DisplayName("Debe lanzar ProductNotFoundException cuando el producto no existe")
    void shouldThrowProductNotFoundWhenProductDoesNotExist() {
        when(mongoRepository.findById("franchise-001")).thenReturn(Mono.just(franchiseDocument));
        when(mapper.toModel(franchiseDocument)).thenReturn(franchise);

        StepVerifier.create(adapter.deleteProduct("franchise-001", "branch-001", "invalid-product"))
                .expectErrorMatches(ex -> ex instanceof ProductNotFoundException)
                .verify();
    }

    @Test
    @DisplayName("Debe retornar el producto con más stock por sucursal")
    void shouldReturnTopStockProductPerBranch() {
        when(mongoRepository.findById("franchise-001")).thenReturn(Mono.just(franchiseDocument));
        when(mapper.toModel(franchiseDocument)).thenReturn(franchise);

        StepVerifier.create(adapter.getTopStockProductPerBranch("franchise-001"))
                .expectNextMatches(result ->
                        result.getBranchId().equals("branch-001") &&
                                result.getStock() == 100
                )
                .verifyComplete();
    }
}