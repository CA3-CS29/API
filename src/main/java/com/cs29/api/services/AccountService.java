package com.cs29.api.services;

import com.cs29.api.dtos.AccountDto;
import com.cs29.api.dtos.PortfolioDto;

public interface AccountService {
    AccountDto getAccount(String accountId);

    void createAccount(String accountId);

    void addPortfolio(String accountId, PortfolioDto portfolioDto);

    void updatePortfolio(String accountId, PortfolioDto portfolioDto);
}
