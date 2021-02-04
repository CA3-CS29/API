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
