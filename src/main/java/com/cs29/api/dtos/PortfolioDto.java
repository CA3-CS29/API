package com.cs29.api.dtos;

import com.cs29.api.models.Region;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@Builder
@Accessors(chain = true)
@JsonDeserialize(builder = PortfolioDto.PortfolioDtoBuilder.class)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PortfolioDto {
    @Id
    @JsonProperty("portfolio_id")
    @NonNull
    private String portfolioId;

    @JsonProperty("user_id")
    @NonNull
    private List<String> userId;

    @JsonProperty("tag")
    private String tag;

    @JsonProperty("num_regions")
    private int numRegions;

    @JsonProperty("regions")
    private List<Region> regions;

    @JsonProperty("created_on")
    @NonNull
    private String createdOn;

    @JsonProperty("updated_on")
    @NonNull
    private String updatedOn;
}
