package com.example.batchprocessing;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;

public class AntlrItemReader implements ItemReader<JavaFileComponents> {

    private final Iterator<Path> fileIterator;

    public AntlrItemReader(List<Path> javaFiles) {
        this.fileIterator = javaFiles.iterator();
    }

    @Override
    public JavaFileComponents read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        return null;
    }
}
