package com.cs29.api.repositories;

import com.cs29.api.models.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AccountRepository extends MongoRepository<Account, String> {
    Optional<Account> findDistinctByUserId(String userId);
}
