package com.example.batchprocessing;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Types;
import java.util.List;

@Configuration
public class BatchConfiguration {

    @Bean
    public FlatFileItemReader<Person> flatFileItemReaderBuilder() {
        return new FlatFileItemReaderBuilder<Person>()
                .name("personItemReader")
                .resource(new ClassPathResource("sample-data.csv"))
                .delimited()
                .names("firstName", "lastName")
                .targetType(Person.class)
                .build();
    }

    @Bean
    public JavaFileReader reader() throws IOException {
        List<Path> javaFiles = Files.walk(Paths.get("src/main/java"))
                .filter(p -> p.toString().endsWith(".java"))
                .toList();
        return new JavaFileReader(javaFiles);
    }

    @Bean
    public PersonItemProcessor personItemProcessor() {
        return new PersonItemProcessor();
    }

    @Bean
    public JavaComponentsItemProcessor javaComponentsItemProcessor() {
        return new JavaComponentsItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Person> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Person>()
                .sql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)")
                .dataSource(dataSource)
                .beanMapped()
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<JavaFileComponents> javaWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<JavaFileComponents>()
                .sql("INSERT INTO source_code (filename, content) values (:fileName, :sourceCode)")
                .itemSqlParameterSourceProvider(item -> {
                    MapSqlParameterSource params = new MapSqlParameterSource();
                    params.addValue("filename", item.getFileName());
                    params.addValue("content", item.getSourceCode(), Types.CLOB); // Specify Types.CLOB
                    return params;
                })
                .dataSource(dataSource)
                .beanMapped()
                .build();
    }

    @Bean
    public Job sourceCodeReaderJob(JobRepository jobRepository, Step javaStep, JavaJobCompletionListener listener) {
        return new JobBuilder("ingestJavaCode", jobRepository)
                .listener(listener)
                .start(javaStep)
                .build();
    }

    @Bean
    public Step javaStep(JobRepository jobRepository, DataSourceTransactionManager transactionManager,
                         JavaFileReader reader, JavaComponentsItemProcessor javaItemProcessor, JdbcBatchItemWriter<JavaFileComponents> javaWriter) {
        return new StepBuilder("ingest", jobRepository)
                .<JavaFileComponents, JavaFileComponents>chunk(3, transactionManager)
                .reader(reader)
                .processor(javaItemProcessor)
                .writer(javaWriter)
                .build();
    }

    @Bean
    public Job importUserJob(JobRepository jobRepository, Step step1, JobCompletionNotificationListener listener) {
        return new JobBuilder("importUserJob", jobRepository)
                .listener(listener)
                .start(step1)
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, DataSourceTransactionManager transactionManager, FlatFileItemReader<Person> reader, PersonItemProcessor personItemProcessor,
                      PersonItemProcessor processor, JdbcBatchItemWriter<Person> writer) {
        return new StepBuilder("step1", jobRepository)
                .<Person, Person>chunk(3, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}
