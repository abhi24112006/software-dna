package com.softwaredna.model;

public class ParsedMethod {

    private String name;

    private String returnType;

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

}