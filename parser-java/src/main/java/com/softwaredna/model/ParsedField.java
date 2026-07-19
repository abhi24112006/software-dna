package com.softwaredna.model;

public class ParsedField {

    private String name;

    private String type;

    private String id;

    public ParsedField() {
    }

    public ParsedField(String name, String type) {

        this.name = name;
        this.type = type;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}