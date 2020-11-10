package com.cs29.api.dtos;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountDtoTest {
    private final ObjectMapper MAPPER = new ObjectMapper();
    private final String TEST_ID = "TEST_ID";
    private final int TEST_NUM_PORTFOLIOS = 0;
    private final AccountDto TEST_ACCOUNT_DTO = AccountDto
            .builder()
            .userId(TEST_ID)
            .numPortfolios(TEST_NUM_PORTFOLIOS)
            .portfolios(null)
            .build();
    private final String TEST_SERIALIZED_ACCOUNT_DTO = "{\"user_id\":\"TEST_ID\",\"num_portfolios\":0}";

    @SneakyThrows
    @Test
    public void givenAccountDtoSerializationSucceeds() {
        String serializedAccount = MAPPER.writeValueAsString(TEST_ACCOUNT_DTO);
        assertEquals(serializedAccount, TEST_SERIALIZED_ACCOUNT_DTO);
    }

    @SneakyThrows
    @Test
    public void givenAccountDtoJsonDeserializeSucceeds() {
        AccountDto account = MAPPER.readValue(TEST_SERIALIZED_ACCOUNT_DTO, AccountDto.class);
        assertEquals(account, TEST_ACCOUNT_DTO);
    }
}
