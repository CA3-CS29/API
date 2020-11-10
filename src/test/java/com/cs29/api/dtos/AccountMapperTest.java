package com.cs29.api.dtos;

import com.cs29.api.models.Account;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountMapperTest {
    private final String TEST_ID = "TEST_ID";
    private final int TEST_NUM_PORTFOLIOS = 0;
    private final AccountDto TEST_ACCOUNT_DTO = AccountDto
            .builder()
            .userId(TEST_ID)
            .numPortfolios(TEST_NUM_PORTFOLIOS)
            .portfolios(null)
            .build();
    private final Account TEST_ACCOUNT = Account
            .builder()
            .userId(TEST_ID)
            .numPortfolios(TEST_NUM_PORTFOLIOS)
            .portfolios(null)
            .build();


    @Test
    public void givenAccountMapToDtoSucceeds() {
        AccountDto accountDto = AccountMapper.toAccountDto(TEST_ACCOUNT);
        assertEquals(accountDto, TEST_ACCOUNT_DTO);
    }

}
