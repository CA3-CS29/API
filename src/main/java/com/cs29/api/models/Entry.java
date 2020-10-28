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
@Document(collection = "entries")
@Accessors(chain = true)
@JsonDeserialize(builder = Entry.EntryBuilder.class)
public class Entry {
    @Id
    @JsonProperty("entry_id")
    @NonNull
    private String entry_id;

    @JsonProperty("office_id")
    @NonNull
    private String office_id;

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
    private String further_info;

    @JsonProperty("percentage")
    private double percentage;

    @JsonProperty("components")
    private List<Entry> components;
}
