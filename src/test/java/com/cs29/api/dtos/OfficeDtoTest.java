package com.cs29.api.dtos;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OfficeDtoTest {

    private final ObjectMapper MAPPER = new ObjectMapper();
    private final List<String> TEST_ID = Arrays.asList("TEST_ID1", "TEST_ID2");
    private final String TEST_NAME = "TEST_NAME";
    private final int TEST_NUM_ENTRIES = 0;
    private final OfficeDto TEST_OFFICE_DTO = OfficeDto
            .builder()
            .officeId(TEST_NAME)
            .regionId(TEST_NAME)
            .userId(TEST_ID)
            .name(TEST_NAME)
            .numEntries(TEST_NUM_ENTRIES)
            .entries(null)
            .build();
    private final String TEST_SERIALIZED_OFFICE_DTO = "{\"office_id\":\"TEST_NAME\",\"user_id\":[\"TEST_ID1\",\"TEST_ID2\"]," +
            "\"region_id\":\"TEST_NAME\",\"name\":\"TEST_NAME\",\"num_entries\":0}";

    @SneakyThrows
    @Test
    public void givenOfficeDtoSerializationSucceeds() {
        String serializedOffice = MAPPER.writeValueAsString(TEST_OFFICE_DTO);
        assertEquals(serializedOffice, TEST_SERIALIZED_OFFICE_DTO);
    }

    @SneakyThrows
    @Test
    public void givenOfficeDtoJsonDeserializeSucceeds() {
        OfficeDto officeDto = MAPPER.readValue(TEST_SERIALIZED_OFFICE_DTO, OfficeDto.class);
        assertEquals(officeDto, TEST_OFFICE_DTO);
    }
}
