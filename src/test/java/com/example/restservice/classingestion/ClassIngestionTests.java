package com.example.restservice.classingestion;

import com.example.antlr.Java20Lexer;
import com.example.antlr.Java20Parser;
import com.example.classingestion.JavaFileComponents;
import com.example.javaingestion.JavaComponentVisitor;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassIngestionTests {

    private final Logger log = LoggerFactory.getLogger(ClassIngestionTests.class);

    @Test
    public void testJavaFileRead() {
        JavaFileComponents item = new JavaFileComponents();
        item.setFileName("src/test/java/com/example/test/TestClass.java");
        item.setSourceCode("""
                package com.example.test;
                
                public class TestClass{
                    public void testMethod(){
                    }
                }
                """);

        item.setFileName("src/main/java/com/example/values/TestEnum.java");
        item.setSourceCode("""
                package com.example.values;
                
                public enum TestEnum {
                    SHOES,
                    CLOTHES,
                    ACCESSORIES;
                }
                """);

        item.setFileName("src/main/java/com/example/values/TestRecord.java");
        item.setSourceCode("""
                package com.example.values;
                
                record TestRecord(String name, int age) {}
                """);

        CharStream cs = CharStreams.fromString(item.getSourceCode());
        Java20Lexer lexer = new Java20Lexer(cs);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        Java20Parser parser = new Java20Parser(tokens);
        ParseTree tree = parser.compilationUnit();
        JavaComponentVisitor visitor = new JavaComponentVisitor();
        JavaFileComponents components = visitor.visit(tree);

        log.info("File name is {}", components.getFileName());
        log.info("package name is {}", components.getPackageName());
        log.info("class type is {}", components.getClassType());
        components.getClasses().forEach(c -> log.info("Class: {}", c));
        components.getMethods().forEach(m -> log.info("Method: {}", m));

    }

    @Test
    public void testCobolParsing() {

    }
}
