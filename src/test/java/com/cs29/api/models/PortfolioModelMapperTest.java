package com.cs29.api.models;

import com.cs29.api.dtos.PortfolioDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PortfolioModelMapperTest {
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

    @Test
    public void givenPortfolioDtoMapToModelSucceeds() {
        Portfolio portfolio = PortfolioModelMapper.toPortfolioModel(TEST_PORTFOLIO_DTO);
        assertEquals(portfolio, TEST_PORTFOLIO);
    }
}
