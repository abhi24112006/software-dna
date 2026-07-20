package com.softwaredna.model;

import java.util.ArrayList;
import java.util.List;

public class ParsedMethodCall {

    /*
     * Expression on which the method is invoked.
     *
     * Examples:
     *
     * teacher.study()
     * teacher
     *
     * student.getTeacher().study()
     * student.getTeacher()
     *
     * System.out.println()
     * System.out
     */
    private String receiverExpression;

    /*
     * Invoked method name.
     */
    private String methodName;

    /*
     * Arguments exactly as they appear in source code.
     *
     * Example:
     *
     * teacher.teach(student, course)
     *
     * arguments =
     * [
     *     "student",
     *     "course"
     * ]
     */
    private List<String> argumentExpressions =
            new ArrayList<>();

    public ParsedMethodCall() {

    }

    public String getReceiverExpression() {

        return receiverExpression;

    }

    public void setReceiverExpression(
            String receiverExpression) {

        this.receiverExpression = receiverExpression;

    }

    public String getMethodName() {

        return methodName;

    }

    public void setMethodName(
            String methodName) {

        this.methodName = methodName;

    }

    public List<String> getArgumentExpressions() {

        return argumentExpressions;

    }

    public void setArgumentExpressions(
            List<String> argumentExpressions) {

        this.argumentExpressions = argumentExpressions;

    }

}