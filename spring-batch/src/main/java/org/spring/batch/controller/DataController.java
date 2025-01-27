package org.spring.batch.controller;

import lombok.AllArgsConstructor;
import org.spring.batch.service.SpringBatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/batch")
@AllArgsConstructor
public class DataController {

    private final SpringBatchService springBatchService;

    @GetMapping("/data")
    public ResponseEntity<?> getBatchData() throws Exception {
        springBatchService.launchJob(new Date());
        return ResponseEntity.ok("");
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }

}
