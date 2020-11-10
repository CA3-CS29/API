package com.cs29.api.dtos;

import com.cs29.api.models.Office;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OfficeMapperTest {
    private final List<String> TEST_ID = Arrays.asList("TEST_ID1", "TEST_ID2");
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
