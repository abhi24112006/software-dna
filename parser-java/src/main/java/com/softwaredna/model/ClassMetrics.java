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

    // ==========================
    // Cyclomatic Complexity
    // ==========================

    private int totalCyclomaticComplexity;
    private double averageCyclomaticComplexity;
    private int maximumCyclomaticComplexity;

    // ==========================
    // Loop Count
    // ==========================

    private int totalLoopCount;
    private double averageLoopCount;
    private int maximumLoopCount;

    // ==========================
    // Conditional Count
    // ==========================

    private int totalConditionalCount;
    private double averageConditionalCount;
    private int maximumConditionalCount;

    // ==========================
    // Maximum Nesting Depth
    // ==========================

    private int maximumNestingDepth;

    private int fanOut;
    private int fanIn;
    private int cbo;
    private int dit;
    private int noc;

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

    public int getTotalCyclomaticComplexity() {
        return totalCyclomaticComplexity;
    }

    public void setTotalCyclomaticComplexity(int totalCyclomaticComplexity) {
        this.totalCyclomaticComplexity = totalCyclomaticComplexity;
    }

    public double getAverageCyclomaticComplexity() {
        return averageCyclomaticComplexity;
    }

    public void setAverageCyclomaticComplexity(double averageCyclomaticComplexity) {
        this.averageCyclomaticComplexity = averageCyclomaticComplexity;
    }

    public int getMaximumCyclomaticComplexity() {
        return maximumCyclomaticComplexity;
    }

    public void setMaximumCyclomaticComplexity(int maximumCyclomaticComplexity) {
        this.maximumCyclomaticComplexity = maximumCyclomaticComplexity;
    }

    public int getTotalLoopCount() {
        return totalLoopCount;
    }

    public void setTotalLoopCount(int totalLoopCount) {
        this.totalLoopCount = totalLoopCount;
    }

    public double getAverageLoopCount() {
        return averageLoopCount;
    }

    public void setAverageLoopCount(double averageLoopCount) {
        this.averageLoopCount = averageLoopCount;
    }

    public int getMaximumLoopCount() {
        return maximumLoopCount;
    }

    public void setMaximumLoopCount(int maximumLoopCount) {
        this.maximumLoopCount = maximumLoopCount;
    }

    public int getTotalConditionalCount() {
        return totalConditionalCount;
    }

    public void setTotalConditionalCount(int totalConditionalCount) {
        this.totalConditionalCount = totalConditionalCount;
    }

    public double getAverageConditionalCount() {
        return averageConditionalCount;
    }

    public void setAverageConditionalCount(double averageConditionalCount) {
        this.averageConditionalCount = averageConditionalCount;
    }

    public int getMaximumConditionalCount() {
        return maximumConditionalCount;
    }

    public void setMaximumConditionalCount(int maximumConditionalCount) {
        this.maximumConditionalCount = maximumConditionalCount;
    }

    public int getMaximumNestingDepth() {
        return maximumNestingDepth;
    }

    public void setMaximumNestingDepth(int maximumNestingDepth) {
        this.maximumNestingDepth = maximumNestingDepth;
    }

    public int getFanOut() {
        return fanOut;
    }

    public void setFanOut(int fanOut) {
        this.fanOut = fanOut;
    }

    public int getFanIn() {
    return fanIn;
}

public void setFanIn(int fanIn) {
    this.fanIn = fanIn;
}

public int getCbo() {
    return cbo;
}

public void setCbo(int cbo) {
    this.cbo = cbo;
}

public int getDit() {
    return dit;
}

public void setDit(int dit) {
    this.dit = dit;
}

public int getNoc() {
    return noc;
}

public void setNoc(int noc) {
    this.noc = noc;
}
}