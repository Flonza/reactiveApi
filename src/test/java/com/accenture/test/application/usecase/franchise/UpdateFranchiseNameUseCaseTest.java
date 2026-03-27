package com.accenture.test.application.usecase.franchise;

import com.accenture.test.domain.model.Franchise;
import com.accenture.test.domain.repository.IFranchiseRepository;
import com.accenture.test.api.exception.custom.FranchiseNotFoundException;
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

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateFranchiseNameUseCaseTest {

    @Mock
    private IFranchiseRepository franchiseRepository;

    @InjectMocks
    private UpdateFranchiseNameUseCase updateFranchiseNameUseCase;

    private Franchise franchise;

    @BeforeEach
    void setUp() {
        franchise = Franchise.builder()
                .id("franchise-001")
                .name("Nombre Nuevo")
                .branches(new ArrayList<>())
                .build();
    }

    @Test
    @DisplayName("Debe actualizar el nombre de la franquicia exitosamente")
    void shouldUpdateFranchiseNameSuccessfully() {
        when(franchiseRepository.updateFranchiseName("franchise-001", "Nombre Nuevo"))
                .thenReturn(Mono.just(franchise));

        StepVerifier.create(updateFranchiseNameUseCase.execute("franchise-001", "Nombre Nuevo"))
                .expectNextMatches(result -> result.getName().equals("Nombre Nuevo"))
                .verifyComplete();

        verify(franchiseRepository, times(1))
                .updateFranchiseName("franchise-001", "Nombre Nuevo");
    }

    @Test
    @DisplayName("Debe lanzar FranchiseNotFoundException cuando no existe la franquicia")
    void shouldThrowFranchiseNotFoundExceptionWhenFranchiseDoesNotExist() {
        when(franchiseRepository.updateFranchiseName("invalid-id", "Nombre Nuevo"))
                .thenReturn(Mono.error(new FranchiseNotFoundException("invalid-id")));

        StepVerifier.create(updateFranchiseNameUseCase.execute("invalid-id", "Nombre Nuevo"))
                .expectErrorMatches(ex -> ex instanceof FranchiseNotFoundException)
                .verify();
    }
}