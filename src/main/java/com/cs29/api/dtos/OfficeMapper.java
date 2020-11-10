package com.cs29.api.dtos;

import com.cs29.api.models.Office;

public class OfficeMapper {
    public static OfficeDto toOfficeDto(Office office) {
        return OfficeDto.builder()
                .officeId(office.getOfficeId())
                .regionId(office.getRegionId())
                .userId(office.getUserId())
                .entries(office.getEntries())
                .numEntries(office.getNumEntries())
                .name(office.getName())
                .build();
    }
}
