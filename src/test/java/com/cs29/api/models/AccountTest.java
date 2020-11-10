package com.cs29.api.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountTest {
    private final ObjectMapper MAPPER = new ObjectMapper();
    private final String TEST_ID = "TEST_ID";
    private final int TEST_NUM_PORTFOLIOS = 0;
    private final Account TEST_ACCOUNT = Account
            .builder()
            .userId(TEST_ID)
            .numPortfolios(TEST_NUM_PORTFOLIOS)
            .portfolios(null)
            .build();
    private final String TEST_SERIALIZED_ACCOUNT = "{\"user_id\":\"TEST_ID\",\"num_portfolios\":0,\"portfolios\":null}";

    @SneakyThrows
    @Test
    public void givenAccountSerializationSucceeds() {
        String serializedAccount = MAPPER.writeValueAsString(TEST_ACCOUNT);
        assertEquals(serializedAccount, TEST_SERIALIZED_ACCOUNT);
    }

    @SneakyThrows
    @Test
    public void givenAccountJsonDeserializeSucceeds() {
        Account account = MAPPER.readValue(TEST_SERIALIZED_ACCOUNT, Account.class);
        assertEquals(account, TEST_ACCOUNT);
    }
}
