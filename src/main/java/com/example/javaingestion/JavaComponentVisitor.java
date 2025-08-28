package com.example.javaingestion;

import com.example.antlr.Java20Parser;
import com.example.antlr.Java20ParserBaseVisitor;
import com.example.classingestion.JavaFileComponents;

public class JavaComponentVisitor extends Java20ParserBaseVisitor<JavaFileComponents> {

    private final JavaFileComponents components = new JavaFileComponents();

    @Override
    public JavaFileComponents visitCompilationUnit(Java20Parser.CompilationUnitContext ctx) {
        super.visitCompilationUnit(ctx);
        return components;
    }

    @Override
    public JavaFileComponents visitClassDeclaration(Java20Parser.ClassDeclarationContext ctx) {
        if (ctx.normalClassDeclaration() != null) {
            String className = ctx.normalClassDeclaration().getText();
            components.getClasses().add(className);
        } else if (ctx.enumDeclaration() != null) {
            String enumName = ctx.enumDeclaration().getText();
            components.getClasses().add(enumName);
        }

        return super.visitClassDeclaration(ctx);
    }

    @Override
    public JavaFileComponents visitMethodDeclaration(Java20Parser.MethodDeclarationContext ctx) {

        components.getMethods().add(ctx.methodHeader().methodDeclarator().identifier().getText());
        return super.visitMethodDeclaration(ctx);
    }
}

