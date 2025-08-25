package com.example.batchprocessing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;

@Component
public class JavaFileReader implements ItemReader<JavaFileComponents> {
    private final Logger log = LoggerFactory.getLogger(JavaFileReader.class);
    private final Iterator<Path> fileIterator;

    public JavaFileReader(List<Path> javaFiles) {
        this.fileIterator = javaFiles.iterator();
    }
    @Override
    public JavaFileComponents read() throws Exception {

        if (!fileIterator.hasNext()) {
            return null; // signals end of input
        }

        Path file = fileIterator.next();
        log.debug("Reading file at {}", file.toString());
        String content = Files.readString(file);

        JavaFileComponents javaFileComponents = new JavaFileComponents();
        javaFileComponents.setFileName(file.getFileName().toString());
        javaFileComponents.setSourceCode(content);
        return javaFileComponents;
    }
}
