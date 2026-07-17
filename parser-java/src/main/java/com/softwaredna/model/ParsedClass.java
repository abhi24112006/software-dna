package com.softwaredna.model;
import com.softwaredna.model.ParsedAnnotation;
import java.util.ArrayList;
import java.util.List;

public class ParsedClass {

    private String name;

    private List<ParsedField> fields;

    private List<ParsedConstructor> constructors;

    private List<ParsedMethod> methods;

    private List<ParsedAnnotation> annotations = new ArrayList<>();

    public ParsedClass() {

        fields = new ArrayList<>();
        constructors = new ArrayList<>();
        methods = new ArrayList<>();

    }

    public ParsedClass(String name) {

        this.name = name;

        fields = new ArrayList<>();
        constructors = new ArrayList<>();
        methods = new ArrayList<>();

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ParsedField> getFields() {
        return fields;
    }

    public void setFields(List<ParsedField> fields) {
        this.fields = fields;
    }

    public List<ParsedConstructor> getConstructors() {
        return constructors;
    }

    public void setConstructors(List<ParsedConstructor> constructors) {
        this.constructors = constructors;
    }

    public List<ParsedMethod> getMethods() {
        return methods;
    }

    public void setMethods(List<ParsedMethod> methods) {
        this.methods = methods;
    }

    public List<ParsedAnnotation> getAnnotations() {
    return annotations;
    }

    public void setAnnotations(List<ParsedAnnotation> annotations) {
        this.annotations = annotations;
    }

}