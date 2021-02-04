package com.cs29.api.dtos;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegionDtoTest {
    private final ObjectMapper MAPPER = new ObjectMapper();
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
    private final String TEST_SERIALIZED_REGION = "{\"region_id\":\"TEST_NAME\",\"portfolio_id\":\"TEST_NAME\"," +
            "\"user_id\":\"TEST_ID1\",\"name\":\"TEST_NAME\",\"num_offices\":0}";

    @SneakyThrows
    @Test
    public void givenRegionDtoSerializationSucceeds() {
        String serializedRegion = MAPPER.writeValueAsString(TEST_REGION_DTO);
        assertEquals(serializedRegion, TEST_SERIALIZED_REGION);
    }

    @SneakyThrows
    @Test
    public void givenRegionDtoJsonDeserializeSucceeds() {
        RegionDto regionDto = MAPPER.readValue(TEST_SERIALIZED_REGION, RegionDto.class);
        assertEquals(regionDto, TEST_REGION_DTO);
    }
}
