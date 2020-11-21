package com.cs29.api.models;

import com.cs29.api.dtos.AccountDto;

public class AccountModelMapper {
    public static Account toAccountModel(AccountDto accountDto) {
        return Account.builder()
                .userId(accountDto.getUserId())
                .numPortfolios(accountDto.getNumPortfolios())
                .portfolios(accountDto.getPortfolios())
                .build();
    }
}
