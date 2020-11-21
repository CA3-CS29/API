package com.cs29.api.models;

import com.cs29.api.dtos.RegionDto;

public class RegionModelMapper {
    public static Region toRegionModel(RegionDto regionDto) {
        return Region.builder()
                .userId(regionDto.getUserId())
                .name(regionDto.getName())
                .numOffices(regionDto.getNumOffices())
                .offices(regionDto.getOffices())
                .portfolioId(regionDto.getPortfolioId())
                .regionId(regionDto.getRegionId())
                .build();
    }
}
