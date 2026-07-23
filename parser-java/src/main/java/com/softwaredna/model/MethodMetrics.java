package com.softwaredna.model;

public class MethodMetrics {

    private int linesOfCode;

    private int cyclomaticComplexity;

    private int parameterCount;

    private int localVariableCount;

    private int methodCallCount;

    private int objectCreationCount;

    private int loopCount;

    private int conditionalCount;

    private int returnCount;

    private int maximumNestingDepth;

    public MethodMetrics() {

    }

    public int getLinesOfCode() {
        return linesOfCode;
    }

    public void setLinesOfCode(int linesOfCode) {
        this.linesOfCode = linesOfCode;
    }

    public int getCyclomaticComplexity() {
        return cyclomaticComplexity;
    }

    public void setCyclomaticComplexity(int cyclomaticComplexity) {
        this.cyclomaticComplexity = cyclomaticComplexity;
    }

    public int getParameterCount() {
        return parameterCount;
    }

    public void setParameterCount(int parameterCount) {
        this.parameterCount = parameterCount;
    }

    public int getLocalVariableCount() {
        return localVariableCount;
    }

    public void setLocalVariableCount(int localVariableCount) {
        this.localVariableCount = localVariableCount;
    }

    public int getMethodCallCount() {
        return methodCallCount;
    }

    public void setMethodCallCount(int methodCallCount) {
        this.methodCallCount = methodCallCount;
    }

    public int getObjectCreationCount() {
        return objectCreationCount;
    }

    public void setObjectCreationCount(int objectCreationCount) {
        this.objectCreationCount = objectCreationCount;
    }

    public int getLoopCount() {
        return loopCount;
    }

    public void setLoopCount(int loopCount) {
        this.loopCount = loopCount;
    }

    public int getConditionalCount() {
        return conditionalCount;
    }

    public void setConditionalCount(int conditionalCount) {
        this.conditionalCount = conditionalCount;
    }

    public int getReturnCount() {
        return returnCount;
    }

    public void setReturnCount(int returnCount) {
        this.returnCount = returnCount;
    }

    public int getMaximumNestingDepth() {
        return maximumNestingDepth;
    }

    public void setMaximumNestingDepth(int maximumNestingDepth) {
        this.maximumNestingDepth = maximumNestingDepth;
    }


}