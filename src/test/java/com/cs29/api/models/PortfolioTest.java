package com.cs29.api.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PortfolioTest {
    private final ObjectMapper MAPPER = new ObjectMapper();
    private final List<String> TEST_ID = Arrays.asList("TEST_ID1", "TEST_ID2");
    private final String TEST_NAME = "TEST_NAME";
    private final int TEST_NUM_REGIONS = 0;
    private final String TEST_DATE = "10/10/2020";
    private final Portfolio TEST_PORTFOLIO = Portfolio
            .builder()
            .portfolioId(TEST_NAME)
            .userId(TEST_ID)
            .tag(TEST_NAME)
            .regions(null)
            .numRegions(TEST_NUM_REGIONS)
            .createdOn(TEST_DATE)
            .updatedOn(TEST_DATE)
            .build();
    private final String TEST_SERIALIZED_PORTFOLIO = "{\"portfolio_id\":\"TEST_NAME\",\"user_id\":[\"TEST_ID1\",\"TEST_ID2\"]," +
            "\"tag\":\"TEST_NAME\",\"num_regions\":0,\"regions\":null,\"created_on\":\"10/10/2020\"," +
            "\"updated_on\":\"10/10/2020\"}";

    @SneakyThrows
    @Test
    public void givenPortfolioSerializationSucceeds() {
        String serializedPortfolio = MAPPER.writeValueAsString(TEST_PORTFOLIO);
        assertEquals(serializedPortfolio, TEST_SERIALIZED_PORTFOLIO);
    }

    @SneakyThrows
    @Test
    public void givenPortfolioJsonDeserializeSucceeds() {
        Portfolio portfolio = MAPPER.readValue(TEST_SERIALIZED_PORTFOLIO, Portfolio.class);
        assertEquals(portfolio, TEST_PORTFOLIO);
    }
}
