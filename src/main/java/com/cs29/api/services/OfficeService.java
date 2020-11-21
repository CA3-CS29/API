package com.cs29.api.services;

import com.cs29.api.dtos.OfficeDto;

import java.util.List;

public interface OfficeService {
    OfficeDto getOffice(String name, String userId);

    List<OfficeDto> getAllUsersOffices(String userId);

    void createOffice(String regionId, String portfolioId, String accountId, OfficeDto officeDto);
}
