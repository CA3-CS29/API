package com.cs29.api.models;

import com.cs29.api.dtos.OfficeDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OfficeModelMapperTest {
    private final String TEST_ID = "test_id";
    private final String TEST_NAME = "TEST_NAME";
    private final int TEST_NUM_ENTRIES = 0;
    private final OfficeDto TEST_OFFICE_DTO = OfficeDto
            .builder()
            .officeId(TEST_ID)
            .regionId(TEST_NAME)
            .userId(TEST_ID)
            .name(TEST_NAME)
            .numEntries(TEST_NUM_ENTRIES)
            .entries(null)
            .build();
    private final Office TEST_OFFICE = Office
            .builder()
            .officeId(TEST_ID)
            .regionId(TEST_NAME)
            .userId(TEST_ID)
            .name(TEST_NAME)
            .numEntries(TEST_NUM_ENTRIES)
            .entries(null)
            .build();

    @Test
    public void givenOfficeDtoMapToModelSucceeds() {
        Office office = OfficeModelMapper.toOfficeModel(TEST_OFFICE_DTO);
        assertEquals(office, TEST_OFFICE);
    }
}
