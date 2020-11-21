package com.cs29.api.models;

import com.cs29.api.dtos.OfficeDto;

public class OfficeModelMapper {
    public static Office toOfficeModel(OfficeDto officeDto) {
        return Office.builder()
                .officeId(officeDto.getOfficeId())
                .userId(officeDto.getUserId())
                .name(officeDto.getName())
                .entries(officeDto.getEntries())
                .numEntries(officeDto.getNumEntries())
                .regionId(officeDto.getRegionId())
                .build();
    }
}
