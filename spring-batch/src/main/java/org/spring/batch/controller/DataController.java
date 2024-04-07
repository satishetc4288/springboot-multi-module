package org.spring.batch.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/batch")
public class DataController {

    @GetMapping("/data")
    public ResponseEntity<?> getBatchData(){
        return ResponseEntity.ok("");
    }

}
