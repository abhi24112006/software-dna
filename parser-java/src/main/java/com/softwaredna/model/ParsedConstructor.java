package com.softwaredna.model;

public class ParsedConstructor {

    private String name;

    private int parameterCount;

    public ParsedConstructor() {
    }

    public ParsedConstructor(String name, int parameterCount) {

        this.name = name;
        this.parameterCount = parameterCount;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParameterCount() {
        return parameterCount;
    }

    public void setParameterCount(int parameterCount) {
        this.parameterCount = parameterCount;
    }

}