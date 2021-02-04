package com.cs29.api.dtos;

import com.cs29.api.models.Region;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegionMapperTest {
    private final String TEST_ID = "TEST_ID1";
    private final String TEST_NAME = "TEST_NAME";
    private final int TEST_NUM_OFFICES = 0;

    private final RegionDto TEST_REGION_DTO = RegionDto
            .builder()
            .regionId(TEST_NAME)
            .portfolioId(TEST_NAME)
            .userId(TEST_ID)
            .numOffices(TEST_NUM_OFFICES)
            .name(TEST_NAME)
            .offices(null)
            .build();

    private final Region TEST_REGION = Region
            .builder()
            .regionId(TEST_NAME)
            .portfolioId(TEST_NAME)
            .userId(TEST_ID)
            .numOffices(TEST_NUM_OFFICES)
            .name(TEST_NAME)
            .offices(null)
            .build();

    @Test
    public void givenRegionMapToDtoSucceeds() {
        RegionDto regionDto = RegionMapper.toRegionDto(TEST_REGION);
        assertEquals(regionDto, TEST_REGION_DTO);
    }
}
