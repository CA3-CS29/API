package com.cs29.api.services;

import com.cs29.api.ApiApplication;
import com.cs29.api.dtos.EntryDto;
import com.cs29.api.dtos.EntryMapper;
import com.cs29.api.models.Account;
import com.cs29.api.models.Entry;
import com.cs29.api.models.EntryModelMapper;
import com.cs29.api.models.Office;
import com.cs29.api.models.Portfolio;
import com.cs29.api.models.Region;
import com.cs29.api.repositories.AccountRepository;
import com.cs29.api.repositories.EntryRepository;
import com.cs29.api.repositories.OfficeRepository;
import com.cs29.api.repositories.PortfolioRepository;
import com.cs29.api.repositories.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class EntryServiceImpl implements EntryService {
    private final RegionRepository regionRepository;
    private final AccountRepository accountRepository;
    private final PortfolioRepository portfolioRepository;
    private final OfficeRepository officeRepository;
    private final EntryRepository entryRepository;

    @Autowired
    public EntryServiceImpl(RegionRepository regionRepository,
                            AccountRepository accountRepository,
                            PortfolioRepository portfolioRepository,
                            OfficeRepository officeRepository,
                            EntryRepository entryRepository) {
        this.regionRepository = regionRepository;
        this.accountRepository = accountRepository;
        this.portfolioRepository = portfolioRepository;
        this.officeRepository = officeRepository;
        this.entryRepository = entryRepository;
    }

    @Override
    public EntryDto getEntry(String tag, String officeId) {
        Optional<Entry> optionalEntry = getEntryFromRepository(tag, officeId);
        if (optionalEntry.isEmpty()) {
            String errorMessage = String.format(
                    "EntryService could not find entry %s", tag);
            ApiApplication.logger.error(errorMessage);
            throw new NoSuchElementException(errorMessage);
        }
        ApiApplication.logger.info("EntryService service retrieved entry " + tag);
        return EntryMapper.toEntryDto(optionalEntry.get());
    }

    @Override
    public List<EntryDto> getEntriesFromOffice(String officeId) {
        var entriesOptionalList = getEntriesFromRepoByOfficeId(officeId);
        if (entriesOptionalList.isEmpty()) {
            String errorMessage = String.format(
                    "EntryService could not find entries for office %s", officeId);
            ApiApplication.logger.error(errorMessage);
            throw new NoSuchElementException(errorMessage);
        }

        List<EntryDto> entryDtos = new ArrayList<>();
        for (Entry entry : entriesOptionalList.get()) {
            entryDtos.add(EntryMapper.toEntryDto(entry));
        }
        ApiApplication.logger.info("EntryService retrieved "
                + entryDtos.size() + "entries for office " + officeId);
        return entryDtos;
    }

    @Override
    public List<EntryDto> getAllBySource(String source) {
        var entriesOptionalList = getEntriesFromRepoBySource(source);
        if (entriesOptionalList.isEmpty()) {
            String errorMessage = String.format(
                    "EntryService could not find entries for source %s", source);
            ApiApplication.logger.error(errorMessage);
            throw new NoSuchElementException(errorMessage);
        }

        List<EntryDto> entryDtos = new ArrayList<>();
        for (Entry entry : entriesOptionalList.get()) {
            entryDtos.add(EntryMapper.toEntryDto(entry));
        }
        ApiApplication.logger.info("EntryService retrieved "
                + entryDtos.size() + "entries for source " + source);
        return entryDtos;
    }

    @Override
    public void createEntry(String accountId, String portfolioId, String regionId, String officeId, EntryDto entryDto) {

        Optional<Entry> optionalEntry = getEntryFromRepository(entryDto.getTag(), accountId);
        if (optionalEntry.isPresent()) {
            String errorMessage = String.format(
                    "EntryService could not create entry %s as it already exists", entryDto.getTag());
            ApiApplication.logger.error(errorMessage);
            throw new NoSuchElementException(errorMessage);
        }

        Optional<Account> accountOptional = getAccountFromRepository(accountId);
        if (accountOptional.isEmpty()) {
            String errorMessage = String.format(
                    "EntryService could not create entry %s as account does not exist", officeId);
            ApiApplication.logger.error(errorMessage);
            throw new NoSuchElementException(errorMessage);
        }

        List<Portfolio> portfolios = accountOptional.get().getPortfolios();
        Portfolio associatedPortfolio = null;
        for (Portfolio portfolio : portfolios) {
            if (portfolio.getPortfolioId().equals(portfolioId)) {
                associatedPortfolio = portfolio;
            }
        }

        if (associatedPortfolio == null) {
            String errorMessage = String.format(
                    "EntryService could not create entry %s as portfolio does not exist", entryDto.getTag());
            ApiApplication.logger.error(errorMessage);
            throw new NoSuchElementException(errorMessage);
        }

        Optional<Portfolio> portfolio = getPortfolioFromRepository(associatedPortfolio.getTag(), accountId);
        if (portfolio.isEmpty()) {
            String errorMessage = String.format(
                    "EntryService could not create entry %s as portfolio does not exist", entryDto.getTag());
            ApiApplication.logger.error(errorMessage);
            throw new NoSuchElementException(errorMessage);
        }

        List<Region> regions = portfolio.get().getRegions();
        Region associatedRegion = null;
        for (Region region : regions) {
            if (region.getRegionId().equals(regionId)) {
                associatedRegion = region;
            }
        }
        if (associatedRegion == null) {
            String errorMessage = String.format(
                    "EntryService could not create entry %s as region does not exist", entryDto.getTag());
            ApiApplication.logger.error(errorMessage);
            throw new NoSuchElementException(errorMessage);
        }
        Optional<Region> optionalRegion = getRegionFromRepository(associatedRegion.getName(), accountId);
        if (optionalRegion.isEmpty()) {
            String errorMessage = String.format(
                    "EntryService could not create entry %s as region does not exist", entryDto.getTag());
            ApiApplication.logger.error(errorMessage);
            throw new NoSuchElementException(errorMessage);
        }

        List<Office> offices = associatedRegion.getOffices();
        Office associatedOffice = null;
        for (Office office : offices) {
            if (office.getOfficeId().equals(officeId)) {
                associatedOffice = office;
            }
        }
        if (associatedOffice == null) {
            String errorMessage = String.format(
                    "EntryService could not create entry %s as office does not exist", entryDto.getTag());
            ApiApplication.logger.error(errorMessage);
            throw new NoSuchElementException(errorMessage);
        }
        Optional<Office> optionalOffice = getOfficeFromRepository(associatedOffice.getName(), accountId);
        if (optionalOffice.isEmpty()) {
            String errorMessage = String.format(
                    "EntryService could not create entry %s as office does not exist", entryDto.getTag());
            ApiApplication.logger.error(errorMessage);
            throw new NoSuchElementException(errorMessage);
        }

        Entry newEntry = EntryModelMapper.toEntryModel(entryDto);
        optionalOffice.get().getEntries().add(newEntry);
        optionalRegion.get().getOffices().add(optionalOffice.get());
        portfolio.get().getRegions().add(optionalRegion.get());
        accountOptional.get().getPortfolios().add(portfolio.get());

        entryRepository.save(newEntry);
        officeRepository.save(optionalOffice.get());
        regionRepository.save(optionalRegion.get());
        portfolioRepository.save(portfolio.get());
        accountRepository.save(accountOptional.get());

        ApiApplication.logger.info("EntryService added new entry: " + entryDto.getTag());
    }

    private Optional<Entry> getEntryFromRepository(String tag, String officeId) {
        return entryRepository.findByTagAndOfficeId(tag, officeId);
    }

    private Optional<List<Entry>> getEntriesFromRepoByOfficeId(String officeId) {
        return entryRepository.findAllByOfficeId(officeId);
    }

    private Optional<List<Entry>> getEntriesFromRepoBySource(String source) {
        return entryRepository.findAllBySource(source);
    }

    private Optional<Office> getOfficeFromRepository(String name, String userId) {
        return officeRepository.findDistinctByNameAndUserId(name, userId);
    }

    private Optional<Region> getRegionFromRepository(String name, String userId) {
        return regionRepository.findDistinctByNameAndUserId(name, userId);
    }

    private Optional<Account> getAccountFromRepository(String userId) {
        return accountRepository.findDistinctByUserId(userId);
    }

    private Optional<Portfolio> getPortfolioFromRepository(String tag, String userId) {
        return portfolioRepository.findDistinctByTagAndUserId(tag, userId);
    }
}
