package com.cs29.api.dtos;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EntryDtoTest {

    private final ObjectMapper MAPPER = new ObjectMapper();
    private final String TEST_ID = "test_id";
    private final String TEST_TAG = "test_tag";
    private final double TEST_DOUBLE = 10.5;
    private final String TEST_SOURCE = "test_source";
    private final String TEST_UNITS = "test_units";
    private final String TEST_LEVEL = "test_level";
    private final String TEST_FURTHER_INFO = "test_further_info";
    private final EntryDto TEST_ENTRY_DTO = EntryDto.builder()
            .entryId(TEST_ID)
            .officeId(TEST_ID)
            .tag(TEST_TAG)
            .consumption(TEST_DOUBLE)
            .original(TEST_DOUBLE)
            .converted(TEST_DOUBLE)
            .source(TEST_SOURCE)
            .units(TEST_UNITS)
            .level1(TEST_LEVEL)
            .level2(TEST_LEVEL)
            .level3(TEST_LEVEL)
            .level4(TEST_LEVEL)
            .furtherInfo(TEST_FURTHER_INFO)
            .components(null)
            .build();
    private final String TEST_SERIALIZED_ENTRY_DTO = "{\"entry_id\":\"test_id\",\"office_id\":\"test_id\",\"tag\":" +
            "\"test_tag\",\"consumption\":10.5,\"original\":10.5,\"converted\":10.5,\"source\":\"test_source\"," +
            "\"units\":\"test_units\",\"level1\":\"test_level\",\"level2\":\"test_level\",\"level3\":\"test_level\"," +
            "\"level4\":\"test_level\",\"further_info\":\"test_further_info\",\"percentage\":0.0}";

    @SneakyThrows
    @Test
    public void givenEntryDtoSerializationSucceeds() {
        String serializedEntryDto = MAPPER.writeValueAsString(TEST_ENTRY_DTO);
        assertEquals(serializedEntryDto, TEST_SERIALIZED_ENTRY_DTO);
    }

    @SneakyThrows
    @Test
    public void givenEntryDtoJsonDeserializeSucceeds() {
        EntryDto entryDto = MAPPER.readValue(TEST_SERIALIZED_ENTRY_DTO, EntryDto.class);
        assertEquals(entryDto, TEST_ENTRY_DTO);
    }
}
