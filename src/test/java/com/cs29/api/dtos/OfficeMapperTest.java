package com.cs29.api.dtos;

import com.cs29.api.models.Office;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OfficeMapperTest {
    private final String TEST_ID = "TEST_ID1";
    private final String TEST_NAME = "TEST_NAME";
    private final int TEST_NUM_ENTRIES = 0;
    private final OfficeDto TEST_OFFICE_DTO = OfficeDto
            .builder()
            .officeId(TEST_NAME)
            .regionId(TEST_NAME)
            .userId(TEST_ID)
            .name(TEST_NAME)
            .numEntries(TEST_NUM_ENTRIES)
            .entries(null)
            .build();
    private final Office TEST_OFFICE = Office
            .builder()
            .officeId(TEST_NAME)
            .regionId(TEST_NAME)
            .userId(TEST_ID)
            .name(TEST_NAME)
            .numEntries(TEST_NUM_ENTRIES)
            .entries(null)
            .build();

    @Test
    public void givenOfficeMapToDtoSucceeds() {
        OfficeDto officeDto = OfficeMapper.toOfficeDto(TEST_OFFICE);
        assertEquals(officeDto, TEST_OFFICE_DTO);
    }
}
