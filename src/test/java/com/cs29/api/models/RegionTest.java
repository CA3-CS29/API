package com.cs29.api.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegionTest {
    private final ObjectMapper MAPPER = new ObjectMapper();
    private final List<String> TEST_ID = Arrays.asList("TEST_ID1", "TEST_ID2");
    private final String TEST_NAME = "TEST_NAME";
    private final int TEST_NUM_OFFICES = 0;
    private final Region TEST_REGION = Region
            .builder()
            .region_id(TEST_NAME)
            .portfolio_id(TEST_NAME)
            .user_id(TEST_ID)
            .num_offices(TEST_NUM_OFFICES)
            .name(TEST_NAME)
            .offices(null)
            .build();
    private final String TEST_SERIALIZED_REGION = "{\"region_id\":\"TEST_NAME\",\"portfolio_id\":\"TEST_NAME\"," +
            "\"user_id\":[\"TEST_ID1\",\"TEST_ID2\"],\"name\":\"TEST_NAME\",\"num_offices\":0,\"offices\":null}";

    @SneakyThrows
    @Test
    public void givenRegionSerializationSucceeds() {
        String serializedRegion = MAPPER.writeValueAsString(TEST_REGION);
        assertEquals(serializedRegion, TEST_SERIALIZED_REGION);
    }

    @SneakyThrows
    @Test
    public void givenRegionJsonDeserializeSucceeds() {
        Region region = MAPPER.readValue(TEST_SERIALIZED_REGION, Region.class);
        assertEquals(region, TEST_REGION);
    }

}
