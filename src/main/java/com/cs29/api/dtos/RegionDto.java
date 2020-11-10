package com.cs29.api.dtos;

import com.cs29.api.models.Office;
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
@JsonDeserialize(builder = RegionDto.RegionDtoBuilder.class)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegionDto {
    @Id
    @JsonProperty("region_id")
    @NonNull
    private String regionId;

    @JsonProperty("portfolio_id")
    @NonNull
    private String portfolioId;

    @JsonProperty("user_id")
    @NonNull
    private List<String> userId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("num_offices")
    private int numOffices;

    @JsonProperty("offices")
    private List<Office> offices;
}
