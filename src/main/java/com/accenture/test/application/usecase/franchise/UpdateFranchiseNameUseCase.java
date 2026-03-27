package com.accenture.test.application.usecase.franchise;

import com.accenture.test.domain.model.Franchise;
import com.accenture.test.domain.repository.IFranchiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UpdateFranchiseNameUseCase {

    private final IFranchiseRepository franchiseRepository;

    public Mono<Franchise> execute(String franchiseId, String newName) {
        return franchiseRepository.updateFranchiseName(franchiseId, newName);
    }
}