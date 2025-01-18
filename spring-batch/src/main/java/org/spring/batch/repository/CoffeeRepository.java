package org.spring.batch.repository;

import org.spring.batch.repository.entity.Coffee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoffeeRepository extends JpaRepository<Coffee, Integer> {

}
