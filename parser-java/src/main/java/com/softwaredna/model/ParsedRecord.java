package com.softwaredna.model;

import java.util.ArrayList;
import java.util.List;

public class ParsedRecord {

    private String name;
    private String id;

    private List<ParsedAnnotation> annotations =
            new ArrayList<>();

    public ParsedRecord() {
    }

    public String getId() {
    return id;
}

public void setId(String id) {
    this.id = id;
}

    public ParsedRecord(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ParsedAnnotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<ParsedAnnotation> annotations) {
        this.annotations = annotations;
    }

}