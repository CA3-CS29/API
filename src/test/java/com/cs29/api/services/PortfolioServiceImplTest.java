package com.cs29.api.services;

import com.cs29.api.dtos.PortfolioDto;
import com.cs29.api.dtos.PortfolioMapper;
import com.cs29.api.models.Account;
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
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PortfolioServiceImplTest {

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
    public void getPortfolioWithExistingPortfolioSucceeds() {
        PortfolioServiceImpl portfolioService = new PortfolioServiceImpl(TEST_ACCOUNT_REPO, TEST_PORTFOLIO_REPO);
        when(TEST_PORTFOLIO_REPO.findDistinctByTagAndUserId(
                TEST_NAME, TEST_ID)).thenReturn(Optional.of(TEST_PORTFOLIO));
        PortfolioDto portfolioDto = portfolioService.getPortfolio(TEST_NAME, TEST_ID);
        assertEquals(portfolioDto, PortfolioMapper.toPortfolioDto(TEST_PORTFOLIO));
        verify(TEST_PORTFOLIO_REPO).findDistinctByTagAndUserId(TEST_NAME, TEST_ID);
    }

    @Test
    public void getPortfolioWithNonExistingPortfolioFails() {
        PortfolioServiceImpl portfolioService = new PortfolioServiceImpl(TEST_ACCOUNT_REPO, TEST_PORTFOLIO_REPO);
        when(TEST_PORTFOLIO_REPO.findDistinctByTagAndUserId(
                TEST_NAME, TEST_ID)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> portfolioService.getPortfolio(TEST_NAME, TEST_ID));
    }

    @Test
    public void createNewPortfolioSucceeds() {
        PortfolioServiceImpl portfolioService = new PortfolioServiceImpl(TEST_ACCOUNT_REPO, TEST_PORTFOLIO_REPO);
        when(TEST_ACCOUNT_REPO.findDistinctByUserId(TEST_ID)).thenReturn(Optional.of(TEST_ACCOUNT));
        when(TEST_PORTFOLIO_REPO.findDistinctByTagAndUserId(
                TEST_NAME, TEST_ID)).thenReturn(Optional.empty());
        portfolioService.createPortfolio(TEST_ID, PortfolioMapper.toPortfolioDto(TEST_PORTFOLIO));
        verify(TEST_PORTFOLIO_REPO).findDistinctByTagAndUserId(TEST_NAME, TEST_ID);
        verify(TEST_PORTFOLIO_REPO).save(TEST_PORTFOLIO);
    }

    @Test
    public void createPortfolioWhenAlreadyExistsThrows() {
        PortfolioServiceImpl portfolioService = new PortfolioServiceImpl(TEST_ACCOUNT_REPO, TEST_PORTFOLIO_REPO);
        when(TEST_ACCOUNT_REPO.findDistinctByUserId(TEST_ID)).thenReturn(Optional.of(TEST_ACCOUNT));
        when(TEST_PORTFOLIO_REPO.findDistinctByTagAndUserId(
                TEST_NAME, TEST_ID)).thenReturn(Optional.of(TEST_PORTFOLIO));
        assertThrows(IllegalArgumentException.class, () -> portfolioService.createPortfolio(
                TEST_ID, PortfolioMapper.toPortfolioDto(TEST_PORTFOLIO)));
    }

    @Test
    public void createPortfolioWhenAccountNotFoundThrows() {
        PortfolioServiceImpl portfolioService = new PortfolioServiceImpl(TEST_ACCOUNT_REPO, TEST_PORTFOLIO_REPO);
        when(TEST_ACCOUNT_REPO.findDistinctByUserId(TEST_ID)).thenReturn(Optional.empty());
        when(TEST_PORTFOLIO_REPO.findDistinctByTagAndUserId(
                TEST_NAME, TEST_ID)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> portfolioService.createPortfolio(
                TEST_ID, PortfolioMapper.toPortfolioDto(TEST_PORTFOLIO)));
    }

    @Test
    public void updatePortfolioWhenExistsSucceeds() {
        PortfolioServiceImpl portfolioService = new PortfolioServiceImpl(TEST_ACCOUNT_REPO, TEST_PORTFOLIO_REPO);
        when(TEST_ACCOUNT_REPO.findDistinctByUserId(TEST_ID)).thenReturn(Optional.of(TEST_ACCOUNT));
        when(TEST_PORTFOLIO_REPO.findDistinctByTagAndUserId(
                TEST_NAME, TEST_ID)).thenReturn(Optional.of(TEST_PORTFOLIO));
        PortfolioDto portfolioDto = PortfolioMapper.toPortfolioDto(TEST_PORTFOLIO);
        portfolioService.updatePortfolio(TEST_ID, portfolioDto);
        Account account = TEST_ACCOUNT;
        account.getPortfolios().add(TEST_PORTFOLIO);
        verify(TEST_ACCOUNT_REPO).save(account);
        verify(TEST_PORTFOLIO_REPO).save(TEST_PORTFOLIO);
    }

    @Test
    public void updatePortfolioWhenAccountDoesNotExistThrows() {
        PortfolioServiceImpl portfolioService = new PortfolioServiceImpl(TEST_ACCOUNT_REPO, TEST_PORTFOLIO_REPO);
        when(TEST_ACCOUNT_REPO.findDistinctByUserId(TEST_ID)).thenReturn(Optional.empty());
        when(TEST_PORTFOLIO_REPO.findDistinctByTagAndUserId(
                TEST_NAME, TEST_ID)).thenReturn(Optional.of(TEST_PORTFOLIO));
        PortfolioDto portfolioDto = PortfolioMapper.toPortfolioDto(TEST_PORTFOLIO);
        assertThrows(NoSuchElementException.class, () -> portfolioService.updatePortfolio(TEST_ID, portfolioDto));
    }

    @Test
    public void updatePortfolioWhenPortfolioDoesNotExistsAddsNewPortfolio() {
        PortfolioServiceImpl portfolioService = new PortfolioServiceImpl(TEST_ACCOUNT_REPO, TEST_PORTFOLIO_REPO);
        when(TEST_ACCOUNT_REPO.findDistinctByUserId(TEST_ID)).thenReturn(Optional.of(TEST_ACCOUNT));
        when(TEST_PORTFOLIO_REPO.findDistinctByTagAndUserId(
                TEST_NAME, TEST_ID)).thenReturn(Optional.empty());
        PortfolioDto portfolioDto = PortfolioMapper.toPortfolioDto(TEST_PORTFOLIO);
        portfolioService.updatePortfolio(TEST_ID, portfolioDto);
        Account account = TEST_ACCOUNT;
        account.getPortfolios().add(TEST_PORTFOLIO);
        verify(TEST_ACCOUNT_REPO).save(account);
        verify(TEST_PORTFOLIO_REPO).save(TEST_PORTFOLIO);
    }

    @Test
    public void getAllByTagWhenExistsSucceeds() {
        PortfolioServiceImpl portfolioService = new PortfolioServiceImpl(TEST_ACCOUNT_REPO, TEST_PORTFOLIO_REPO);
        when(TEST_PORTFOLIO_REPO.findAllByTag(TEST_NAME)).thenReturn(Optional.of(List.of(TEST_PORTFOLIO)));
        List<PortfolioDto> portfolios = portfolioService.getAllByTag(TEST_NAME);
        List<PortfolioDto> testPortfolios = List.of(PortfolioMapper.toPortfolioDto(TEST_PORTFOLIO));
        verify(TEST_PORTFOLIO_REPO).findAllByTag(TEST_NAME);
        assertEquals(portfolios, testPortfolios);
    }

    @Test
    public void getAllByTagWhenDoesNotExistThrows() {
        PortfolioServiceImpl portfolioService = new PortfolioServiceImpl(TEST_ACCOUNT_REPO, TEST_PORTFOLIO_REPO);
        when(TEST_PORTFOLIO_REPO.findAllByTag(TEST_NAME)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> portfolioService.getAllByTag(TEST_NAME));
    }

    @Test
    public void getAllByUserIdWhenExistsSucceeds() {
        PortfolioServiceImpl portfolioService = new PortfolioServiceImpl(TEST_ACCOUNT_REPO, TEST_PORTFOLIO_REPO);
        when(TEST_PORTFOLIO_REPO.findAllByUserId(TEST_ID)).thenReturn(Optional.of(List.of(TEST_PORTFOLIO)));
        List<PortfolioDto> portfolios = portfolioService.getAllByUserId(TEST_ID);
        List<PortfolioDto> testPortfolios = List.of(PortfolioMapper.toPortfolioDto(TEST_PORTFOLIO));
        verify(TEST_PORTFOLIO_REPO).findAllByUserId(TEST_ID);
        assertEquals(portfolios, testPortfolios);
    }

    @Test
    public void getAllByUserIdWhenDoesNotExistThrows() {
        PortfolioServiceImpl portfolioService = new PortfolioServiceImpl(TEST_ACCOUNT_REPO, TEST_PORTFOLIO_REPO);
        when(TEST_PORTFOLIO_REPO.findAllByUserId(TEST_ID)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> portfolioService.getAllByUserId(TEST_ID));
    }
}
