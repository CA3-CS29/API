package com.cs29.api.services;

import com.cs29.api.dtos.RegionDto;

import java.util.List;

public interface RegionService {

    RegionDto getRegion(String name, String userId, String portfolioId);

    List<RegionDto> getAllRegionsForUser(String userId);

    void createRegion(String accountId, String portfolioId, RegionDto regionDto, String userId);

}
