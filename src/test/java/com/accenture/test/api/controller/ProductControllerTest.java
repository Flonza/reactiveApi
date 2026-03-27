package com.accenture.test.api.controller;

import com.accenture.test.application.dto.request.ProductRequest;
import com.accenture.test.application.usecase.product.*;
import com.accenture.test.domain.model.Branch;
import com.accenture.test.domain.model.Franchise;
import com.accenture.test.domain.model.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebFluxTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private AddProductUseCase addProductUseCase;

    @MockitoBean
    private DeleteProductUseCase deleteProductUseCase;

    @MockitoBean
    private UpdateProductStockUseCase updateProductStockUseCase;

    @MockitoBean
    private UpdateProductNameUseCase updateProductNameUseCase;

    private Franchise buildFranchise() {
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

        return Franchise.builder()
                .id("franchise-001")
                .name("Franquicia Test")
                .branches(List.of(branch))
                .build();
    }

    @Test
    @DisplayName("POST - Debe agregar producto y retornar 201")
    void shouldAddProductAndReturn201() {
        when(addProductUseCase.execute(eq("franchise-001"), eq("branch-001"), any(ProductRequest.class)))
                .thenReturn(Mono.just(buildFranchise()));

        webTestClient.post()
                .uri("/api/franchises/franchise-001/branches/branch-001/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"name\": \"Producto Test\", \"stock\": 100}")
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.status").isEqualTo(201)
                .jsonPath("$.data.branches[0].products[0].name").isEqualTo("Producto Test");
    }

    @Test
    @DisplayName("DELETE - Debe eliminar producto y retornar 200")
    void shouldDeleteProductAndReturn200() {
        when(deleteProductUseCase.execute("franchise-001", "branch-001", "product-001"))
                .thenReturn(Mono.just(buildFranchise()));

        webTestClient.delete()
                .uri("/api/franchises/franchise-001/branches/branch-001/products/product-001")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo(200);
    }

    @Test
    @DisplayName("PATCH /stock - Debe actualizar stock y retornar 200")
    void shouldUpdateStockAndReturn200() {
        when(updateProductStockUseCase.execute("franchise-001", "branch-001", "product-001", 200))
                .thenReturn(Mono.just(buildFranchise()));

        webTestClient.patch()
                .uri("/api/franchises/franchise-001/branches/branch-001/products/product-001/stock?newStock=200")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo(200);
    }

    @Test
    @DisplayName("PATCH /name - Debe actualizar nombre de producto y retornar 200")
    void shouldUpdateProductNameAndReturn200() {
        when(updateProductNameUseCase.execute("franchise-001", "branch-001", "product-001", "Nombre Nuevo"))
                .thenReturn(Mono.just(buildFranchise()));

        webTestClient.patch()
                .uri("/api/franchises/franchise-001/branches/branch-001/products/product-001/name?newName=Nombre Nuevo")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo(200);
    }

    @Test
    @DisplayName("POST - Debe retornar 400 cuando el stock es negativo")
    void shouldReturn400WhenStockIsNegative() {
        webTestClient.post()
                .uri("/api/franchises/franchise-001/branches/branch-001/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"name\": \"Producto Test\", \"stock\": -1}")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.status").isEqualTo(400)
                .jsonPath("$.errorCode").isEqualTo("ERR-401");
    }
}