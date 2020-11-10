package com.cs29.api.dtos;

import com.cs29.api.models.Portfolio;
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
@JsonDeserialize(builder = AccountDto.AccountDtoBuilder.class)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountDto {
    @Id
    @JsonProperty("user_id")
    @NonNull
    private String userId;

    @JsonProperty("num_portfolios")
    private int numPortfolios;

    @JsonProperty("portfolios")
    private List<Portfolio> portfolios;
}
