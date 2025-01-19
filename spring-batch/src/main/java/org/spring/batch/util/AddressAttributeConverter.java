package org.spring.batch.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;
import org.spring.batch.repository.entity.Address;

@Converter
@Slf4j
public class AddressAttributeConverter implements AttributeConverter<Address, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Address address) {
        log.info("################## address: " + address);
        try {
            return objectMapper.writeValueAsString(address);
        } catch (JsonProcessingException jpe) {
            log.warn("Cannot convert Address into JSON");
            return null;
        }
    }

    @Override
    public Address convertToEntityAttribute(String value) {
        log.info("################## value: " + value);
        try {
            return objectMapper.readValue(value, Address.class);
        } catch (JsonProcessingException e) {
            log.warn("Cannot convert JSON into Address {}", e.getMessage());
            return null;
        }
    }
}
