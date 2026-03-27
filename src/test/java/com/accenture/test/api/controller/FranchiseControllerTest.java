package com.accenture.test.api.controller;

import com.accenture.test.application.dto.request.FranchiseRequest;
import com.accenture.test.application.usecase.franchise.CreateFranchiseUseCase;
import com.accenture.test.application.usecase.franchise.UpdateFranchiseNameUseCase;
import com.accenture.test.application.usecase.product.GetTopStockProductUseCase;
import com.accenture.test.domain.model.Franchise;
import com.accenture.test.domain.model.ProductBranchResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebFluxTest(FranchiseController.class)
class FranchiseControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private CreateFranchiseUseCase createFranchiseUseCase;

    @MockitoBean
    private UpdateFranchiseNameUseCase updateFranchiseNameUseCase;

    @MockitoBean
    private GetTopStockProductUseCase getTopStockProductUseCase;

    @Test
    @DisplayName("POST /api/franchises - Debe crear franquicia y retornar 201")
    void shouldCreateFranchiseAndReturn201() {
        Franchise franchise = Franchise.builder()
                .id("franchise-001")
                .name("Franquicia Test")
                .branches(new ArrayList<>())
                .build();

        when(createFranchiseUseCase.execute(any(FranchiseRequest.class)))
                .thenReturn(Mono.just(franchise));

        webTestClient.post()
                .uri("/api/franchises")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"name\": \"Franquicia Test\"}")
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.status").isEqualTo(201)
                .jsonPath("$.data.name").isEqualTo("Franquicia Test");
    }

    @Test
    @DisplayName("POST /api/franchises - Debe retornar 400 cuando el nombre está vacío")
    void shouldReturn400WhenNameIsEmpty() {
        webTestClient.post()
                .uri("/api/franchises")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"name\": \"\"}")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.status").isEqualTo(400)
                .jsonPath("$.errorCode").isEqualTo("ERR-401");
    }

    @Test
    @DisplayName("PATCH /api/franchises/{id}/name - Debe actualizar nombre y retornar 200")
    void shouldUpdateFranchiseNameAndReturn200() {
        Franchise franchise = Franchise.builder()
                .id("franchise-001")
                .name("Nombre Nuevo")
                .branches(new ArrayList<>())
                .build();

        when(updateFranchiseNameUseCase.execute(eq("franchise-001"), eq("Nombre Nuevo")))
                .thenReturn(Mono.just(franchise));

        webTestClient.patch()
                .uri("/api/franchises/franchise-001/name?newName=Nombre Nuevo")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo(200)
                .jsonPath("$.data.name").isEqualTo("Nombre Nuevo");
    }

    @Test
    @DisplayName("GET /api/franchises/{id}/top-stock - Debe retornar productos con más stock")
    void shouldReturnTopStockProducts() {
        ProductBranchResult result = ProductBranchResult.builder()
                .branchId("branch-001")
                .branchName("Sucursal Norte")
                .productId("product-001")
                .productName("Producto A")
                .stock(100)
                .build();

        when(getTopStockProductUseCase.execute("franchise-001"))
                .thenReturn(Flux.just(result));

        webTestClient.get()
                .uri("/api/franchises/franchise-001/top-stock")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo(200)
                .jsonPath("$.data[0].branchName").isEqualTo("Sucursal Norte")
                .jsonPath("$.data[0].stock").isEqualTo(100);
    }
}