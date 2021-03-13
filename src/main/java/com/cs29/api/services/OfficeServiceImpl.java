package com.cs29.api.services;

import com.cs29.api.ApiApplication;
import com.cs29.api.dtos.OfficeDto;
import com.cs29.api.dtos.OfficeMapper;
import com.cs29.api.models.Account;
import com.cs29.api.models.Office;
import com.cs29.api.models.OfficeModelMapper;
import com.cs29.api.models.Portfolio;
import com.cs29.api.models.Region;
import com.cs29.api.repositories.AccountRepository;
import com.cs29.api.repositories.OfficeRepository;
import com.cs29.api.repositories.PortfolioRepository;
import com.cs29.api.repositories.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class OfficeServiceImpl implements OfficeService {
    private final RegionRepository regionRepository;
    private final AccountRepository accountRepository;
    private final PortfolioRepository portfolioRepository;
    private final OfficeRepository officeRepository;

    @Autowired
    public OfficeServiceImpl(RegionRepository regionRepository,
                             AccountRepository accountRepository,
                             PortfolioRepository portfolioRepository,
                             OfficeRepository officeRepository) {
        this.regionRepository = regionRepository;
        this.accountRepository = accountRepository;
        this.portfolioRepository = portfolioRepository;
        this.officeRepository = officeRepository;
    }


    @Override
    public OfficeDto getOffice(String name, String userId, String regionId) {
        Optional<Office> optionalOffice = getOfficeFromRepository(name, userId, regionId);
        if (optionalOffice.isEmpty()) {
            String errorMessage = String.format(
                    "OfficeService could not find office %s", name);
            ApiApplication.logger.error(errorMessage);
            throw new NoSuchElementException(errorMessage);
        }
        ApiApplication.logger.info("Office service retrieved office " + name);
        return OfficeMapper.toOfficeDto(optionalOffice.get());
    }

    @Override
    public List<OfficeDto> getAllUsersOffices(String userId) {
        var optionalOffices = getOfficesFromRepoForUser(userId);
        if (optionalOffices.isEmpty()) {
            String errorMessage = String.format(
                    "OfficeService could not find offices for user %s", userId);
            ApiApplication.logger.error(errorMessage);
            throw new NoSuchElementException(errorMessage);
        }
        List<OfficeDto> officeDtoList = new ArrayList<>();
        for (Office office : optionalOffices.get()) {
            officeDtoList.add(OfficeMapper.toOfficeDto(office));
        }
        ApiApplication.logger.info("OfficeService retrieved "
                + officeDtoList.size() + "offices for user " + userId);
        return officeDtoList;
    }

    @Override
    public void createOffice(String regionId, String portfolioId, String accountId, OfficeDto officeDto) {
        Optional<Office> optionalOffice = getOfficeFromRepository(officeDto.getName(), accountId, regionId);
        if (optionalOffice.isPresent()) {
            String errorMessage = String.format(
                    "OfficeService could not create office %s as it already exists", officeDto.getName());
            ApiApplication.logger.error(errorMessage);
            throw new NoSuchElementException(errorMessage);
        }

        Optional<Account> accountOptional = getAccountFromRepository(accountId);
        if (accountOptional.isEmpty()) {
            String errorMessage = String.format(
                    "OfficeService could not create office %s as account does not exist", officeDto.getName());
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
                    "OfficeService could not create office %s as portfolio does not exist", regionId);
            ApiApplication.logger.error(errorMessage);
            throw new NoSuchElementException(errorMessage);
        }

        Optional<Portfolio> portfolio = getPortfolioFromRepository(associatedPortfolio.getTag(), accountId);
        if (portfolio.isEmpty()) {
            String errorMessage = String.format(
                    "OfficeService could not create office %s as portfolio does not exist", portfolioId);
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
                    "OfficeService could not create office %s as region does not exist", regionId);
            ApiApplication.logger.error(errorMessage);
            throw new NoSuchElementException(errorMessage);
        }
        Optional<Region> optionalRegion = getRegionFromRepository(associatedRegion.getName(), accountId, associatedPortfolio.getPortfolioId());
        if (optionalRegion.isEmpty()) {
            String errorMessage = String.format(
                    "OfficeService could not create office %s as region does not exist", regionId);
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
        Office newOffice = OfficeModelMapper.toOfficeModel(officeDto);
        optionalRegion.get().getOffices().add(newOffice);
        optionalRegion.get().setNumOffices(optionalRegion.get().getNumOffices() + 1);
        portfolio.get().getRegions().set(regionIndex, optionalRegion.get());
        accountOptional.get().getPortfolios().set(portIndex, portfolio.get());

        officeRepository.save(newOffice);
        regionRepository.save(optionalRegion.get());
        portfolioRepository.save(portfolio.get());
        accountRepository.save(accountOptional.get());

        ApiApplication.logger.info("OfficeService added new office: " + officeDto.getName());

    }

    @Override
    public void deleteOffice(String officeId, String portfolioId,
                             String portfolioTag, String regionId, String regionName, String userId) {
        Optional<Account> account = getAccountFromRepository(userId);
        if (account.isEmpty()) {
            String errorMessage = String.format(
                    "OfficeService could not retrieve account with account id: %s", userId);
            ApiApplication.logger.error(errorMessage);
            throw new NoSuchElementException(errorMessage);
        }

        Optional<Portfolio> portfolio = getPortfolioFromRepository(portfolioTag, userId);

        if (portfolio.isEmpty()) {
            throw new NoSuchElementException();
        }

        Optional<Region> optionalRegion = getRegionFromRepository(regionName, userId, portfolioId);
        if (optionalRegion.isEmpty()) {
            String errorMessage = String.format(
                    "OfficeService could not delete office %s as region does not exist", regionId);
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
        optionalRegion.get().getOffices().remove(officeIndex);
        optionalRegion.get().setNumOffices(optionalRegion.get().getNumOffices() - 1);
        portfolio.get().getRegions().set(regionIndex, optionalRegion.get());
        account.get().getPortfolios().set(portIndex, portfolio.get());

        accountRepository.save(account.get());
        regionRepository.save(optionalRegion.get());
        portfolioRepository.save(portfolio.get());
        officeRepository.deleteById(officeId);
        ApiApplication.logger.info("OfficeService deleted office with office id: " + officeId);

    }

    private Optional<Office> getOfficeFromRepository(String name, String userId, String regionId) {
        return officeRepository.findDistinctByNameAndUserIdAndRegionId(name, userId, regionId);
    }

    private Optional<List<Office>> getOfficesFromRepoForUser(String userId) {
        return officeRepository.findAllByUserId(userId);
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
