package com.cs29.api.dtos;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PortfolioDtoTest {

    private final ObjectMapper MAPPER = new ObjectMapper();
    private final String TEST_ID = "TEST_ID1";
    private final String TEST_NAME = "TEST_NAME";
    private final int TEST_NUM_REGIONS = 0;
    private final String TEST_DATE = "10/10/2020";
    private final PortfolioDto TEST_PORTFOLIO_DTO = PortfolioDto
            .builder()
            .portfolioId(TEST_NAME)
            .userId(TEST_ID)
            .tag(TEST_NAME)
            .regions(null)
            .numRegions(TEST_NUM_REGIONS)
            .createdOn(TEST_DATE)
            .updatedOn(TEST_DATE)
            .build();
    private final String TEST_SERIALIZED_PORTFOLIO_DTO = "{\"portfolio_id\":\"TEST_NAME\",\"user_id\":\"TEST_ID1\"" +
            ",\"tag\":\"TEST_NAME\",\"num_regions\":0,\"created_on\":\"10/10/2020\",\"updated_on\":\"10/10/2020\"}";

    @SneakyThrows
    @Test
    public void givenPortfolioDtoSerializationSucceeds() {
        String serializedPortfolio = MAPPER.writeValueAsString(TEST_PORTFOLIO_DTO);
        assertEquals(serializedPortfolio, TEST_SERIALIZED_PORTFOLIO_DTO);
    }

    @SneakyThrows
    @Test
    public void givenPortfolioDtoJsonDeserializeSucceeds() {
        PortfolioDto portfolioDto = MAPPER.readValue(TEST_SERIALIZED_PORTFOLIO_DTO, PortfolioDto.class);
        assertEquals(portfolioDto, TEST_PORTFOLIO_DTO);
    }
}
