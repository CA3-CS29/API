package com.cs29.api.models;

import com.cs29.api.dtos.AccountDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountModelMapperTest {
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
    public void givenAccountDtoMapToModelSucceeds() {
        Account account = AccountModelMapper.toAccountModel(TEST_ACCOUNT_DTO);
        assertEquals(account, TEST_ACCOUNT);
    }
}
