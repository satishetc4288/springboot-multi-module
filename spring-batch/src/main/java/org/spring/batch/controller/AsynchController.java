package org.spring.batch.controller;

import org.spring.batch.service.FutureCalc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/customers")
public class AsynchController {

    @Autowired
    private FutureCalc futureCalc;

    @GetMapping(value = "/test")
    public CompletableFuture<String> getString(){
        return futureCalc.runAsynch();
    }
}
