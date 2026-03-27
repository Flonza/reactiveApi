package com.accenture.test.application.usecase.franchise;

import com.accenture.test.domain.model.Franchise;
import com.accenture.test.application.dto.request.FranchiseRequest;
import com.accenture.test.domain.repository.IFranchiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class CreateFranchiseUseCase {

    private final IFranchiseRepository franchiseRepository;

    public Mono<Franchise> execute(FranchiseRequest request) {
        Franchise franchise = Franchise.builder()
                .name(request.getName())
                .branches(new ArrayList<>())
                .build();
        return franchiseRepository.save(franchise);
    }
}