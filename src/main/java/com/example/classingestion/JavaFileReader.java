package com.example.classingestion;

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
            log.info("No more files to read");
            return null; // signals end of input
        }

        Path file = fileIterator.next();
        log.info("Reading file at {}", file.toString());
        String content = Files.readString(file);
        JavaFileComponents components = new JavaFileComponents();

        components.setFileName(file.getFileName().toString());
        components.setSourceCode(content);
        return components;
    }
}
