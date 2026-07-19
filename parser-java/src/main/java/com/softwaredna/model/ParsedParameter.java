package com.softwaredna.model;

import java.util.ArrayList;
import java.util.List;

public class ParsedParameter {

    private String name;
    private String id;

    private String type;

    private List<ParsedAnnotation> annotations =
            new ArrayList<>();

    public ParsedParameter() {
    }

    public String getId() {
    return id;
}

public void setId(String id) {
    this.id = id;
}

    public ParsedParameter(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ParsedAnnotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(
            List<ParsedAnnotation> annotations) {
        this.annotations = annotations;
    }

}