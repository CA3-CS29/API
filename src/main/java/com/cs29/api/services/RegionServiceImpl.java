package com.cs29.api.services;

import com.cs29.api.ApiApplication;
import com.cs29.api.dtos.RegionDto;
import com.cs29.api.dtos.RegionMapper;
import com.cs29.api.models.Account;
import com.cs29.api.models.Portfolio;
import com.cs29.api.models.Region;
import com.cs29.api.models.RegionModelMapper;
import com.cs29.api.repositories.AccountRepository;
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
public class RegionServiceImpl implements RegionService {
    private final RegionRepository regionRepository;
    private final AccountRepository accountRepository;
    private final PortfolioRepository portfolioRepository;

    @Autowired
    public RegionServiceImpl(RegionRepository regionRepository,
                             AccountRepository accountRepository,
                             PortfolioRepository portfolioRepository) {
        this.regionRepository = regionRepository;
        this.accountRepository = accountRepository;
        this.portfolioRepository = portfolioRepository;
    }

    @Override
    @Cacheable(value = "regionCache", key = "#name ")
    public RegionDto getRegion(String name, String userId, String portfolioId) {
        Optional<Region> regionOptional = getRegionFromRepository(name, userId, portfolioId);
        if (regionOptional.isEmpty()) {
            String errorMessage = String.format(
                    "RegionService could not find region %s", name);
            ApiApplication.logger.error(errorMessage);
            throw new NoSuchElementException(errorMessage);
        }
        return RegionMapper.toRegionDto(regionOptional.get());
    }

    @Override
    @Cacheable(value = "allRegionsForUserCache", key = "#userId")
    public List<RegionDto> getAllRegionsForUser(String userId) {
        var regionsOptional = getAllRegionsFromRepository(userId);
        if (regionsOptional.isEmpty()) {
            String errorMessage = String.format(
                    "RegionService could not find regions for user %s", userId);
            ApiApplication.logger.error(errorMessage);
            throw new NoSuchElementException(errorMessage);
        }
        List<RegionDto> dtos = new ArrayList<>();
        for (Region region : regionsOptional.get()) {
            dtos.add(RegionMapper.toRegionDto(region));
        }
        return dtos;
    }

    @Override
    public void createRegion(String accountId, String portfolioId, RegionDto regionDto, String userId) {
        Optional<Region> regionOptional = getRegionFromRepository(regionDto.getName(), userId, regionDto.getPortfolioId());
        if (regionOptional.isPresent()) {
            String errorMessage = String.format(
                    "RegionService could not create region %s as it already exists", regionDto.getName());
            ApiApplication.logger.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        Optional<Account> account = getAccountFromRepository(userId);
        if (account.isEmpty()) {
            String errorMessage = String.format(
                    "RegionService could not create region %s as account does not exist", regionDto.getName());
            ApiApplication.logger.error(errorMessage);
            throw new NoSuchElementException(errorMessage);
        }

        List<Portfolio> portfolios = account.get().getPortfolios();
        Portfolio associatedPortfolio = null;
        for (Portfolio portfolio : portfolios) {
            if (portfolio.getPortfolioId().equals(regionDto.getPortfolioId())) {
                associatedPortfolio = portfolio;
            }
        }

        if (associatedPortfolio == null) {
            String errorMessage = String.format(
                    "RegionService could not create region %s as portfolio does not exist", regionDto.getName());
            ApiApplication.logger.error(errorMessage);
            throw new NoSuchElementException(errorMessage);
        }

        Optional<Portfolio> portfolio = getPortfolioFromRepository(associatedPortfolio.getTag(), userId);
        if (portfolio.isEmpty()) {
            String errorMessage = String.format(
                    "RegionService could not create region %s as portfolio does not exist", regionDto.getName());
            ApiApplication.logger.error(errorMessage);
            throw new NoSuchElementException(errorMessage);
        }

        Region newRegion = RegionModelMapper.toRegionModel(regionDto);
        portfolio.get().getRegions().add(newRegion);
        account.get().getPortfolios().set(account.get().getPortfolios().indexOf(portfolio.get()), portfolio.get());
        regionRepository.save(newRegion);
        portfolioRepository.save(portfolio.get());
        accountRepository.save(account.get());

        ApiApplication.logger.info("RegionService added new region: " + regionDto.getName());
    }

    private Optional<Region> getRegionFromRepository(String name, String userId, String portfolioId) {
        return regionRepository.findDistinctByNameAndUserIdAndPortfolioId(name, userId, portfolioId);
    }

    private Optional<List<Region>> getAllRegionsFromRepository(String userId) {
        return regionRepository.findAllByUserId(userId);
    }

    private Optional<Account> getAccountFromRepository(String userId) {
        return accountRepository.findDistinctByUserId(userId);
    }

    private Optional<Portfolio> getPortfolioFromRepository(String tag, String userId) {
        return portfolioRepository.findDistinctByTagAndUserId(tag, userId);
    }
}
