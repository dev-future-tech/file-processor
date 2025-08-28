package com.example.classingestion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Component;

import java.io.StringReader;

@Component
public class SavingJavaItemWriter implements ItemWriter<JavaFileComponents> {

    private final Logger logger = LoggerFactory.getLogger(SavingJavaItemWriter.class);

    private final JdbcTemplate template;
    private final JdbcTemplate jdbcTemplate;


    public SavingJavaItemWriter(JdbcTemplate template, JdbcTemplate jdbcTemplate) {
        this.template = template;
        this.jdbcTemplate = jdbcTemplate;
    }

    private StepExecution stepExecution;

    @BeforeStep
    public void saveStepExecutions(StepExecution stepExecution) throws JobExecutionException {
        this.stepExecution = stepExecution;
    }

    @Override
    public void write(Chunk<? extends JavaFileComponents> chunk) throws Exception {
        ExecutionContext stepContext = this.stepExecution.getExecutionContext();

        chunk.getItems().forEach(javaItem -> {
            logger.info("Filename is {}", javaItem.getFileName());
            stepContext.put("methods", javaItem.getMethods());
            stepContext.put("classes",  javaItem.getClasses());

            javaItem.getClasses().forEach(javaClass -> {
                logger.info("Classname is {}", javaClass);
            });

            javaItem.getMethods().forEach(javaMethod -> {
                logger.info("Method is {}", javaMethod);
            });
            String sql = "INSERT INTO source_code (filename, content) VALUES (?, ?)";
            PreparedStatementCreator psc = con -> con.prepareStatement(sql);
            jdbcTemplate.execute(psc,
                    pstmt -> {
                        pstmt.setString(1, javaItem.getFileName());
                        pstmt.setCharacterStream(2, new StringReader(javaItem.getSourceCode()));
                        return pstmt;
                    });

        });

    }
}
