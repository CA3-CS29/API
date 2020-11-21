package com.cs29.api.services;

import com.cs29.api.dtos.PortfolioDto;

import java.util.List;

public interface PortfolioService {
    PortfolioDto getPortfolio(String tag, String userId);

    void createPortfolio(String userId, PortfolioDto portfolioDto);

    void updatePortfolio(String userId, PortfolioDto portfolioDto);

    List<PortfolioDto> getAllByTag(String tag);

    List<PortfolioDto> getAllByUserId(String userId);
}
