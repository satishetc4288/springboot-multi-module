package org.spring.batch.controller;

import lombok.AllArgsConstructor;
import org.spring.batch.repository.CoffeeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jpa")
@AllArgsConstructor
public class JpaController {
    private final CoffeeRepository coffeeRepository;

    @GetMapping("/get/coffee")
    public ResponseEntity<?> getCoffeeList() throws Exception {
        return ResponseEntity.ok(coffeeRepository.findAll());
    }
}
