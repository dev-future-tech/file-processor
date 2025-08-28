package com.example.javaingestion;

import com.example.antlr.Java20Lexer;
import com.example.antlr.Java20Parser;
import com.example.classingestion.JavaFileComponents;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class JavaComponentsItemProcessor implements ItemProcessor<JavaFileComponents, JavaFileComponents> {
    private static final Logger log = LoggerFactory.getLogger(JavaComponentsItemProcessor.class);

    @Override
    public JavaFileComponents process(final JavaFileComponents item) throws Exception {

        log.info("Processing JavaFileComponents, {}", item.getFileName());
        log.info("Processing JavaFileComponents source, {}", item.getSourceCode());

        CharStream cs = CharStreams.fromString(item.getSourceCode());
        Java20Lexer lexer = new Java20Lexer(cs);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        Java20Parser parser = new Java20Parser(tokens);
        ParseTree tree = parser.compilationUnit();
        JavaComponentVisitor visitor = new JavaComponentVisitor();
        JavaFileComponents components = visitor.visit(tree);
        components.setFileName(item.getFileName());
        components.setSourceCode(item.getSourceCode());

        return components;
    }
}
