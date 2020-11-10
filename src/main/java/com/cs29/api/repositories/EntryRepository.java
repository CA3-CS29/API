package com.cs29.api.repositories;

import com.cs29.api.models.Entry;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EntryRepository extends MongoRepository<Entry, String> {
    Optional<Entry> findByTagAndOfficeId(String tag, String officeId);

    Optional<List<Entry>> findAllByOfficeId(String officeId);

    Optional<List<Entry>> findAllBySource(String source);
}
