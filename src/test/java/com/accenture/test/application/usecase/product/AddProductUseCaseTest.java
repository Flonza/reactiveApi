package com.accenture.test.application.usecase.product;

import com.accenture.test.application.dto.request.ProductRequest;
import com.accenture.test.api.exception.custom.BranchNotFoundException;
import com.accenture.test.api.exception.custom.InvalidStockException;
import com.accenture.test.domain.model.Branch;
import com.accenture.test.domain.model.Franchise;
import com.accenture.test.domain.model.Product;
import com.accenture.test.domain.repository.IFranchiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddProductUseCaseTest {

    @Mock
    private IFranchiseRepository franchiseRepository;

    @InjectMocks
    private AddProductUseCase addProductUseCase;

    private Franchise franchise;
    private ProductRequest request;

    @BeforeEach
    void setUp() {
        request = ProductRequest.builder()
                .name("Producto Test")
                .stock(100)
                .build();

        Product product = Product.builder()
                .id("product-001")
                .name("Producto Test")
                .stock(100)
                .build();

        Branch branch = Branch.builder()
                .id("branch-001")
                .name("Sucursal Norte")
                .products(List.of(product))
                .build();

        franchise = Franchise.builder()
                .id("franchise-001")
                .name("Franquicia Test")
                .branches(List.of(branch))
                .build();
    }

    @Test
    @DisplayName("Debe agregar un producto exitosamente")
    void shouldAddProductSuccessfully() {
        when(franchiseRepository.addProduct(eq("franchise-001"), eq("branch-001"), any(Product.class)))
                .thenReturn(Mono.just(franchise));

        StepVerifier.create(addProductUseCase.execute("franchise-001", "branch-001", request))
                .expectNextMatches(result ->
                        result.getBranches().get(0).getProducts().size() == 1 &&
                                result.getBranches().get(0).getProducts().get(0).getName().equals("Producto Test")
                )
                .verifyComplete();

        verify(franchiseRepository, times(1))
                .addProduct(eq("franchise-001"), eq("branch-001"), any(Product.class));
    }

    @Test
    @DisplayName("Debe lanzar BranchNotFoundException cuando no existe la sucursal")
    void shouldThrowBranchNotFoundWhenBranchDoesNotExist() {
        when(franchiseRepository.addProduct(eq("franchise-001"), eq("invalid-id"), any(Product.class)))
                .thenReturn(Mono.error(new BranchNotFoundException("invalid-id")));

        StepVerifier.create(addProductUseCase.execute("franchise-001", "invalid-id", request))
                .expectErrorMatches(ex -> ex instanceof BranchNotFoundException)
                .verify();
    }

    @Test
    @DisplayName("Debe lanzar InvalidStockException cuando el stock es negativo")
    void shouldThrowInvalidStockExceptionWhenStockIsNegative() {
        when(franchiseRepository.addProduct(eq("franchise-001"), eq("branch-001"), any(Product.class)))
                .thenReturn(Mono.error(new InvalidStockException(-1)));

        ProductRequest invalidRequest = ProductRequest.builder()
                .name("Producto Test")
                .stock(-1)
                .build();

        StepVerifier.create(addProductUseCase.execute("franchise-001", "branch-001", invalidRequest))
                .expectErrorMatches(ex -> ex instanceof InvalidStockException)
                .verify();
    }
}