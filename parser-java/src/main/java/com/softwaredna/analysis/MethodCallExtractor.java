package com.softwaredna.analysis;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.softwaredna.model.ParsedMethodCall;

public class MethodCallExtractor {

    public void extract(
            MethodDeclaration method,
            MethodAnalysisResult result) {

        method.findAll(MethodCallExpr.class)
                .forEach(methodCall -> {

                    ParsedMethodCall parsedMethodCall =
                            new ParsedMethodCall();

                    /*
                     * Receiver
                     *
                     * teacher.study()
                     * ^
                     */
                    methodCall.getScope().ifPresent(scope ->
                            parsedMethodCall.setReceiverExpression(
                                    scope.toString()
                            )
                    );

                    /*
                     * Method name
                     *
                     * teacher.study()
                     *         ^
                     */
                    parsedMethodCall.setMethodName(
                            methodCall.getNameAsString()
                    );

                    /*
                     * Arguments
                     *
                     * teacher.teach(student, course)
                     *               ^^^^^^^^^^^^^^^
                     */
                    methodCall.getArguments().forEach(argument ->
                            parsedMethodCall.getArgumentExpressions().add(
                                    argument.toString()
                            )
                    );

                    /*
                     * Store the completed method call
                     */
                    result.getMethodCalls().add(
                            parsedMethodCall
                    );

                });

    }

}