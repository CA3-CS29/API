package com.cs29.api.repositories;

import com.cs29.api.models.Portfolio;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PortfolioRepository extends MongoRepository<Portfolio, String> {
    Optional<Portfolio> findDistinctByTagAndUserId(String tag, String userId);

    Optional<List<Portfolio>> findAllByTag(String Tag);

    Optional<List<Portfolio>> findAllByCreatedOn(String createdOn);

    Optional<List<Portfolio>> findAllByCreatedOnAndUserId(String createdOn, String userId);
}
