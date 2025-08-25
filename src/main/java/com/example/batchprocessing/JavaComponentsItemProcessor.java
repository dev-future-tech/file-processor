package com.example.batchprocessing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class JavaComponentsItemProcessor implements ItemProcessor<JavaFileComponents, JavaFileComponents> {
    private static final Logger log = LoggerFactory.getLogger(JavaComponentsItemProcessor.class);

    @Override
    public JavaFileComponents process(JavaFileComponents item) throws Exception {
        log.info("Processing JavaFileComponents, {}", item.getFileName());
        log.info("Processing JavaFileComponents source, {}", item.getSourceCode());

        //do something with it like add it to neo4j
        return item;
    }
}
