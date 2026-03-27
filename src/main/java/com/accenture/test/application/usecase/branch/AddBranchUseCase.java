package com.accenture.test.application.usecase.branch;

import com.accenture.test.domain.model.Branch;
import com.accenture.test.domain.model.Franchise;
import com.accenture.test.domain.repository.IFranchiseRepository;
import com.accenture.test.application.dto.request.BranchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class AddBranchUseCase {

    private final IFranchiseRepository franchiseRepository;

    public Mono<Franchise> execute(String franchiseId, BranchRequest request) {
        Branch branch = Branch.builder()
                .name(request.getName())
                .products(new ArrayList<>())
                .build();
        return franchiseRepository.addBranch(franchiseId, branch);
    }
}