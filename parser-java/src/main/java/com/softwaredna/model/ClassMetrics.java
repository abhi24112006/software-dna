package com.softwaredna.model;

public class ClassMetrics {

    private int fieldCount;
    private int constructorCount;
    private int methodCount;

    private int publicMethodCount;
    private int privateMethodCount;
    private int protectedMethodCount;

    private int totalLinesOfCode;
    private int averageMethodLinesOfCode;

    private int totalParameters;
    private int totalLocalVariables;
    private int totalMethodCalls;
    private int totalObjectCreations;
    private int totalReturnStatements;

    public int getFieldCount() {
        return fieldCount;
    }

    public void setFieldCount(int fieldCount) {
        this.fieldCount = fieldCount;
    }

    public int getConstructorCount() {
        return constructorCount;
    }

    public void setConstructorCount(int constructorCount) {
        this.constructorCount = constructorCount;
    }

    public int getMethodCount() {
        return methodCount;
    }

    public void setMethodCount(int methodCount) {
        this.methodCount = methodCount;
    }

    public int getPublicMethodCount() {
        return publicMethodCount;
    }

    public void setPublicMethodCount(int publicMethodCount) {
        this.publicMethodCount = publicMethodCount;
    }

    public int getPrivateMethodCount() {
        return privateMethodCount;
    }

    public void setPrivateMethodCount(int privateMethodCount) {
        this.privateMethodCount = privateMethodCount;
    }

    public int getProtectedMethodCount() {
        return protectedMethodCount;
    }

    public void setProtectedMethodCount(int protectedMethodCount) {
        this.protectedMethodCount = protectedMethodCount;
    }

    public int getTotalLinesOfCode() {
        return totalLinesOfCode;
    }

    public void setTotalLinesOfCode(int totalLinesOfCode) {
        this.totalLinesOfCode = totalLinesOfCode;
    }

    public int getAverageMethodLinesOfCode() {
        return averageMethodLinesOfCode;
    }

    public void setAverageMethodLinesOfCode(int averageMethodLinesOfCode) {
        this.averageMethodLinesOfCode = averageMethodLinesOfCode;
    }

    public int getTotalParameters() {
        return totalParameters;
    }

    public void setTotalParameters(int totalParameters) {
        this.totalParameters = totalParameters;
    }

    public int getTotalLocalVariables() {
        return totalLocalVariables;
    }

    public void setTotalLocalVariables(int totalLocalVariables) {
        this.totalLocalVariables = totalLocalVariables;
    }

    public int getTotalMethodCalls() {
        return totalMethodCalls;
    }

    public void setTotalMethodCalls(int totalMethodCalls) {
        this.totalMethodCalls = totalMethodCalls;
    }

    public int getTotalObjectCreations() {
        return totalObjectCreations;
    }

    public void setTotalObjectCreations(int totalObjectCreations) {
        this.totalObjectCreations = totalObjectCreations;
    }

    public int getTotalReturnStatements() {
        return totalReturnStatements;
    }

    public void setTotalReturnStatements(int totalReturnStatements) {
        this.totalReturnStatements = totalReturnStatements;
    }
}