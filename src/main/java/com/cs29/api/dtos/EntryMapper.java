package com.cs29.api.dtos;

import com.cs29.api.models.Entry;

public class EntryMapper {
    public static EntryDto toEntryDto(Entry entry) {
        return EntryDto.builder()
                .entryId(entry.getEntryId())
                .officeId(entry.getOfficeId())
                .components(entry.getComponents())
                .consumption(entry.getConsumption())
                .converted(entry.getConverted())
                .furtherInfo(entry.getFurtherInfo())
                .level1(entry.getLevel1())
                .level2(entry.getLevel2())
                .level3(entry.getLevel3())
                .level4(entry.getLevel4())
                .original(entry.getOriginal())
                .percentage(entry.getPercentage())
                .source(entry.getSource())
                .tag(entry.getTag())
                .units(entry.getUnits())
                .build();
    }
}
