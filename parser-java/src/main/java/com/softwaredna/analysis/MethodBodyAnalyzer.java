package com.softwaredna.analysis;

import com.github.javaparser.ast.body.MethodDeclaration;

public class MethodBodyAnalyzer {

    private final LocalVariableExtractor localVariableExtractor =
            new LocalVariableExtractor();

    private final MethodCallExtractor methodCallExtractor =
            new MethodCallExtractor();

    public MethodAnalysisResult analyze(
            MethodDeclaration method) {

        MethodAnalysisResult result =
                new MethodAnalysisResult();

        /*
         * Phase 1
         * Build local scope
         */
        localVariableExtractor.extract(
                method,
                result.getScope());

        /*
         * Phase 2
         * Extract method calls
         */
        methodCallExtractor.extract(
                method,
                result);

        return result;

    }

}