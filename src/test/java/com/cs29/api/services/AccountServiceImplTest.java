package com.cs29.api.services;

import com.cs29.api.dtos.AccountDto;
import com.cs29.api.dtos.PortfolioDto;
import com.cs29.api.dtos.PortfolioMapper;
import com.cs29.api.models.Account;
import com.cs29.api.models.AccountModelMapper;
import com.cs29.api.models.Portfolio;
import com.cs29.api.repositories.AccountRepository;
import com.cs29.api.repositories.PortfolioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceImplTest {
    private final String TEST_ID = "test_id";
    private final int TEST_NUM = 1;
    private final String TEST_DATE = "10/10/2020";
    private final String TEST_NAME = "TEST_NAME";
    private final Portfolio TEST_PORTFOLIO = Portfolio.builder()
            .portfolioId(TEST_ID)
            .userId(TEST_ID)
            .tag(TEST_NAME)
            .regions(null)
            .numRegions(TEST_NUM)
            .createdOn(TEST_DATE)
            .updatedOn(TEST_DATE)
            .build();
    private final Account TEST_ACCOUNT = Account.builder()
            .userId(TEST_ID)
            .numPortfolios(TEST_NUM)
            .portfolios(new ArrayList<>())
            .build();
    @Mock
    AccountRepository TEST_ACCOUNT_REPO;
    @Mock
    PortfolioRepository TEST_PORTFOLIO_REPO;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TEST_ACCOUNT.getPortfolios().add(TEST_PORTFOLIO);
    }

    @AfterEach
    public void teardown() {
        TEST_ACCOUNT.getPortfolios().clear();
    }

    @Test
    public void getAccountWithExistingIdRetrievesAccount() {
        AccountServiceImpl accountService = new AccountServiceImpl(TEST_ACCOUNT_REPO, TEST_PORTFOLIO_REPO);
        when(TEST_ACCOUNT_REPO.findDistinctByUserId(TEST_ID)).thenReturn(Optional.of(TEST_ACCOUNT));
        AccountDto accountDto = accountService.getAccount(TEST_ID);
        verify(TEST_ACCOUNT_REPO).findDistinctByUserId(TEST_ID);
        assertEquals(AccountModelMapper.toAccountModel(accountDto), TEST_ACCOUNT);
    }

    @Test
    public void getAccountWithNonExistingIdThrows() {
        AccountServiceImpl accountService = new AccountServiceImpl(TEST_ACCOUNT_REPO, TEST_PORTFOLIO_REPO);
        when(TEST_ACCOUNT_REPO.findDistinctByUserId(TEST_ID)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> accountService.getAccount(TEST_ID));
    }

    @Test
    public void createNewAccountSucceeds() {
        AccountServiceImpl accountService = new AccountServiceImpl(TEST_ACCOUNT_REPO, TEST_PORTFOLIO_REPO);
        when(TEST_ACCOUNT_REPO.findDistinctByUserId(TEST_ID)).thenReturn(Optional.empty());
        accountService.createAccount(TEST_ID);
        verify(TEST_ACCOUNT_REPO).findDistinctByUserId(TEST_ID);
    }

    @Test
    public void createWhenAccountExistsThrows() {
        AccountServiceImpl accountService = new AccountServiceImpl(TEST_ACCOUNT_REPO, TEST_PORTFOLIO_REPO);
        when(TEST_ACCOUNT_REPO.findDistinctByUserId(TEST_ID)).thenReturn(Optional.of(TEST_ACCOUNT));
        assertThrows(IllegalArgumentException.class, () -> accountService.createAccount(TEST_ID));
    }

    @Test
    public void addPortfolioSucceeds() {
        AccountServiceImpl accountService = new AccountServiceImpl(TEST_ACCOUNT_REPO, TEST_PORTFOLIO_REPO);
        when(TEST_ACCOUNT_REPO.findDistinctByUserId(TEST_ID)).thenReturn(Optional.of(TEST_ACCOUNT));
        PortfolioDto portfolioDto = PortfolioMapper.toPortfolioDto(TEST_PORTFOLIO);
        accountService.addPortfolio(TEST_ID, portfolioDto);
        verify(TEST_ACCOUNT_REPO).findDistinctByUserId(TEST_ID);
    }

    @Test
    public void addPortfolioWhenAccountDoesntExistThrows() {
        AccountServiceImpl accountService = new AccountServiceImpl(TEST_ACCOUNT_REPO, TEST_PORTFOLIO_REPO);
        when(TEST_ACCOUNT_REPO.findDistinctByUserId(TEST_ID)).thenReturn(Optional.empty());
        PortfolioDto portfolioDto = PortfolioMapper.toPortfolioDto(TEST_PORTFOLIO);
        assertThrows(NoSuchElementException.class, () -> accountService.addPortfolio(TEST_ID, portfolioDto));
    }

    @Test
    public void updatePortfolioWhenExistsSucceeds() {
        AccountServiceImpl accountService = new AccountServiceImpl(TEST_ACCOUNT_REPO, TEST_PORTFOLIO_REPO);
        when(TEST_ACCOUNT_REPO.findDistinctByUserId(TEST_ID)).thenReturn(Optional.of(TEST_ACCOUNT));
        when(TEST_PORTFOLIO_REPO.findDistinctByTagAndUserId(
                TEST_NAME, TEST_ID)).thenReturn(Optional.of(TEST_PORTFOLIO));
        PortfolioDto portfolioDto = PortfolioMapper.toPortfolioDto(TEST_PORTFOLIO);
        accountService.updatePortfolio(TEST_ID, portfolioDto);
        verify(TEST_ACCOUNT_REPO).findDistinctByUserId(TEST_ID);
        verify(TEST_PORTFOLIO_REPO).findDistinctByTagAndUserId(TEST_NAME, TEST_ID);
    }

    @Test
    public void updatePortfolioWhenAccountDoesNotExistThrows() {
        AccountServiceImpl accountService = new AccountServiceImpl(TEST_ACCOUNT_REPO, TEST_PORTFOLIO_REPO);
        when(TEST_ACCOUNT_REPO.findDistinctByUserId(TEST_ID)).thenReturn(Optional.empty());
        when(TEST_PORTFOLIO_REPO.findDistinctByTagAndUserId(
                TEST_NAME, TEST_ID)).thenReturn(Optional.of(TEST_PORTFOLIO));
        PortfolioDto portfolioDto = PortfolioMapper.toPortfolioDto(TEST_PORTFOLIO);
        assertThrows(NoSuchElementException.class, () -> accountService.updatePortfolio(TEST_ID, portfolioDto));
    }

    @Test
    public void updatePortfolioWhenPortfolioDoesNotExistThrows() {
        AccountServiceImpl accountService = new AccountServiceImpl(TEST_ACCOUNT_REPO, TEST_PORTFOLIO_REPO);
        when(TEST_ACCOUNT_REPO.findDistinctByUserId(TEST_ID)).thenReturn(Optional.of(TEST_ACCOUNT));
        when(TEST_PORTFOLIO_REPO.findDistinctByTagAndUserId(
                TEST_NAME, TEST_ID)).thenReturn(Optional.empty());
        PortfolioDto portfolioDto = PortfolioMapper.toPortfolioDto(TEST_PORTFOLIO);
        assertThrows(NoSuchElementException.class, () -> accountService.updatePortfolio(TEST_ID, portfolioDto));
    }

    @Test
    public void updatePortfolioWhenAccountAndPortfolioDoesNotExistThrows() {
        AccountServiceImpl accountService = new AccountServiceImpl(TEST_ACCOUNT_REPO, TEST_PORTFOLIO_REPO);
        when(TEST_ACCOUNT_REPO.findDistinctByUserId(TEST_ID)).thenReturn(Optional.empty());
        when(TEST_PORTFOLIO_REPO.findDistinctByTagAndUserId(
                TEST_NAME, TEST_ID)).thenReturn(Optional.empty());
        PortfolioDto portfolioDto = PortfolioMapper.toPortfolioDto(TEST_PORTFOLIO);
        assertThrows(NoSuchElementException.class, () -> accountService.updatePortfolio(TEST_ID, portfolioDto));
    }
}
