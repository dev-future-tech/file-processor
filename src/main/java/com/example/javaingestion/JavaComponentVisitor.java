package com.example.javaingestion;

import com.example.antlr.Java20Parser;
import com.example.antlr.Java20ParserBaseVisitor;
import com.example.classingestion.JavaFileComponents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaComponentVisitor extends Java20ParserBaseVisitor<JavaFileComponents> {

    private final Logger log = LoggerFactory.getLogger(JavaComponentVisitor.class);

    private final JavaFileComponents components = new JavaFileComponents();

    @Override
    public JavaFileComponents visitCompilationUnit(Java20Parser.CompilationUnitContext ctx) {
        super.visitCompilationUnit(ctx);
        return components;
    }

    @Override
    public JavaFileComponents visitPackageDeclaration(Java20Parser.PackageDeclarationContext ctx) {
        StringBuilder packageName = new StringBuilder();
        ctx.identifier().forEach(id -> {
            packageName.append(id.getText()).append(".");
        });

        components.setPackageName(packageName.toString().substring(0, packageName.length() - 1));

        return super.visitPackageDeclaration(ctx);
    }

    @Override
    public JavaFileComponents visitClassDeclaration(Java20Parser.ClassDeclarationContext ctx) {
        if (ctx.normalClassDeclaration() != null) {
            String className = ctx.normalClassDeclaration().typeIdentifier().getText();
            log.info("Class name is {}", className);
            components.setClassType("class");
            components.getClasses().add(className);
        } else if (ctx.enumDeclaration() != null) {
            String enumName = ctx.enumDeclaration().typeIdentifier().getText();
            components.setClassType("enum");
            components.getClasses().add(enumName);
        } else if (ctx.recordDeclaration() != null) {
            String recordName = ctx.recordDeclaration().typeIdentifier().getText();
            components.setClassType("record");
            components.getClasses().add(recordName);
        }

        return super.visitClassDeclaration(ctx);
    }

    @Override
    public JavaFileComponents visitMethodDeclaration(Java20Parser.MethodDeclarationContext ctx) {

        components.getMethods().add(ctx.methodHeader().methodDeclarator().identifier().getText());
        return super.visitMethodDeclaration(ctx);
    }
}

