package com.softwaredna.model;

import java.util.ArrayList;
import java.util.List;

public class ParsedFile {

    private String packageName;

    private List<String> imports;

    private List<ParsedClass> classes;

    public ParsedFile() {

        imports = new ArrayList<>();
        classes = new ArrayList<>();

    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<String> getImports() {
        return imports;
    }

    public void setImports(List<String> imports) {
        this.imports = imports;
    }

    public List<ParsedClass> getClasses() {
        return classes;
    }

    public void setClasses(List<ParsedClass> classes) {
        this.classes = classes;
    }

}