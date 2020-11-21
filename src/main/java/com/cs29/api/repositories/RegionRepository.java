package com.cs29.api.repositories;

import com.cs29.api.models.Region;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface RegionRepository extends MongoRepository<Region, String> {
    Optional<Region> findDistinctByNameAndUserId(String name, String userId);

    Optional<List<Region>> findAllByUserId(String userId);

}
