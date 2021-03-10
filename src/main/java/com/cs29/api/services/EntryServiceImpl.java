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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
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
        Optional<Region> optionalRegion = getRegionFromRepository(associatedRegion.getName(), accountId, associatedPortfolio.getPortfolioId());
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
        Optional<Office> optionalOffice = getOfficeFromRepository(associatedOffice.getName(), accountId, regionId);
        if (optionalOffice.isEmpty()) {
            String errorMessage = String.format(
                    "EntryService could not create entry %s as office does not exist", entryDto.getTag());
            ApiApplication.logger.error(errorMessage);
            throw new NoSuchElementException(errorMessage);
        }

        var memRegionIndex = new HashMap<String, Integer>();
        for (int i = 0; i < portfolio.get().getRegions().size(); i++) {
            memRegionIndex.put(portfolio.get().getRegions().get(i).getRegionId(), i);
        }
        var regionIndex = memRegionIndex.get(optionalRegion.get().getRegionId());

        var memPortIndex = new HashMap<String, Integer>();
        for (int i = 0; i < accountOptional.get().getPortfolios().size(); i++) {
            memPortIndex.put(accountOptional.get().getPortfolios().get(i).getPortfolioId(), i);
        }
        var portIndex = memPortIndex.get(portfolio.get().getPortfolioId());

        var memOfficeIndex = new HashMap<String, Integer>();
        for (int i = 0; i < optionalRegion.get().getOffices().size(); i++) {
            memOfficeIndex.put(optionalRegion.get().getOffices().get(i).getOfficeId(), i);
        }

        var officeIndex = memOfficeIndex.get(optionalOffice.get().getOfficeId());
        Entry newEntry = EntryModelMapper.toEntryModel(entryDto);
        try {
            optionalOffice.get().getEntries().add(newEntry);
        } catch (Exception e) {
            optionalOffice.get().setEntries(new ArrayList<>());
            optionalOffice.get().getEntries().add(newEntry);
        }
        optionalRegion.get().getOffices().set(officeIndex, optionalOffice.get());
        optionalOffice.get().setNumEntries(optionalOffice.get().getNumEntries() + 1);
        portfolio.get().getRegions().set(regionIndex, optionalRegion.get());
        accountOptional.get().getPortfolios().set(portIndex, portfolio.get());

        entryRepository.save(newEntry);
        officeRepository.save(optionalOffice.get());
        regionRepository.save(optionalRegion.get());
        portfolioRepository.save(portfolio.get());
        accountRepository.save(accountOptional.get());
        ApiApplication.logger.info("EntryService added new entry: " + entryDto.getTag());
    }

    @Override
    public void deleteEntry(String entryId,
                            String officeId, String officeTag,
                            String regionId, String regionName,
                            String portfolioId, String portfolioTag,
                            String userId) {
        Optional<Account> account = getAccountFromRepository(userId);
        if (account.isEmpty()) {
            String errorMessage = String.format(
                    "EntryService could not retrieve account with account id: %s", userId);
            ApiApplication.logger.error(errorMessage);
            throw new NoSuchElementException(errorMessage);
        }

        Optional<Portfolio> portfolio = getPortfolioFromRepository(portfolioTag, portfolioId);

        if (portfolio.isEmpty()) {
            throw new NoSuchElementException();
        }

        Optional<Region> optionalRegion = getRegionFromRepository(regionName, userId, portfolioId);
        if (optionalRegion.isEmpty()) {
            String errorMessage = String.format(
                    "EntryService could not delete entry %s as region does not exist", regionId);
            ApiApplication.logger.error(errorMessage);
            throw new NoSuchElementException(errorMessage);
        }

        var memPortIndex = new HashMap<String, Integer>();
        for (int i = 0; i < account.get().getPortfolios().size(); i++) {
            memPortIndex.put(account.get().getPortfolios().get(i).getPortfolioId(), i);
        }
        int portIndex = memPortIndex.get(portfolioId);

        var memOfficeIndex = new HashMap<String, Integer>();
        for (int i = 0; i < optionalRegion.get().getOffices().size(); i++) {
            memOfficeIndex.put(optionalRegion.get().getOffices().get(i).getOfficeId(), i);
        }

        int officeIndex = memOfficeIndex.get(officeId);

        var memRegionIndex = new HashMap<String, Integer>();
        for (int i = 0; i < portfolio.get().getRegions().size(); i++) {
            memRegionIndex.put(portfolio.get().getRegions().get(i).getRegionId(), i);
        }
        var regionIndex = memRegionIndex.get(optionalRegion.get().getRegionId());

        var entryOfficeIndex = new HashMap<String, Integer>();
        Optional<Office> office = getOfficeFromRepository(officeTag, userId, regionId);
        if (office.isEmpty()) {
            throw new NoSuchElementException();
        }
        for (int i = 0; i < office.get().getEntries().size(); i++) {
            entryOfficeIndex.put(office.get().getEntries().get(i).getOfficeId(), i);
        }
        int entryIndex = entryOfficeIndex.get(entryId);

        office.get().getEntries().remove(entryIndex);
        optionalRegion.get().getOffices().set(officeIndex, office.get());
        portfolio.get().getRegions().set(regionIndex, optionalRegion.get());
        account.get().getPortfolios().set(portIndex, portfolio.get());

        accountRepository.save(account.get());
        regionRepository.save(optionalRegion.get());
        portfolioRepository.save(portfolio.get());
        officeRepository.save(office.get());
        entryRepository.deleteById(entryId);
        ApiApplication.logger.info("EntryService deleted entry with entry id: " + entryId);
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

    private Optional<Office> getOfficeFromRepository(String name, String userId, String regionId) {
        return officeRepository.findDistinctByNameAndUserIdAndRegionId(name, userId, regionId);
    }

    private Optional<Region> getRegionFromRepository(String name, String userId, String portfolioId) {
        return regionRepository.findDistinctByNameAndUserIdAndPortfolioId(name, userId, portfolioId);
    }

    private Optional<Account> getAccountFromRepository(String userId) {
        return accountRepository.findDistinctByUserId(userId);
    }

    private Optional<Portfolio> getPortfolioFromRepository(String tag, String userId) {
        return portfolioRepository.findDistinctByTagAndUserId(tag, userId);
    }
}
