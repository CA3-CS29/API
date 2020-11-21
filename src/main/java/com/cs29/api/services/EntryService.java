package com.cs29.api.services;

import com.cs29.api.dtos.EntryDto;

import java.util.List;

public interface EntryService {
    EntryDto getEntry(String tag, String officeId);

    List<EntryDto> getEntriesFromOffice(String officeId);

    List<EntryDto> getAllBySource(String source);

    void createEntry(String accountId, String portfolioId, String regionId, String officeId, EntryDto entryDto);

}
