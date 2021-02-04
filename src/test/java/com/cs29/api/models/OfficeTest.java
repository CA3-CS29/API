package com.cs29.api.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OfficeTest {
    private final ObjectMapper MAPPER = new ObjectMapper();
    private final String TEST_ID = "TEST_ID1";
    private final String TEST_NAME = "TEST_NAME";
    private final int TEST_NUM_ENTRIES = 0;
    private final Office TEST_OFFICE = Office
            .builder()
            .officeId(TEST_NAME)
            .regionId(TEST_NAME)
            .userId(TEST_ID)
            .name(TEST_NAME)
            .numEntries(TEST_NUM_ENTRIES)
            .entries(null)
            .build();
    private final String TEST_SERIALIZED_OFFICE = "{\"office_id\":\"TEST_NAME\",\"user_id\":\"TEST_ID1\"," +
            "\"region_id\":\"TEST_NAME\",\"name\":\"TEST_NAME\",\"num_entries\":0,\"entries\":null}";

    @SneakyThrows
    @Test
    public void givenOfficeSerializationSucceeds() {
        String serializedOffice = MAPPER.writeValueAsString(TEST_OFFICE);
        assertEquals(serializedOffice, TEST_SERIALIZED_OFFICE);
    }

    @SneakyThrows
    @Test
    public void givenOfficeJsonDeserializeSucceeds() {
        Office office = MAPPER.readValue(TEST_SERIALIZED_OFFICE, Office.class);
        assertEquals(office, TEST_OFFICE);
    }
}
