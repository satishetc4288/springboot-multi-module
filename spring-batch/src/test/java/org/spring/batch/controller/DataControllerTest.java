package org.spring.batch.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.spring.batch.service.SpringBatchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@ExtendWith(MockitoExtension.class)
public class DataControllerTest {

    @Mock
    private SpringBatchService springBatchService;

    @InjectMocks
    private DataController dataController;

//    @BeforeEach
//    void init_mocks() {
//        MockitoAnnotations.openMocks(this);
//    }

    @Test
    public void test_getBatchData() throws Exception {
        Mockito.doNothing().when(springBatchService).launchJob(Mockito.any());
        ResponseEntity<?> responseEntity = dataController.getBatchData();
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

}
