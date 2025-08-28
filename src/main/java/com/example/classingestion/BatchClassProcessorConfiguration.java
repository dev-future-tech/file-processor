package com.example.classingestion;

import com.example.javaingestion.JavaComponentsItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Configuration
public class BatchClassProcessorConfiguration {

    @Bean
    public ItemReader<JavaFileComponents> reader() throws IOException {
        List<Path> javaFiles = Files.walk(Paths.get("src/main/java"))
                .filter(p -> p.toString().endsWith(".java"))
                .toList();
        System.out.println("Found " + javaFiles.size() + " java files");
        return new JavaFileReader(javaFiles);
    }

    @Bean
    public JavaComponentsItemProcessor sampleProcessor() {
        return new JavaComponentsItemProcessor();
    }


    @Bean
    public Step writeJavaStep(JobRepository jobRepository,
                              JavaComponentsItemProcessor sampleProcessor,
                              SavingJavaItemWriter savingJavaItemWriter,
                              DataSourceTransactionManager transactionManager, JavaFileReader reader) {
        return new StepBuilder("writeJavaStep", jobRepository)
                .<JavaFileComponents, JavaFileComponents>chunk(3, transactionManager)
                .reader(reader)
                .processor(sampleProcessor)
                .writer(savingJavaItemWriter)
                .build();
    }

    @Bean
    public Job recreateClassesJob(JobRepository jobRepository, JavaJobCompletionListener listener, Step writeJavaStep) {
        return new JobBuilder("processJavaCode3", jobRepository)
                .listener(listener)
                .start(writeJavaStep)
                .build();
    }

}
