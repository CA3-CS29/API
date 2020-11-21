package com.cs29.api.models;

import com.cs29.api.dtos.EntryDto;

public class EntryModelMapper {
    public static Entry toEntryModel(EntryDto entryDto) {
        return Entry.builder()
                .entryId(entryDto.getEntryId())
                .officeId(entryDto.getOfficeId())
                .level1(entryDto.getLevel1())
                .level2(entryDto.getLevel2())
                .level3(entryDto.getLevel3())
                .level4(entryDto.getLevel4())
                .components(entryDto.getComponents())
                .consumption(entryDto.getConsumption())
                .converted(entryDto.getConverted())
                .furtherInfo(entryDto.getFurtherInfo())
                .original(entryDto.getOriginal())
                .percentage(entryDto.getPercentage())
                .source(entryDto.getSource())
                .tag(entryDto.getTag())
                .units(entryDto.getUnits())
                .build();
    }
}
