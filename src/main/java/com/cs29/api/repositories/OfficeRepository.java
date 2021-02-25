package com.cs29.api.repositories;

import com.cs29.api.models.Office;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface OfficeRepository extends MongoRepository<Office, String> {
    Optional<Office> findDistinctByNameAndUserIdAndRegionId(String name, String userId, String regionId);

    Optional<List<Office>> findAllByUserId(String userId);

}
