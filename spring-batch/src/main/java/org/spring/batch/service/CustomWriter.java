package org.spring.batch.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.spring.batch.repository.entity.Address;
import org.spring.batch.repository.entity.Coffee;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.PreparedStatement;

@Component
@Slf4j
public class CustomWriter implements ItemWriter<Coffee> {
    private static final String SQL = "INSERT INTO coffee (brand, origin, characteristics, address) VALUES (?,?,?,?);";

    @Autowired
    private DataSource dataSource;

    @Override
    public void write(Chunk<? extends Coffee> list) throws Exception {
        PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(SQL);
        for (Coffee identity : list) {
            // Set the variables
            preparedStatement.setString(1, identity.getBrand());
            preparedStatement.setString(2, identity.getOrigin());
            preparedStatement.setString(3, identity.getCharacteristics());
            preparedStatement.setString(4, convertToDatabaseColumn(identity.getAddress()));
            // Add it to the batch
            preparedStatement.addBatch();

        }
        preparedStatement.executeBatch();
    }

    public String convertToDatabaseColumn(Address address) {
        final ObjectMapper objectMapper = new ObjectMapper();
        log.info("################## address: " + address);
        try {
            return objectMapper.writeValueAsString(address);
        } catch (JsonProcessingException jpe) {
            log.warn("Cannot convert Address into JSON");
            return null;
        }
    }
}
