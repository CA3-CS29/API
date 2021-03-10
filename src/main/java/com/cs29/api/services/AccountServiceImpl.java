package com.cs29.api.services;

import com.cs29.api.ApiApplication;
import com.cs29.api.dtos.AccountDto;
import com.cs29.api.dtos.AccountMapper;
import com.cs29.api.dtos.PortfolioDto;
import com.cs29.api.models.Account;
import com.cs29.api.models.AccountModelMapper;
import com.cs29.api.models.Portfolio;
import com.cs29.api.models.PortfolioModelMapper;
import com.cs29.api.repositories.AccountRepository;
import com.cs29.api.repositories.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final PortfolioRepository portfolioRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, PortfolioRepository portfolioRepository) {
        this.accountRepository = accountRepository;
        this.portfolioRepository = portfolioRepository;
    }

    @Override
    @Cacheable(value = "accountCache", key = "#accountId")
    public AccountDto getAccount(String accountId) {
        Optional<Account> account = getAccountFromRepository(accountId);
        if (account.isEmpty()) {
            String errorMessage = String.format(
                    "AccountService could not retrieve account with account id: %s", accountId);
            ApiApplication.logger.error(errorMessage);
            throw new NoSuchElementException(errorMessage);
        }
        ApiApplication.logger.info("AccountService retrieved user with account id: " + accountId);
        return AccountMapper.toAccountDto(account.get());
    }

    @Override
    public void createAccount(String accountId) {
        Optional<Account> account = getAccountFromRepository(accountId);
        if (account.isPresent()) {
            String errorMessage = String.format(
                    "AccountService could not create account with account id: %s as it already exists", accountId);
            ApiApplication.logger.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
        Account newAccount = Account.builder()
                .userId(accountId)
                .numPortfolios(0)
                .portfolios(new ArrayList<>())
                .build();
        accountRepository.insert(newAccount);
        ApiApplication.logger.info("AccountService created new account with account id" + accountId);
    }

    @Override
    public void addPortfolio(String accountId, PortfolioDto portfolioDto) {
        Optional<Account> account = getAccountFromRepository(accountId);
        if (account.isEmpty()) {
            String errorMessage = String.format(
                    "AccountService could not retrieve account with account id: %s", accountId);
            ApiApplication.logger.error(errorMessage);
            throw new NoSuchElementException(errorMessage);
        }

        AccountDto accountDto = AccountMapper.toAccountDto(account.get());
        List<Portfolio> portfolios = accountDto.getPortfolios();
        if (portfolios == null) {
            portfolios = new ArrayList<>();
        }
        Portfolio portfolio = PortfolioModelMapper.toPortfolioModel(portfolioDto);
        portfolios.add(portfolio);
        accountDto.setPortfolios(portfolios);
        accountDto.setNumPortfolios(accountDto.getNumPortfolios() + 1);
        Account updatedAccount = AccountModelMapper.toAccountModel(accountDto);
        accountRepository.save(updatedAccount);
        ApiApplication.logger.info("AccountService added new portfolio to account with id: " + accountId);
    }

    @Override
    public void updatePortfolio(String accountId, PortfolioDto portfolioDto) {
        Optional<Account> accountOptional = getAccountFromRepository(accountId);
        Optional<Portfolio> portfolioOptional = getPortfolioFromRepository(accountId, portfolioDto.getTag());

        if (accountOptional.isEmpty() || portfolioOptional.isEmpty()) {
            String errorMessage = String.format(
                    "AccountService could not update portfolio with id: %s", portfolioDto.getPortfolioId());
            ApiApplication.logger.error(errorMessage);
            throw new NoSuchElementException(errorMessage);
        }

        Account account = accountOptional.get();
        Portfolio portfolio = portfolioOptional.get();
        List<Portfolio> portfolios = account.getPortfolios();
        portfolios.set(portfolios.indexOf(portfolio), PortfolioModelMapper.toPortfolioModel(portfolioDto));
        Account newAccount = Account.builder()
                .userId(accountId)
                .numPortfolios(account.getNumPortfolios())
                .portfolios(portfolios)
                .build();
        accountRepository.save(newAccount);
        portfolioRepository.save(PortfolioModelMapper.toPortfolioModel(portfolioDto));
    }

    @Override
    public void deleteAccount(String accountId) {
        Optional<Account> account = getAccountFromRepository(accountId);
        if (account.isEmpty()) {
            String errorMessage = String.format(
                    "AccountService could not retrieve account with account id: %s", accountId);
            ApiApplication.logger.error(errorMessage);
            throw new NoSuchElementException(errorMessage);
        }
        ApiApplication.logger.info("AccountService deleted user with account id: " + accountId);
        accountRepository.deleteById(accountId);
    }

    private Optional<Account> getAccountFromRepository(String accountId) {
        return accountRepository.findDistinctByUserId(accountId);
    }

    private Optional<Portfolio> getPortfolioFromRepository(String accountId, String tag) {
        return portfolioRepository.findDistinctByTagAndUserId(tag, accountId);
    }
}
