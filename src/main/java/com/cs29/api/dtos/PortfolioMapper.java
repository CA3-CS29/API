package com.cs29.api.dtos;

import com.cs29.api.models.Portfolio;

public class PortfolioMapper {
    public static PortfolioDto toPortfolioDto(Portfolio portfolio) {
        return PortfolioDto.builder()
                .userId(portfolio.getUserId())
                .portfolioId(portfolio.getPortfolioId())
                .numRegions(portfolio.getNumRegions())
                .regions(portfolio.getRegions())
                .createdOn(portfolio.getCreatedOn())
                .updatedOn(portfolio.getUpdatedOn())
                .tag(portfolio.getTag())
                .build();
    }
}
