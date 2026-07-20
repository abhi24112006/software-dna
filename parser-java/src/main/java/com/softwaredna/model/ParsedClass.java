package com.softwaredna.model;

import java.util.ArrayList;
import java.util.List;

public class ParsedClass {

    private String id;

    private String name;

    private String packageName;

    private String superClass;

    private List<String> implementedInterfaces;

    private List<ParsedField> fields;

    private List<ParsedConstructor> constructors;

    private List<ParsedMethod> methods;

    private List<ParsedAnnotation> annotations;

    public ParsedClass() {

        implementedInterfaces = new ArrayList<>();
        fields = new ArrayList<>();
        constructors = new ArrayList<>();
        methods = new ArrayList<>();
        annotations = new ArrayList<>();

    }

    public ParsedClass(String name) {

        this();

        this.name = name;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getSuperClass() {
        return superClass;
    }

    public void setSuperClass(String superClass) {
        this.superClass = superClass;
    }

    public List<String> getImplementedInterfaces() {
        return implementedInterfaces;
    }

    public void setImplementedInterfaces(List<String> implementedInterfaces) {
        this.implementedInterfaces = implementedInterfaces;
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