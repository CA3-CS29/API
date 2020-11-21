package com.cs29.api.models;

import com.cs29.api.dtos.PortfolioDto;

public class PortfolioModelMapper {
    public static Portfolio toPortfolioModel(PortfolioDto portfolioDto) {
        return Portfolio.builder()
                .userId(portfolioDto.getUserId())
                .portfolioId(portfolioDto.getPortfolioId())
                .regions(portfolioDto.getRegions())
                .numRegions(portfolioDto.getNumRegions())
                .tag(portfolioDto.getTag())
                .createdOn(portfolioDto.getCreatedOn())
                .updatedOn(portfolioDto.getUpdatedOn())
                .build();
    }
}
