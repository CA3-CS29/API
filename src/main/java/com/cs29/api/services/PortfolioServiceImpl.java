package com.cs29.api.services;

import com.cs29.api.ApiApplication;
import com.cs29.api.dtos.PortfolioDto;
import com.cs29.api.dtos.PortfolioMapper;
import com.cs29.api.models.Account;
import com.cs29.api.models.Portfolio;
import com.cs29.api.models.PortfolioModelMapper;
import com.cs29.api.repositories.AccountRepository;
import com.cs29.api.repositories.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PortfolioServiceImpl implements PortfolioService {
    private final AccountRepository accountRepository;
    private final PortfolioRepository portfolioRepository;

    @Autowired
    public PortfolioServiceImpl(AccountRepository accountRepository, PortfolioRepository portfolioRepository) {
        this.accountRepository = accountRepository;
        this.portfolioRepository = portfolioRepository;
    }

    @Override
    public PortfolioDto getPortfolio(String tag, String userId) {
        Optional<Portfolio> portfolio = getPortfolioFromRepository(userId, tag);
        if (portfolio.isEmpty()) {
            String errorMessage = String.format(
                    "PortfolioService could not get portfolio %s", tag);
            ApiApplication.logger.error(errorMessage);
            throw new NoSuchElementException(errorMessage);
        }
        ApiApplication.logger.info("PortfolioService retrieved portfolio: " + tag);
        return PortfolioMapper.toPortfolioDto(portfolio.get());
    }

    @Override
    public void createPortfolio(String userId, PortfolioDto portfolioDto) {
        Optional<Portfolio> portfolio = getPortfolioFromRepository(userId, portfolioDto.getTag());
        Optional<Account> account = getAccountFromRepository(userId);
        if (portfolio.isPresent()) {
            String errorMessage = String.format(
                    "PortfolioService could not create portfolio with tag: %s as it already exists",
                    portfolioDto.getTag());
            ApiApplication.logger.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
        if (account.isEmpty()) {
            String errorMessage = String.format(
                    "PortfolioService could not create portfolio for account id: %s as account does not exist", userId);
            ApiApplication.logger.error(errorMessage);
            throw new NoSuchElementException(errorMessage);
        }

        Portfolio newPortfolio = PortfolioModelMapper.toPortfolioModel(portfolioDto);
        portfolioRepository.save(newPortfolio);
        Account associatedAccount = account.get();
        associatedAccount.getPortfolios().add(newPortfolio);
        associatedAccount.setNumPortfolios(associatedAccount.getNumPortfolios() + 1);
        accountRepository.save(associatedAccount);
        ApiApplication.logger.info(
                "PortfolioService created new portfolio for account " + userId + "with tag" + portfolioDto.getTag());
    }

    @Override
    public void updatePortfolio(String userId, PortfolioDto portfolioDto) {
        Optional<Account> account = getAccountFromRepository(userId);
        if (account.isEmpty()) {
            String errorMessage = String.format(
                    "PortfolioService could not create portfolio with account id: %s as it does not exist", userId);
            ApiApplication.logger.error(errorMessage);
            throw new NoSuchElementException(errorMessage);
        }
        portfolioRepository.save(PortfolioModelMapper.toPortfolioModel(portfolioDto));
        Account associatedAccount = account.get();
        associatedAccount.getPortfolios().add(PortfolioModelMapper.toPortfolioModel(portfolioDto));
        accountRepository.save(associatedAccount);
        ApiApplication.logger.info(
                "PortfolioService updated portfolio for account " + userId + "with tag" + portfolioDto.getTag());
    }

    @Override
    public List<PortfolioDto> getAllByTag(String tag) {
        Optional<List<Portfolio>> portfolios = getAllPortfoliosForTag(tag);
        if (portfolios.isEmpty()) {
            String errorMessage = String.format(
                    "PortfolioService could not find portfolios for tag: %s", tag);
            ApiApplication.logger.error(errorMessage);
            throw new NoSuchElementException(errorMessage);
        }
        List<PortfolioDto> dtos = new ArrayList<>();
        for (Portfolio portfolio : portfolios.get()) {
            dtos.add(PortfolioMapper.toPortfolioDto(portfolio));
        }
        ApiApplication.logger.info(
                "PortfolioService retrieved all portfolios for tag " + tag);
        return dtos;
    }


    @Override
    public List<PortfolioDto> getAllByUserId(String userId) {
        Optional<List<Portfolio>> portfolios = getAllPortfoliosForAccount(userId);
        if (portfolios.isEmpty()) {
            String errorMessage = String.format(
                    "PortfolioService could not find portfolios for account id: %s", userId);
            ApiApplication.logger.error(errorMessage);
            throw new NoSuchElementException(errorMessage);
        }
        List<PortfolioDto> dtos = new ArrayList<>();
        for (Portfolio portfolio : portfolios.get()) {
            dtos.add(PortfolioMapper.toPortfolioDto(portfolio));
        }
        ApiApplication.logger.info(
                "PortfolioService retrieved all portfolios for user with id " + userId);
        return dtos;
    }

    @Override
    public void deleteById(String portfolioId, String userId) {
        Optional<Account> account = getAccountFromRepository(userId);
        if (account.isEmpty()) {
            String errorMessage = String.format(
                    "PortfolioService could not retrieve account with account id: %s", userId);
            ApiApplication.logger.error(errorMessage);
            throw new NoSuchElementException(errorMessage);
        }
        var memPortIndex = new HashMap<String, Integer>();
        for (int i = 0; i < account.get().getPortfolios().size(); i++) {
            memPortIndex.put(account.get().getPortfolios().get(i).getPortfolioId(), i);
        }
        int portIndex = memPortIndex.get(portfolioId);
        account.get().getPortfolios().remove(portIndex);
        ApiApplication.logger.info("PortfolioService deleted portfolio with portfolio id: " + portfolioId);
        accountRepository.save(account.get());
        portfolioRepository.deleteById(portfolioId);
    }

    private Optional<Account> getAccountFromRepository(String accountId) {
        return accountRepository.findDistinctByUserId(accountId);
    }

    private Optional<List<Portfolio>> getAllPortfoliosForAccount(String accountId) {
        return portfolioRepository.findAllByUserId(accountId);
    }

    private Optional<List<Portfolio>> getAllPortfoliosForTag(String tag) {
        return portfolioRepository.findAllByTag(tag);
    }

    private Optional<Portfolio> getPortfolioFromRepository(String accountId, String tag) {
        return portfolioRepository.findDistinctByTagAndUserId(tag, accountId);
    }
}
