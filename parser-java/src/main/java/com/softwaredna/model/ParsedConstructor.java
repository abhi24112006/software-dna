package com.softwaredna.model;

import java.util.ArrayList;
import java.util.List;

public class ParsedConstructor {

    private String name;
    private String id;

    private List<ParsedParameter> parameters =
            new ArrayList<>();

    private List<ParsedAnnotation> annotations =
            new ArrayList<>();

    public ParsedConstructor() {
    }

    public String getId() {
    return id;
}

public void setId(String id) {
    this.id = id;
}

    public ParsedConstructor(String name) {

        this.name = name;

    }

    public String getName() {

        return name;

    }

    public void setName(String name) {

        this.name = name;

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