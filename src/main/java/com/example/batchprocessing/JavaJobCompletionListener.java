package com.example.batchprocessing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JavaJobCompletionListener implements JobExecutionListener {
    private static final Logger log = LoggerFactory.getLogger(JavaJobCompletionListener.class);

    private final JdbcTemplate template;
    private final JdbcTemplate jdbcTemplate;

    public JavaJobCompletionListener(JdbcTemplate template, JdbcTemplate jdbcTemplate) {
        this.template = template;
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("Job completed successfully");
            jdbcTemplate
                    .query("SELECT filename, content from source_code", new DataClassRowMapper<>(JavaFileComponents.class))
                    .forEach(source -> log.info("Found <{}>, <{}> in the database", source.getFileName(), source.getSourceCode()));
        } else {
            log.info("Job completed with status " + jobExecution.getStatus());
        }
    }
}
