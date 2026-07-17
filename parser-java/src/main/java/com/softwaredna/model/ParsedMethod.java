package com.softwaredna.model;

import java.util.ArrayList;
import java.util.List;

public class ParsedMethod {

    private String name;

    private String returnType;

    private List<ParsedParameter> parameters =
            new ArrayList<>();

    private List<ParsedAnnotation> annotations =
            new ArrayList<>();

    public ParsedMethod() {
    }

    public ParsedMethod(String name, String returnType) {

        this.name = name;
        this.returnType = returnType;

    }

    public String getName() {

        return name;

    }

    public void setName(String name) {

        this.name = name;

    }

    public String getReturnType() {

        return returnType;

    }

    public void setReturnType(String returnType) {

        this.returnType = returnType;

    }

    public List<ParsedParameter> getParameters() {

        return parameters;

    }

    public void setParameters(
            List<ParsedParameter> parameters) {

        this.parameters = parameters;

    }

    public List<ParsedAnnotation> getAnnotations() {

        return annotations;

    }

    public void setAnnotations(
            List<ParsedAnnotation> annotations) {

        this.annotations = annotations;

    }

}