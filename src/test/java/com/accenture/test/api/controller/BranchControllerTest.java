package com.accenture.test.api.controller;

import com.accenture.test.application.dto.request.BranchRequest;
import com.accenture.test.application.usecase.branch.AddBranchUseCase;
import com.accenture.test.application.usecase.branch.UpdateBranchNameUseCase;
import com.accenture.test.domain.model.Branch;
import com.accenture.test.domain.model.Franchise;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebFluxTest(BranchController.class)
class BranchControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private AddBranchUseCase addBranchUseCase;

    @MockitoBean
    private UpdateBranchNameUseCase updateBranchNameUseCase;

    @Test
    @DisplayName("POST /api/franchises/{id}/branches - Debe agregar sucursal y retornar 201")
    void shouldAddBranchAndReturn201() {
        Branch branch = Branch.builder()
                .id("branch-001")
                .name("Sucursal Norte")
                .products(new ArrayList<>())
                .build();

        Franchise franchise = Franchise.builder()
                .id("franchise-001")
                .name("Franquicia Test")
                .branches(List.of(branch))
                .build();

        when(addBranchUseCase.execute(eq("franchise-001"), any(BranchRequest.class)))
                .thenReturn(Mono.just(franchise));

        webTestClient.post()
                .uri("/api/franchises/franchise-001/branches")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"name\": \"Sucursal Norte\"}")
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.status").isEqualTo(201)
                .jsonPath("$.data.branches[0].name").isEqualTo("Sucursal Norte");
    }

    @Test
    @DisplayName("POST /api/franchises/{id}/branches - Debe retornar 400 cuando el nombre está vacío")
    void shouldReturn400WhenNameIsEmpty() {
        webTestClient.post()
                .uri("/api/franchises/franchise-001/branches")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"name\": \"\"}")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.status").isEqualTo(400)
                .jsonPath("$.errorCode").isEqualTo("ERR-401");
    }

    @Test
    @DisplayName("PATCH /api/franchises/{id}/branches/{id}/name - Debe actualizar nombre y retornar 200")
    void shouldUpdateBranchNameAndReturn200() {
        Branch branch = Branch.builder()
                .id("branch-001")
                .name("Nombre Nuevo")
                .products(new ArrayList<>())
                .build();

        Franchise franchise = Franchise.builder()
                .id("franchise-001")
                .name("Franquicia Test")
                .branches(List.of(branch))
                .build();

        when(updateBranchNameUseCase.execute(eq("franchise-001"), eq("branch-001"), eq("Nombre Nuevo")))
                .thenReturn(Mono.just(franchise));

        webTestClient.patch()
                .uri("/api/franchises/franchise-001/branches/branch-001/name?newName=Nombre Nuevo")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo(200)
                .jsonPath("$.data.branches[0].name").isEqualTo("Nombre Nuevo");
    }
}