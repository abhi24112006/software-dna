package com.softwaredna.parser.language;

public class PythonCall {

    private final String sourceClass;
    private final String sourceMethod;

    private final String receiver;
    private final String targetMethod;

    public PythonCall(
            String sourceClass,
            String sourceMethod,
            String receiver,
            String targetMethod) {

        this.sourceClass = sourceClass;
        this.sourceMethod = sourceMethod;
        this.receiver = receiver;
        this.targetMethod = targetMethod;
    }

    public String getSourceClass() {
        return sourceClass;
    }

    public String getSourceMethod() {
        return sourceMethod;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getTargetMethod() {
        return targetMethod;
    }
}