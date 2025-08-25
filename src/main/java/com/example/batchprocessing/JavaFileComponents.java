package com.example.batchprocessing;

import java.util.ArrayList;
import java.util.List;

public class JavaFileComponents {
    private String fileName;
    private List<String> classes = new ArrayList<>();
    private List<String> methods = new ArrayList<>();
    private List<String> fields = new ArrayList<>();
    private String sourceCode;

    // getters/setters
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public List<String> getClasses() { return classes; }
    public List<String> getMethods() { return methods; }
    public List<String> getFields() { return fields; }
    public String getSourceCode() { return sourceCode; }
    public void setSourceCode(String sourceCode) { this.sourceCode = sourceCode; }
}
