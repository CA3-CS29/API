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
@Document(collection = "accounts")
@Accessors(chain = true)
@JsonDeserialize(builder = Account.AccountBuilder.class)
public class Account {

    @Id
    @JsonProperty("user_id")
    @NonNull
    private String userId;

    @JsonProperty("num_portfolios")
    private int numPortfolios;

    @JsonProperty("portfolios")
    private List<Portfolio> portfolios;
}
