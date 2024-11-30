package org.spring.batch;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;


public class ApplicationTest {

    @InjectMocks
    private Application application;

    @Test
    public void testInit(){
        Assertions.assertTrue(true);
    }

    @Test
    public void test_Application(){
        application.main(new String[]{});
    }

}
