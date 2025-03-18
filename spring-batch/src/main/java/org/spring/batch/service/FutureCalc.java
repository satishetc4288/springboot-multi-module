package org.spring.batch.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Component
public class FutureCalc {

    public CompletableFuture<String> runAsynch(){
        List<Integer> data = IntStream.range(0,1000).boxed().collect(Collectors.toList());
        List<List<Integer>> list = ListUtils.partition(data, 5);
        list.forEach(list1 -> {
                list1
                .stream()
                .map(elem ->
                    CompletableFuture.runAsync( () -> {
                                log.info("{}, this is thread: {}", elem, Thread.currentThread().getName());
                                threadSleep(5000);
                            }
                    )
                ).toList();
        });
        return CompletableFuture.completedFuture("data");
    }

    private void threadSleep(long seconds){
        try {
            Thread.sleep(seconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
