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
@Document(collection = "offices")
@Accessors(chain = true)
@JsonDeserialize(builder = Office.OfficeBuilder.class)
public class Office {
    @Id
    @JsonProperty("office_id")
    @NonNull
    private String office_id;

    @JsonProperty("user_id")
    @NonNull
    private List<String> user_id;

    @JsonProperty("region_id")
    @NonNull
    private String region_id;

    @JsonProperty("name")
    @NonNull
    private String name;

    @JsonProperty("num_entries")
    private int num_entries;

    @JsonProperty("entries")
    private List<Entry> entries;
}
