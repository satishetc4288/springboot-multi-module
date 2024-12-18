package org.spring.batch.repository.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.spring.batch.repository.AddressAttributeConverter;

@Entity
@Table(name = "Coffee")
@Data
public class Coffee {

    @Id
    @GeneratedValue
    private Integer coffee_id;

    private String brand;
    private String origin;
    private String characteristics;

    @Convert(converter = AddressAttributeConverter.class)
    @Column(name = "address", length = 300)
    private Address address;
}
