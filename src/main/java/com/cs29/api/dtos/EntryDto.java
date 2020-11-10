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
@JsonDeserialize(builder = EntryDto.EntryDtoBuilder.class)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EntryDto {
    @Id
    @JsonProperty("entry_id")
    @NonNull
    private String entryId;

    @JsonProperty("office_id")
    @NonNull
    private String officeId;

    @JsonProperty("tag")
    @NonNull
    private String tag;

    @JsonProperty("consumption")
    private double consumption;

    @JsonProperty("original")
    private double original;

    @JsonProperty("converted")
    private double converted;

    @JsonProperty("source")
    @NonNull
    private String source;

    @JsonProperty("units")
    @NonNull
    private String units;

    @JsonProperty("level1")
    private String level1;

    @JsonProperty("level2")
    private String level2;

    @JsonProperty("level3")
    private String level3;

    @JsonProperty("level4")
    private String level4;

    @JsonProperty("further_info")
    private String furtherInfo;

    @JsonProperty("percentage")
    private double percentage;

    @JsonProperty("components")
    private List<Entry> components;
}
