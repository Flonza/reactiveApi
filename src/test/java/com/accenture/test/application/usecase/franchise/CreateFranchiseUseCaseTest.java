package com.accenture.test.application.usecase.franchise;

import com.accenture.test.application.dto.request.FranchiseRequest;
import com.accenture.test.domain.model.Franchise;
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

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateFranchiseUseCaseTest {

    @Mock
    private IFranchiseRepository franchiseRepository;

    @InjectMocks
    private CreateFranchiseUseCase createFranchiseUseCase;

    private Franchise franchise;
    private FranchiseRequest request;

    @BeforeEach
    void setUp() {
        request = FranchiseRequest.builder()
                .name("Franquicia Test")
                .build();

        franchise = Franchise.builder()
                .id("franchise-001")
                .name("Franquicia Test")
                .branches(new ArrayList<>())
                .build();
    }

    @Test
    @DisplayName("Debe crear una franquicia exitosamente")
    void shouldCreateFranchiseSuccessfully() {
        when(franchiseRepository.save(any(Franchise.class)))
                .thenReturn(Mono.just(franchise));

        StepVerifier.create(createFranchiseUseCase.execute(request))
                .expectNextMatches(result ->
                        result.getName().equals("Franquicia Test") &&
                                result.getBranches().isEmpty()
                )
                .verifyComplete();

        verify(franchiseRepository, times(1)).save(any(Franchise.class));
    }

    @Test
    @DisplayName("Debe propagar error cuando falla el repositorio")
    void shouldPropagateErrorWhenRepositoryFails() {
        when(franchiseRepository.save(any(Franchise.class)))
                .thenReturn(Mono.error(new RuntimeException("Database error")));

        StepVerifier.create(createFranchiseUseCase.execute(request))
                .expectErrorMatches(ex ->
                        ex instanceof RuntimeException &&
                                ex.getMessage().equals("Database error")
                )
                .verify();

        verify(franchiseRepository, times(1)).save(any(Franchise.class));
    }
}
