package com.cs29.api.services;

import com.cs29.api.dtos.EntryDto;
import com.cs29.api.dtos.EntryMapper;
import com.cs29.api.models.Account;
import com.cs29.api.models.Entry;
import com.cs29.api.models.Office;
import com.cs29.api.models.Portfolio;
import com.cs29.api.models.Region;
import com.cs29.api.repositories.AccountRepository;
import com.cs29.api.repositories.EntryRepository;
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
public class EntryServiceImplTest {
    private final String TEST_ID = "test_id";
    private final List<String> TEST_ID_LIST = List.of(TEST_ID);
    private final int TEST_NUM = 1;
    private final String TEST_DATE = "10/10/2020";
    private final String TEST_NAME = "TEST_NAME";
    private final int TEST_NUM_ENTRIES = 0;
    private final double TEST_DOUBLE = 10.5;
    private final String TEST_SOURCE = "test_source";
    private final String TEST_UNITS = "test_units";
    private final String TEST_LEVEL = "test_level";
    private final String TEST_FURTHER_INFO = "test_further_info";
    private final Entry TEST_ENTRY = Entry.builder()
            .entryId(TEST_ID)
            .officeId(TEST_ID)
            .tag(TEST_NAME)
            .consumption(TEST_DOUBLE)
            .original(TEST_DOUBLE)
            .converted(TEST_DOUBLE)
            .source(TEST_SOURCE)
            .units(TEST_UNITS)
            .level1(TEST_LEVEL)
            .level2(TEST_LEVEL)
            .level3(TEST_LEVEL)
            .level4(TEST_LEVEL)
            .furtherInfo(TEST_FURTHER_INFO)
            .components(null)
            .build();
    private final Office TEST_OFFICE = Office
            .builder()
            .officeId(TEST_ID)
            .regionId(TEST_ID)
            .userId(TEST_ID_LIST)
            .name(TEST_NAME)
            .numEntries(TEST_NUM_ENTRIES)
            .entries(new ArrayList<>())
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
    @Mock
    EntryRepository TEST_ENTRY_REPO;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TEST_PORTFOLIO.getUserId().add(TEST_ID);
        TEST_ACCOUNT.getPortfolios().add(TEST_PORTFOLIO);
        TEST_PORTFOLIO.getRegions().add(TEST_REGION);
        TEST_REGION.getOffices().add(TEST_OFFICE);
        TEST_OFFICE.getEntries().add(TEST_ENTRY);
    }

    @AfterEach
    public void teardown() {
        TEST_PORTFOLIO.getUserId().clear();
        TEST_ACCOUNT.getPortfolios().clear();
        TEST_REGION.getOffices().clear();
        TEST_OFFICE.getEntries().clear();
    }

    @Test
    public void getEntrySucceedsWhenExists() {
        EntryService entryService = new EntryServiceImpl(TEST_REGION_REPO,
                TEST_ACCOUNT_REPO,
                TEST_PORTFOLIO_REPO,
                TEST_OFFICE_REPO,
                TEST_ENTRY_REPO);
        when(TEST_ENTRY_REPO.findByTagAndOfficeId(
                TEST_NAME, TEST_ID)).thenReturn(Optional.of(TEST_ENTRY));
        EntryDto entryDto = entryService.getEntry(TEST_NAME, TEST_ID);
        verify(TEST_ENTRY_REPO).findByTagAndOfficeId(TEST_NAME, TEST_ID);
        assertEquals(entryDto, EntryMapper.toEntryDto(TEST_ENTRY));
    }

    @Test
    public void getEntryThrowsWhenDoesNotExist() {
        EntryService entryService = new EntryServiceImpl(TEST_REGION_REPO,
                TEST_ACCOUNT_REPO,
                TEST_PORTFOLIO_REPO,
                TEST_OFFICE_REPO,
                TEST_ENTRY_REPO);
        when(TEST_ENTRY_REPO.findByTagAndOfficeId(
                TEST_NAME, TEST_ID)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> entryService.getEntry(TEST_NAME, TEST_ID));
    }

    @Test
    public void getEntriesFromOfficeSucceedsWhenExists() {
        EntryService entryService = new EntryServiceImpl(TEST_REGION_REPO,
                TEST_ACCOUNT_REPO,
                TEST_PORTFOLIO_REPO,
                TEST_OFFICE_REPO,
                TEST_ENTRY_REPO);
        when(TEST_ENTRY_REPO.findAllByOfficeId(TEST_ID)).thenReturn(Optional.of(List.of(TEST_ENTRY)));
        List<EntryDto> entryDtos = entryService.getEntriesFromOffice(TEST_ID);
        verify(TEST_ENTRY_REPO).findAllByOfficeId(TEST_ID);
        assertEquals(entryDtos, List.of(EntryMapper.toEntryDto(TEST_ENTRY)));
    }

    @Test
    public void getEntriesFromOfficeThrowsWhenDoesNotExist() {
        EntryService entryService = new EntryServiceImpl(TEST_REGION_REPO,
                TEST_ACCOUNT_REPO,
                TEST_PORTFOLIO_REPO,
                TEST_OFFICE_REPO,
                TEST_ENTRY_REPO);
        when(TEST_ENTRY_REPO.findAllByOfficeId(TEST_ID)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> entryService.getEntriesFromOffice(TEST_ID));
    }

    @Test
    public void getAllBySourceSucceedsWhenExists() {
        EntryService entryService = new EntryServiceImpl(TEST_REGION_REPO,
                TEST_ACCOUNT_REPO,
                TEST_PORTFOLIO_REPO,
                TEST_OFFICE_REPO,
                TEST_ENTRY_REPO);
        when(TEST_ENTRY_REPO.findAllBySource(TEST_NAME)).thenReturn(Optional.of(List.of(TEST_ENTRY)));
        List<EntryDto> entryDtos = entryService.getAllBySource(TEST_NAME);
        verify(TEST_ENTRY_REPO).findAllBySource(TEST_NAME);
        assertEquals(entryDtos, List.of(EntryMapper.toEntryDto(TEST_ENTRY)));
    }

    @Test
    public void getAllBySourceThrowsWhenDoesNotExist() {
        EntryService entryService = new EntryServiceImpl(TEST_REGION_REPO,
                TEST_ACCOUNT_REPO,
                TEST_PORTFOLIO_REPO,
                TEST_OFFICE_REPO,
                TEST_ENTRY_REPO);
        when(TEST_ENTRY_REPO.findAllBySource(TEST_NAME)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> entryService.getAllBySource(TEST_NAME));
    }

    @Test
    public void createEntrySucceedsWhenAccountPortfolioRegionOfficeExist() {
        EntryService entryService = new EntryServiceImpl(TEST_REGION_REPO,
                TEST_ACCOUNT_REPO,
                TEST_PORTFOLIO_REPO,
                TEST_OFFICE_REPO,
                TEST_ENTRY_REPO);
        when(TEST_REGION_REPO.findDistinctByNameAndUserId(
                TEST_NAME, TEST_ID)).thenReturn(Optional.of(TEST_REGION));
        when(TEST_PORTFOLIO_REPO.findDistinctByTagAndUserId(
                TEST_NAME, TEST_ID)).thenReturn(Optional.of(TEST_PORTFOLIO));
        when(TEST_ACCOUNT_REPO.findDistinctByUserId(TEST_ID)).thenReturn(Optional.of(TEST_ACCOUNT));
        when(TEST_OFFICE_REPO.findDistinctByNameAndUserId(
                TEST_NAME, TEST_ID)).thenReturn(Optional.of(TEST_OFFICE));
        when(TEST_ENTRY_REPO.findByTagAndOfficeId(TEST_NAME, TEST_ID)).thenReturn(Optional.empty());
        entryService.createEntry(TEST_ID, TEST_ID, TEST_ID, TEST_ID, EntryMapper.toEntryDto(TEST_ENTRY));
        verify(TEST_REGION_REPO).findDistinctByNameAndUserId(TEST_NAME, TEST_ID);
        verify(TEST_PORTFOLIO_REPO).findDistinctByTagAndUserId(TEST_NAME, TEST_ID);
        verify(TEST_ACCOUNT_REPO).findDistinctByUserId(TEST_ID);
        verify(TEST_OFFICE_REPO).findDistinctByNameAndUserId(TEST_NAME, TEST_ID);
        verify(TEST_ENTRY_REPO).save(TEST_ENTRY);
    }

    @Test
    public void createEntryThrowsWhenAccountDoesNotExist() {
        EntryService entryService = new EntryServiceImpl(TEST_REGION_REPO,
                TEST_ACCOUNT_REPO,
                TEST_PORTFOLIO_REPO,
                TEST_OFFICE_REPO,
                TEST_ENTRY_REPO);
        when(TEST_REGION_REPO.findDistinctByNameAndUserId(
                TEST_NAME, TEST_ID)).thenReturn(Optional.of(TEST_REGION));
        when(TEST_PORTFOLIO_REPO.findDistinctByTagAndUserId(
                TEST_NAME, TEST_ID)).thenReturn(Optional.of(TEST_PORTFOLIO));
        when(TEST_ACCOUNT_REPO.findDistinctByUserId(TEST_ID)).thenReturn(Optional.empty());
        when(TEST_OFFICE_REPO.findDistinctByNameAndUserId(
                TEST_NAME, TEST_ID)).thenReturn(Optional.of(TEST_OFFICE));
        when(TEST_ENTRY_REPO.findByTagAndOfficeId(TEST_NAME, TEST_ID)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () ->
                entryService.createEntry(TEST_ID, TEST_ID, TEST_ID, TEST_ID, EntryMapper.toEntryDto(TEST_ENTRY)));
    }

    @Test
    public void createEntryThrowsWhenPortfolioDoesNotExist() {
        EntryService entryService = new EntryServiceImpl(TEST_REGION_REPO,
                TEST_ACCOUNT_REPO,
                TEST_PORTFOLIO_REPO,
                TEST_OFFICE_REPO,
                TEST_ENTRY_REPO);
        when(TEST_REGION_REPO.findDistinctByNameAndUserId(
                TEST_NAME, TEST_ID)).thenReturn(Optional.of(TEST_REGION));
        when(TEST_PORTFOLIO_REPO.findDistinctByTagAndUserId(
                TEST_NAME, TEST_ID)).thenReturn(Optional.empty());
        when(TEST_ACCOUNT_REPO.findDistinctByUserId(TEST_ID)).thenReturn(Optional.of(TEST_ACCOUNT));
        when(TEST_OFFICE_REPO.findDistinctByNameAndUserId(
                TEST_NAME, TEST_ID)).thenReturn(Optional.of(TEST_OFFICE));
        when(TEST_ENTRY_REPO.findByTagAndOfficeId(TEST_NAME, TEST_ID)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () ->
                entryService.createEntry(TEST_ID, TEST_ID, TEST_ID, TEST_ID, EntryMapper.toEntryDto(TEST_ENTRY)));
    }

    @Test
    public void createThrowsWhenRegionDoesNotExist() {
        EntryService entryService = new EntryServiceImpl(TEST_REGION_REPO,
                TEST_ACCOUNT_REPO,
                TEST_PORTFOLIO_REPO,
                TEST_OFFICE_REPO,
                TEST_ENTRY_REPO);
        when(TEST_REGION_REPO.findDistinctByNameAndUserId(
                TEST_NAME, TEST_ID)).thenReturn(Optional.empty());
        when(TEST_PORTFOLIO_REPO.findDistinctByTagAndUserId(
                TEST_NAME, TEST_ID)).thenReturn(Optional.of(TEST_PORTFOLIO));
        when(TEST_ACCOUNT_REPO.findDistinctByUserId(TEST_ID)).thenReturn(Optional.of(TEST_ACCOUNT));
        when(TEST_OFFICE_REPO.findDistinctByNameAndUserId(
                TEST_NAME, TEST_ID)).thenReturn(Optional.of(TEST_OFFICE));
        when(TEST_ENTRY_REPO.findByTagAndOfficeId(TEST_NAME, TEST_ID)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () ->
                entryService.createEntry(TEST_ID, TEST_ID, TEST_ID, TEST_ID, EntryMapper.toEntryDto(TEST_ENTRY)));
    }

    @Test
    public void createThrowsWhenOfficeDoesNotExist() {
        EntryService entryService = new EntryServiceImpl(TEST_REGION_REPO,
                TEST_ACCOUNT_REPO,
                TEST_PORTFOLIO_REPO,
                TEST_OFFICE_REPO,
                TEST_ENTRY_REPO);
        when(TEST_REGION_REPO.findDistinctByNameAndUserId(
                TEST_NAME, TEST_ID)).thenReturn(Optional.of(TEST_REGION));
        when(TEST_PORTFOLIO_REPO.findDistinctByTagAndUserId(
                TEST_NAME, TEST_ID)).thenReturn(Optional.of(TEST_PORTFOLIO));
        when(TEST_ACCOUNT_REPO.findDistinctByUserId(TEST_ID)).thenReturn(Optional.of(TEST_ACCOUNT));
        when(TEST_OFFICE_REPO.findDistinctByNameAndUserId(
                TEST_NAME, TEST_ID)).thenReturn(Optional.empty());
        when(TEST_ENTRY_REPO.findByTagAndOfficeId(TEST_NAME, TEST_ID)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () ->
                entryService.createEntry(TEST_ID, TEST_ID, TEST_ID, TEST_ID, EntryMapper.toEntryDto(TEST_ENTRY)));
    }

    @Test
    public void createThrowsWhenEntryAlreadyExists() {
        EntryService entryService = new EntryServiceImpl(TEST_REGION_REPO,
                TEST_ACCOUNT_REPO,
                TEST_PORTFOLIO_REPO,
                TEST_OFFICE_REPO,
                TEST_ENTRY_REPO);
        when(TEST_REGION_REPO.findDistinctByNameAndUserId(
                TEST_NAME, TEST_ID)).thenReturn(Optional.of(TEST_REGION));
        when(TEST_PORTFOLIO_REPO.findDistinctByTagAndUserId(
                TEST_NAME, TEST_ID)).thenReturn(Optional.of(TEST_PORTFOLIO));
        when(TEST_ACCOUNT_REPO.findDistinctByUserId(TEST_ID)).thenReturn(Optional.of(TEST_ACCOUNT));
        when(TEST_OFFICE_REPO.findDistinctByNameAndUserId(
                TEST_NAME, TEST_ID)).thenReturn(Optional.of(TEST_OFFICE));
        when(TEST_ENTRY_REPO.findByTagAndOfficeId(TEST_NAME, TEST_ID)).thenReturn(Optional.of(TEST_ENTRY));
        assertThrows(NoSuchElementException.class, () ->
                entryService.createEntry(TEST_ID, TEST_ID, TEST_ID, TEST_ID, EntryMapper.toEntryDto(TEST_ENTRY)));
    }
}
