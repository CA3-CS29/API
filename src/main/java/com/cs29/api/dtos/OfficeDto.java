package com.cs29.api.dtos;

import com.cs29.api.models.Entry;
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
@JsonDeserialize(builder = OfficeDto.OfficeDtoBuilder.class)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OfficeDto {
    @Id
    @JsonProperty("office_id")
    @NonNull
    private String officeId;

    @JsonProperty("user_id")
    @NonNull
    private String userId;

    @JsonProperty("region_id")
    @NonNull
    private String regionId;

    @JsonProperty("name")
    @NonNull
    private String name;

    @JsonProperty("num_entries")
    private int numEntries;

    @JsonProperty("entries")
    private List<Entry> entries;
}
