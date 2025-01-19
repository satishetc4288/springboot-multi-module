package org.spring.batch.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class SpringBatchService {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    public void launchJob( Date date) throws Exception {
        jobLauncher
                .run(
                        job,
                        new JobParametersBuilder()
                                .addDate("launchDate", date)
                                .toJobParameters()
                );
    }
}
