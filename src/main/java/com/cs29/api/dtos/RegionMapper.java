package com.cs29.api.dtos;

import com.cs29.api.models.Region;

public class RegionMapper {
    public static RegionDto toRegionDto(Region region) {
        return RegionDto.builder()
                .regionId(region.getRegionId())
                .userId(region.getUserId())
                .portfolioId(region.getPortfolioId())
                .name(region.getName())
                .numOffices(region.getNumOffices())
                .offices(region.getOffices())
                .build();
    }
}
