package com.cs29.api.models;

import com.cs29.api.dtos.EntryDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EntryModelMapperTest {
    private final ObjectMapper MAPPER = new ObjectMapper();
    private final String TEST_ID = "test_id";
    private final String TEST_TAG = "test_tag";
    private final double TEST_DOUBLE = 10.5;
    private final String TEST_SOURCE = "test_source";
    private final String TEST_UNITS = "test_units";
    private final String TEST_LEVEL = "test_level";
    private final String TEST_FURTHER_INFO = "test_further_info";
    private final EntryDto TEST_ENTRY_DTO = EntryDto.builder()
            .entryId(TEST_ID)
            .officeId(TEST_ID)
            .tag(TEST_TAG)
            .consumption(TEST_DOUBLE)
            .original(TEST_DOUBLE)
            .converted(TEST_DOUBLE)
            .source(TEST_SOURCE)
            .units(TEST_UNITS)
            .level1(TEST_LEVEL)
            .level2(TEST_LEVEL)
            .level3(TEST_LEVEL)
            .level4(TEST_LEVEL)
            .furtherInfo(TEST_FURTHER_INFO)
            .components(null)
            .build();
    private final Entry TEST_ENTRY = Entry.builder()
            .entryId(TEST_ID)
            .officeId(TEST_ID)
            .tag(TEST_TAG)
            .consumption(TEST_DOUBLE)
            .original(TEST_DOUBLE)
            .converted(TEST_DOUBLE)
            .source(TEST_SOURCE)
            .units(TEST_UNITS)
            .level1(TEST_LEVEL)
            .level2(TEST_LEVEL)
            .level3(TEST_LEVEL)
            .level4(TEST_LEVEL)
            .furtherInfo(TEST_FURTHER_INFO)
            .components(null)
            .build();

    @Test
    public void givenEntryDtoMapToModelSucceeds() {
        Entry entry = EntryModelMapper.toEntryModel(TEST_ENTRY_DTO);
        assertEquals(entry, TEST_ENTRY);
    }
}
