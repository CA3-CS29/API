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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    @Cacheable(value = "officeCache", key = "#name")
    public OfficeDto getOffice(String name, String userId) {
        Optional<Office> optionalOffice = getOfficeFromRepository(name, userId);
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
    @Cacheable(value = "usersOfficesCache", key = "#userId")
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
        Optional<Office> optionalOffice = getOfficeFromRepository(officeDto.getName(), accountId);
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

        Office newOffice = OfficeModelMapper.toOfficeModel(officeDto);
        optionalRegion.get().getOffices().add(newOffice);
        portfolio.get().getRegions().set(portfolio.get().getRegions().indexOf(optionalRegion.get()),
                optionalRegion.get());
        accountOptional.get().getPortfolios().set(accountOptional.get().getPortfolios().indexOf(portfolio.get()),
                portfolio.get());

        officeRepository.save(newOffice);
        regionRepository.save(optionalRegion.get());
        portfolioRepository.save(portfolio.get());
        accountRepository.save(accountOptional.get());

        ApiApplication.logger.info("OfficeService added new office: " + officeDto.getName());

    }

    private Optional<Office> getOfficeFromRepository(String name, String userId) {
        return officeRepository.findDistinctByNameAndUserId(name, userId);
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
