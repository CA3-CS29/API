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
    private String portfolio_id;

    @JsonProperty("user_id")
    @NonNull
    private String user_id;

    @JsonProperty("tag")
    private String tag;

    @JsonProperty("num_regions")
    private int num_regions;

    @JsonProperty("regions")
    private List<Region> regions;

    @JsonProperty("created_on")
    @NonNull
    private String created_on;

    @JsonProperty("updated_on")
    @NonNull
    private String updated_on;
}
