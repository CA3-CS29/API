package com.cs29.api.models;

import com.cs29.api.dtos.RegionDto;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegionModelMapperTest {
    private final List<String> TEST_ID = Arrays.asList("TEST_ID1", "TEST_ID2");
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
    public void givenRegionDtoMapToModelSucceeds() {
        Region region = RegionModelMapper.toRegionModel(TEST_REGION_DTO);
        assertEquals(region, TEST_REGION);
    }
}
