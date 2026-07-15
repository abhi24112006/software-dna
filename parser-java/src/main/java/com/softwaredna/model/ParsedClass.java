package com.softwaredna.model;

import java.util.ArrayList;
import java.util.List;

public class ParsedClass {

    private String name;

    private List<ParsedField> fields;

    private List<ParsedMethod> methods;

    public ParsedClass() {

        fields = new ArrayList<>();
        methods = new ArrayList<>();

    }

    public ParsedClass(String name) {

        this.name = name;

        fields = new ArrayList<>();
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

    public List<ParsedMethod> getMethods() {
        return methods;
    }

    public void setMethods(List<ParsedMethod> methods) {
        this.methods = methods;
    }

}