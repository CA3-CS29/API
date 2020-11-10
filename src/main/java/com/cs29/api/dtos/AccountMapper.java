package com.cs29.api.dtos;

import com.cs29.api.models.Account;

public class AccountMapper {
    public static AccountDto toAccountDto(Account account) {
        return AccountDto.builder()
                .userId(account.getUserId())
                .numPortfolios(account.getNumPortfolios())
                .portfolios(account.getPortfolios())
                .build();
    }
}
