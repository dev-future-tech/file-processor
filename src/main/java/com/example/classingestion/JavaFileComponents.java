package com.example.classingestion;

import java.util.ArrayList;
import java.util.List;

public class JavaFileComponents {
    private String fileName;
    private String packageName;
    private final List<String> classes = new ArrayList<>();
    private final List<String> methods = new ArrayList<>();
    private final List<String> fields = new ArrayList<>();
    private String sourceCode;
    private String classType;

    // getters/setters
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }
    public List<String> getClasses() { return classes; }
    public List<String> getMethods() { return methods; }
    public List<String> getFields() { return fields; }
    public String getSourceCode() { return sourceCode; }
    public void setSourceCode(String sourceCode) { this.sourceCode = sourceCode; }
    public String getClassType() { return classType; }
    public void setClassType(String classType) { this.classType = classType; }
}
