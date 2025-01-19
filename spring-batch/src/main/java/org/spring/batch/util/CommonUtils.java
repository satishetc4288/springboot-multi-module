package org.spring.batch.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.spring.batch.repository.entity.Address;

@Slf4j
public class CommonUtils {

    public static Address stringToObject(String addressJson) {
        Address address = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            address = mapper.readValue(addressJson, Address.class);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing JSON address", e);
        }
        return address;
    }

    public static String objectToString(Address address) {
        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(address);
        } catch (JsonProcessingException jpe) {
            log.warn("Cannot convert Address into JSON");
        }
        return null;
    }
}
