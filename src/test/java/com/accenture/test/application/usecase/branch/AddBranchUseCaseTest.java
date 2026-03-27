package com.accenture.test.application.usecase.branch;

import com.accenture.test.application.dto.request.BranchRequest;
import com.accenture.test.domain.model.Branch;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddBranchUseCaseTest {

    @Mock
    private IFranchiseRepository franchiseRepository;

    @InjectMocks
    private AddBranchUseCase addBranchUseCase;

    private Franchise franchise;
    private BranchRequest request;

    @BeforeEach
    void setUp() {
        request = BranchRequest.builder()
                .name("Sucursal Norte")
                .build();

        Branch branch = Branch.builder()
                .id("branch-001")
                .name("Sucursal Norte")
                .products(new ArrayList<>())
                .build();

        franchise = Franchise.builder()
                .id("franchise-001")
                .name("Franquicia Test")
                .branches(List.of(branch))
                .build();
    }

    @Test
    @DisplayName("Debe agregar una sucursal exitosamente")
    void shouldAddBranchSuccessfully() {
        when(franchiseRepository.addBranch(eq("franchise-001"), any(Branch.class)))
                .thenReturn(Mono.just(franchise));

        StepVerifier.create(addBranchUseCase.execute("franchise-001", request))
                .expectNextMatches(result ->
                        result.getBranches().size() == 1 &&
                                result.getBranches().get(0).getName().equals("Sucursal Norte")
                )
                .verifyComplete();

        verify(franchiseRepository, times(1)).addBranch(eq("franchise-001"), any(Branch.class));
    }

    @Test
    @DisplayName("Debe lanzar FranchiseNotFoundException cuando no existe la franquicia")
    void shouldThrowFranchiseNotFoundWhenFranchiseDoesNotExist() {
        when(franchiseRepository.addBranch(eq("invalid-id"), any(Branch.class)))
                .thenReturn(Mono.error(new FranchiseNotFoundException("invalid-id")));

        StepVerifier.create(addBranchUseCase.execute("invalid-id", request))
                .expectErrorMatches(ex -> ex instanceof FranchiseNotFoundException)
                .verify();
    }
}