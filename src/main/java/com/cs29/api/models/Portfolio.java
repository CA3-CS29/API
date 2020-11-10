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
@Document(collection = "portfolios")
@Accessors(chain = true)
@JsonDeserialize(builder = Portfolio.PortfolioBuilder.class)
public class Portfolio {

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
