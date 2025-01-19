package org.spring.batch.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.batch.repository.entity.Coffee;
import org.spring.batch.util.CommonUtils;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JobNotificationListener implements JobExecutionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobNotificationListener.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JobNotificationListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            LOGGER.info("!!! JOB FINISHED! Time to verify the results");

            String query = "SELECT brand, origin, characteristics, address FROM coffee";
            jdbcTemplate.query(query, (rs, row) ->
                Coffee.builder()
                    .brand(rs.getString(1))
                    .origin(rs.getString(2))
                    .characteristics(rs.getString(3))
                    .address(CommonUtils.stringToObject(rs.getString(4)))
                    .build())
                .forEach(coffee -> LOGGER.info("Found < {} > in the database.", coffee));
        }
    }
}
