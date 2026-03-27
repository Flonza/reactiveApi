package com.accenture.test.infrastructure.persistence.repository;

import com.accenture.test.infrastructure.persistence.entity.FranchiseDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IFranchiseMongoRepository extends ReactiveMongoRepository<FranchiseDocument, String> {
}