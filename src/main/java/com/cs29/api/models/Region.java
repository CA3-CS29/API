package com.cs29.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Document(collection = "regions")
@Accessors(chain = true)
@JsonDeserialize(builder = Region.RegionBuilder.class)
public class Region {

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
