package com.softwaredna.model;

public class ResolvedMethodCall {

    /*
     * The parsed method call from the source code.
     */
    private ParsedMethodCall methodCall;

    /*
     * Type on which the method is invoked.
     *
     * Example:
     *
     * teacher.study()
     *
     * receiverType = Teacher
     */
    private String receiverType;

    /*
     * The resolved target method.
     *
     * Null if resolution fails.
     */
    private ParsedMethod resolvedMethod;

    public ParsedMethodCall getMethodCall() {

        return methodCall;

    }

    public void setMethodCall(
            ParsedMethodCall methodCall) {

        this.methodCall = methodCall;

    }

    public String getReceiverType() {

        return receiverType;

    }

    public void setReceiverType(
            String receiverType) {

        this.receiverType = receiverType;

    }

    public ParsedMethod getResolvedMethod() {

        return resolvedMethod;

    }

    public void setResolvedMethod(
            ParsedMethod resolvedMethod) {

        this.resolvedMethod = resolvedMethod;

    }

}