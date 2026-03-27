package com.accenture.test.application.usecase.branch;

import com.accenture.test.domain.model.Branch;
import com.accenture.test.domain.model.Franchise;
import com.accenture.test.domain.repository.IFranchiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateBranchNameUseCaseTest {

    @Mock
    private IFranchiseRepository franchiseRepository;

    @InjectMocks
    private UpdateBranchNameUseCase updateBranchNameUseCase;

    private static final String FRANCHISE_ID = "franchise-001";
    private static final String BRANCH_ID    = "branch-001";
    private static final String NEW_NAME     = "Sucursal Renovada";

    private Franchise franchiseWithUpdatedBranch;

    @BeforeEach
    void setUp() {
        Branch updatedBranch = Branch.builder()
                .id(BRANCH_ID)
                .name(NEW_NAME)
                .build();

        franchiseWithUpdatedBranch = Franchise.builder()
                .id(FRANCHISE_ID)
                .name("Franquicia Test")
                .branches(List.of(updatedBranch))
                .build();
    }

    @Test
    void execute_shouldUpdateBranchNameSuccessfully() {
        when(franchiseRepository.updateBranchName(FRANCHISE_ID, BRANCH_ID, NEW_NAME))
                .thenReturn(Mono.just(franchiseWithUpdatedBranch));

        StepVerifier.create(updateBranchNameUseCase.execute(FRANCHISE_ID, BRANCH_ID, NEW_NAME))
                .expectNextMatches(franchise ->
                        franchise.getId().equals(FRANCHISE_ID) &&
                                franchise.getBranches().get(0).getName().equals(NEW_NAME) &&
                                franchise.getBranches().get(0).getId().equals(BRANCH_ID)
                )
                .verifyComplete();

        verify(franchiseRepository, times(1))
                .updateBranchName(FRANCHISE_ID, BRANCH_ID, NEW_NAME);
    }

    @Test
    void execute_shouldDelegateToRepositoryWithCorrectParameters() {
        when(franchiseRepository.updateBranchName(FRANCHISE_ID, BRANCH_ID, NEW_NAME))
                .thenReturn(Mono.just(franchiseWithUpdatedBranch));

        updateBranchNameUseCase.execute(FRANCHISE_ID, BRANCH_ID, NEW_NAME).block();

        verify(franchiseRepository).updateBranchName(
                eq(FRANCHISE_ID),
                eq(BRANCH_ID),
                eq(NEW_NAME)
        );
        verifyNoMoreInteractions(franchiseRepository);
    }

    @Test
    void execute_shouldPropagateErrorWhenRepositoryFails() {
        RuntimeException repositoryException = new RuntimeException("Error al actualizar la sucursal");

        when(franchiseRepository.updateBranchName(FRANCHISE_ID, BRANCH_ID, NEW_NAME))
                .thenReturn(Mono.error(repositoryException));

        StepVerifier.create(updateBranchNameUseCase.execute(FRANCHISE_ID, BRANCH_ID, NEW_NAME))
                .expectErrorMatches(error ->
                        error instanceof RuntimeException &&
                                error.getMessage().equals("Error al actualizar la sucursal")
                )
                .verify();

        verify(franchiseRepository, times(1))
                .updateBranchName(FRANCHISE_ID, BRANCH_ID, NEW_NAME);
    }

    @Test
    void execute_shouldReturnEmptyWhenRepositoryReturnsEmpty() {
        when(franchiseRepository.updateBranchName(FRANCHISE_ID, BRANCH_ID, NEW_NAME))
                .thenReturn(Mono.empty());

        StepVerifier.create(updateBranchNameUseCase.execute(FRANCHISE_ID, BRANCH_ID, NEW_NAME))
                .verifyComplete();

        verify(franchiseRepository, times(1))
                .updateBranchName(FRANCHISE_ID, BRANCH_ID, NEW_NAME);
    }

    @Test
    void execute_shouldOnlyCallRepositoryOnce() {
        when(franchiseRepository.updateBranchName(anyString(), anyString(), anyString()))
                .thenReturn(Mono.just(franchiseWithUpdatedBranch));

        updateBranchNameUseCase.execute(FRANCHISE_ID, BRANCH_ID, NEW_NAME).block();

        verify(franchiseRepository, times(1))
                .updateBranchName(anyString(), anyString(), anyString());
    }
}