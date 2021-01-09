package com.cs29.api.services;

import com.cs29.api.dtos.OfficeDto;
import com.cs29.api.dtos.OfficeMapper;
import com.cs29.api.models.Account;
import com.cs29.api.models.Office;
import com.cs29.api.models.Portfolio;
import com.cs29.api.models.Region;
import com.cs29.api.repositories.AccountRepository;
import com.cs29.api.repositories.OfficeRepository;
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
public class OfficeServiceImplTest {
    private final String TEST_ID = "test_id";
    private final List<String> TEST_ID_LIST = List.of(TEST_ID);
    private final int TEST_NUM = 1;
    private final String TEST_DATE = "10/10/2020";
    private final String TEST_NAME = "TEST_NAME";
    private final int TEST_NUM_ENTRIES = 0;
    private final Office TEST_OFFICE = Office
            .builder()
            .officeId(TEST_ID)
            .regionId(TEST_ID)
            .userId(TEST_ID_LIST)
            .name(TEST_NAME)
            .numEntries(TEST_NUM_ENTRIES)
            .entries(null)
            .build();
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
            .offices(new ArrayList<>())
            .build();
    @Mock
    AccountRepository TEST_ACCOUNT_REPO;
    @Mock
    PortfolioRepository TEST_PORTFOLIO_REPO;
    @Mock
    RegionRepository TEST_REGION_REPO;
    @Mock
    OfficeRepository TEST_OFFICE_REPO;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TEST_PORTFOLIO.getUserId().add(TEST_ID);
        TEST_ACCOUNT.getPortfolios().add(TEST_PORTFOLIO);
        TEST_PORTFOLIO.getRegions().add(TEST_REGION);
        TEST_REGION.getOffices().add(TEST_OFFICE);
    }

    @AfterEach
    public void teardown() {
        TEST_PORTFOLIO.getUserId().clear();
        TEST_ACCOUNT.getPortfolios().clear();
        TEST_REGION.getOffices().clear();
    }

    @Test
    public void getOfficeSucceedsWhenExists() {
        OfficeService officeService = new OfficeServiceImpl(
                TEST_REGION_REPO, TEST_ACCOUNT_REPO, TEST_PORTFOLIO_REPO, TEST_OFFICE_REPO);
        when(TEST_OFFICE_REPO.findDistinctByNameAndUserId(
                TEST_NAME, TEST_ID)).thenReturn(Optional.of(TEST_OFFICE));
        OfficeDto officeDto = officeService.getOffice(TEST_NAME, TEST_ID);
        verify(TEST_OFFICE_REPO).findDistinctByNameAndUserId(TEST_NAME, TEST_ID);
        assertEquals(officeDto, OfficeMapper.toOfficeDto(TEST_OFFICE));
    }

    @Test
    public void getOfficeThrowsWhenOfficeDoesNotExist() {
        OfficeService officeService = new OfficeServiceImpl(
                TEST_REGION_REPO, TEST_ACCOUNT_REPO, TEST_PORTFOLIO_REPO, TEST_OFFICE_REPO);
        when(TEST_OFFICE_REPO.findDistinctByNameAndUserId(
                TEST_NAME, TEST_ID)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> officeService.getOffice(TEST_NAME, TEST_ID));
    }

    @Test
    public void getAllUsersOfficesSucceedsWhenExists() {
        OfficeService officeService = new OfficeServiceImpl(
                TEST_REGION_REPO, TEST_ACCOUNT_REPO, TEST_PORTFOLIO_REPO, TEST_OFFICE_REPO);
        when(TEST_OFFICE_REPO.findAllByUserId(
                TEST_ID)).thenReturn(Optional.of(List.of(TEST_OFFICE)));
        List<OfficeDto> officeDtoList = officeService.getAllUsersOffices(TEST_ID);
        verify(TEST_OFFICE_REPO).findAllByUserId(TEST_ID);
        assertEquals(officeDtoList, List.of(OfficeMapper.toOfficeDto(TEST_OFFICE)));
    }

    @Test
    public void getAllUsersThrowsWhenDoesNotExist() {
        OfficeService officeService = new OfficeServiceImpl(
                TEST_REGION_REPO, TEST_ACCOUNT_REPO, TEST_PORTFOLIO_REPO, TEST_OFFICE_REPO);
        when(TEST_OFFICE_REPO.findAllByUserId(
                TEST_ID)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> officeService.getAllUsersOffices(TEST_ID));

    }

    @Test
    public void createOfficeSucceedsWhenAccountPortfolioRegionExist() {
        OfficeService officeService = new OfficeServiceImpl(
                TEST_REGION_REPO, TEST_ACCOUNT_REPO, TEST_PORTFOLIO_REPO, TEST_OFFICE_REPO);
        when(TEST_REGION_REPO.findDistinctByNameAndUserIdAndPortfolioId(
                TEST_NAME, TEST_ID, TEST_ID)).thenReturn(Optional.of(TEST_REGION));
        when(TEST_PORTFOLIO_REPO.findDistinctByTagAndUserId(
                TEST_NAME, TEST_ID)).thenReturn(Optional.of(TEST_PORTFOLIO));
        when(TEST_ACCOUNT_REPO.findDistinctByUserId(TEST_ID)).thenReturn(Optional.of(TEST_ACCOUNT));
        when(TEST_OFFICE_REPO.findAllByUserId(
                TEST_ID)).thenReturn(Optional.of(List.of(TEST_OFFICE)));
        officeService.createOffice(TEST_ID, TEST_ID, TEST_ID, OfficeMapper.toOfficeDto(TEST_OFFICE));
        verify(TEST_REGION_REPO).findDistinctByNameAndUserIdAndPortfolioId(TEST_NAME, TEST_ID, TEST_ID);
        verify(TEST_PORTFOLIO_REPO).findDistinctByTagAndUserId(TEST_NAME, TEST_ID);
        verify(TEST_ACCOUNT_REPO).findDistinctByUserId(TEST_ID);
        verify(TEST_OFFICE_REPO).save(TEST_OFFICE);
    }

    @Test
    public void createOfficeThrowsWhenAccountDoesNotExist() {
        OfficeService officeService = new OfficeServiceImpl(
                TEST_REGION_REPO, TEST_ACCOUNT_REPO, TEST_PORTFOLIO_REPO, TEST_OFFICE_REPO);
        when(TEST_REGION_REPO.findDistinctByNameAndUserIdAndPortfolioId(
                TEST_NAME, TEST_ID, TEST_ID)).thenReturn(Optional.of(TEST_REGION));
        when(TEST_PORTFOLIO_REPO.findDistinctByTagAndUserId(
                TEST_NAME, TEST_ID)).thenReturn(Optional.of(TEST_PORTFOLIO));
        when(TEST_ACCOUNT_REPO.findDistinctByUserId(TEST_ID)).thenReturn(Optional.empty());
        when(TEST_OFFICE_REPO.findAllByUserId(
                TEST_ID)).thenReturn(Optional.of(List.of(TEST_OFFICE)));
        assertThrows(NoSuchElementException.class, () ->
                officeService.createOffice(TEST_ID, TEST_ID, TEST_ID, OfficeMapper.toOfficeDto(TEST_OFFICE)));

    }

    @Test
    public void createOfficeThrowsWhenPortfolioDoesNotExist() {
        OfficeService officeService = new OfficeServiceImpl(
                TEST_REGION_REPO, TEST_ACCOUNT_REPO, TEST_PORTFOLIO_REPO, TEST_OFFICE_REPO);
        when(TEST_REGION_REPO.findDistinctByNameAndUserIdAndPortfolioId(
                TEST_NAME, TEST_ID, TEST_ID)).thenReturn(Optional.of(TEST_REGION));
        when(TEST_PORTFOLIO_REPO.findDistinctByTagAndUserId(
                TEST_NAME, TEST_ID)).thenReturn(Optional.empty());
        when(TEST_ACCOUNT_REPO.findDistinctByUserId(TEST_ID)).thenReturn(Optional.of(TEST_ACCOUNT));
        when(TEST_OFFICE_REPO.findAllByUserId(
                TEST_ID)).thenReturn(Optional.of(List.of(TEST_OFFICE)));
        assertThrows(NoSuchElementException.class, () ->
                officeService.createOffice(TEST_ID, TEST_ID, TEST_ID, OfficeMapper.toOfficeDto(TEST_OFFICE)));
    }

    @Test
    public void createOfficeThrowsWhenRegionDoesNotExist() {
        OfficeService officeService = new OfficeServiceImpl(
                TEST_REGION_REPO, TEST_ACCOUNT_REPO, TEST_PORTFOLIO_REPO, TEST_OFFICE_REPO);
        when(TEST_REGION_REPO.findDistinctByNameAndUserIdAndPortfolioId(
                TEST_NAME, TEST_ID, TEST_ID)).thenReturn(Optional.empty());
        when(TEST_PORTFOLIO_REPO.findDistinctByTagAndUserId(
                TEST_NAME, TEST_ID)).thenReturn(Optional.of(TEST_PORTFOLIO));
        when(TEST_ACCOUNT_REPO.findDistinctByUserId(TEST_ID)).thenReturn(Optional.of(TEST_ACCOUNT));
        when(TEST_OFFICE_REPO.findAllByUserId(
                TEST_ID)).thenReturn(Optional.of(List.of(TEST_OFFICE)));
        assertThrows(NoSuchElementException.class, () ->
                officeService.createOffice(TEST_ID, TEST_ID, TEST_ID, OfficeMapper.toOfficeDto(TEST_OFFICE)));
    }
}
