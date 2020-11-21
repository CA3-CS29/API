package com.cs29.api.services;

import com.cs29.api.dtos.RegionDto;
import com.cs29.api.dtos.RegionMapper;
import com.cs29.api.models.Account;
import com.cs29.api.models.Portfolio;
import com.cs29.api.models.Region;
import com.cs29.api.repositories.AccountRepository;
import com.cs29.api.repositories.PortfolioRepository;
import com.cs29.api.repositories.RegionRepository;
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
public class RegionServiceImplTest {
    private final String TEST_ID = "test_id";
    private final List<String> TEST_ID_LIST = List.of(TEST_ID);
    private final int TEST_NUM = 1;
    private final String TEST_DATE = "10/10/2020";
    private final String TEST_NAME = "TEST_NAME";
    private final Portfolio TEST_PORTFOLIO = Portfolio.builder()
            .portfolioId(TEST_ID)
            .userId(new ArrayList<>())
            .tag(TEST_NAME)
            .regions(new ArrayList<>())
            .numRegions(TEST_NUM)
            .createdOn(TEST_DATE)
            .updatedOn(TEST_DATE)
            .build();
    private final Account TEST_ACCOUNT = Account.builder()
            .userId(TEST_ID)
            .numPortfolios(TEST_NUM)
            .portfolios(new ArrayList<>())
            .build();
    private final Region TEST_REGION = Region
            .builder()
            .regionId(TEST_ID)
            .portfolioId(TEST_ID)
            .userId(TEST_ID_LIST)
            .numOffices(TEST_NUM)
            .name(TEST_NAME)
            .offices(null)
            .build();
    @Mock
    AccountRepository TEST_ACCOUNT_REPO;
    @Mock
    PortfolioRepository TEST_PORTFOLIO_REPO;
    @Mock
    RegionRepository TEST_REGION_REPO;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TEST_PORTFOLIO.getUserId().add(TEST_ID);
        TEST_ACCOUNT.getPortfolios().add(TEST_PORTFOLIO);
        TEST_PORTFOLIO.getRegions().add(TEST_REGION);
    }

    @AfterEach
    public void teardown() {
        TEST_PORTFOLIO.getUserId().clear();
        TEST_ACCOUNT.getPortfolios().clear();
    }

    @Test
    public void getRegionSucceedsWhenRegionExists() {
        RegionService regionService = new RegionServiceImpl(TEST_REGION_REPO, TEST_ACCOUNT_REPO, TEST_PORTFOLIO_REPO);
        when(TEST_REGION_REPO.findDistinctByNameAndUserId(
                TEST_NAME, TEST_ID)).thenReturn(Optional.of(TEST_REGION));
        RegionDto regionDto = regionService.getRegion(TEST_NAME, TEST_ID);
        verify(TEST_REGION_REPO).findDistinctByNameAndUserId(TEST_NAME, TEST_ID);
        assertEquals(regionDto, RegionMapper.toRegionDto(TEST_REGION));
    }

    @Test
    public void getRegionThrowsWhenRegionDoesNotExist() {
        RegionService regionService = new RegionServiceImpl(TEST_REGION_REPO, TEST_ACCOUNT_REPO, TEST_PORTFOLIO_REPO);
        when(TEST_REGION_REPO.findDistinctByNameAndUserId(
                TEST_NAME, TEST_ID)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> regionService.getRegion(TEST_NAME, TEST_ID));
    }

    @Test
    public void getAllRegionsForUserSucceedsWhenExists() {
        RegionService regionService = new RegionServiceImpl(TEST_REGION_REPO, TEST_ACCOUNT_REPO, TEST_PORTFOLIO_REPO);
        when(TEST_REGION_REPO.findAllByUserId(
                TEST_ID)).thenReturn(Optional.of(List.of(TEST_REGION)));
        List<RegionDto> regionDtos = regionService.getAllRegionsForUser(TEST_ID);
        verify(TEST_REGION_REPO).findAllByUserId(TEST_ID);
        assertEquals(List.of(RegionMapper.toRegionDto(TEST_REGION)), regionDtos);
    }

    @Test
    public void getAllRegionsThrowsWhenDoesNotExist() {
        RegionService regionService = new RegionServiceImpl(TEST_REGION_REPO, TEST_ACCOUNT_REPO, TEST_PORTFOLIO_REPO);
        when(TEST_REGION_REPO.findAllByUserId(
                TEST_ID)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> regionService.getAllRegionsForUser(TEST_ID));
    }

    @Test
    public void createRegionSucceedsWhenAccountPortfolioExist() {
        RegionService regionService = new RegionServiceImpl(TEST_REGION_REPO, TEST_ACCOUNT_REPO, TEST_PORTFOLIO_REPO);
        when(TEST_REGION_REPO.findDistinctByNameAndUserId(
                TEST_NAME, TEST_ID)).thenReturn(Optional.empty());
        when(TEST_PORTFOLIO_REPO.findDistinctByTagAndUserId(
                TEST_NAME, TEST_ID)).thenReturn(Optional.of(TEST_PORTFOLIO));
        when(TEST_ACCOUNT_REPO.findDistinctByUserId(TEST_ID)).thenReturn(Optional.of(TEST_ACCOUNT));

        regionService.createRegion(TEST_ID, TEST_ID, RegionMapper.toRegionDto(TEST_REGION), TEST_ID);
        verify(TEST_REGION_REPO).findDistinctByNameAndUserId(TEST_NAME, TEST_ID);
        verify(TEST_PORTFOLIO_REPO).findDistinctByTagAndUserId(TEST_NAME, TEST_ID);
        verify(TEST_ACCOUNT_REPO).findDistinctByUserId(TEST_ID);
        verify(TEST_REGION_REPO).save(TEST_REGION);
    }

    @Test
    public void createRegionThrowsWhenAccountDoesNotExist() {
        RegionService regionService = new RegionServiceImpl(TEST_REGION_REPO, TEST_ACCOUNT_REPO, TEST_PORTFOLIO_REPO);
        when(TEST_REGION_REPO.findDistinctByNameAndUserId(
                TEST_NAME, TEST_ID)).thenReturn(Optional.empty());
        when(TEST_PORTFOLIO_REPO.findDistinctByTagAndUserId(
                TEST_NAME, TEST_ID)).thenReturn(Optional.of(TEST_PORTFOLIO));
        when(TEST_ACCOUNT_REPO.findDistinctByUserId(TEST_ID)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () ->
                regionService.createRegion(TEST_ID, TEST_ID, RegionMapper.toRegionDto(TEST_REGION), TEST_ID));

    }

    @Test
    public void createRegionThrowsWhenPortfolioDoesNotExist() {
        RegionService regionService = new RegionServiceImpl(TEST_REGION_REPO, TEST_ACCOUNT_REPO, TEST_PORTFOLIO_REPO);
        when(TEST_REGION_REPO.findDistinctByNameAndUserId(
                TEST_NAME, TEST_ID)).thenReturn(Optional.empty());
        when(TEST_PORTFOLIO_REPO.findDistinctByTagAndUserId(
                TEST_NAME, TEST_ID)).thenReturn(Optional.empty());
        when(TEST_ACCOUNT_REPO.findDistinctByUserId(TEST_ID)).thenReturn(Optional.of(TEST_ACCOUNT));

        assertThrows(NoSuchElementException.class, () ->
                regionService.createRegion(TEST_ID, TEST_ID, RegionMapper.toRegionDto(TEST_REGION), TEST_ID));
    }

    @Test
    public void createRegionThrowsWhenPortfolioAlreadyExists() {
        RegionService regionService = new RegionServiceImpl(TEST_REGION_REPO, TEST_ACCOUNT_REPO, TEST_PORTFOLIO_REPO);
        when(TEST_REGION_REPO.findDistinctByNameAndUserId(
                TEST_NAME, TEST_ID)).thenReturn(Optional.of(TEST_REGION));
        when(TEST_PORTFOLIO_REPO.findDistinctByTagAndUserId(
                TEST_NAME, TEST_ID)).thenReturn(Optional.of(TEST_PORTFOLIO));
        when(TEST_ACCOUNT_REPO.findDistinctByUserId(TEST_ID)).thenReturn(Optional.of(TEST_ACCOUNT));
        assertThrows(IllegalArgumentException.class, () ->
                regionService.createRegion(TEST_ID, TEST_ID, RegionMapper.toRegionDto(TEST_REGION), TEST_ID));
    }
}
