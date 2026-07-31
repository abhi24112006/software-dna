package com.softwaredna.analysis;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.softwaredna.model.MethodMetrics;

public class MethodMetricsExtractor {

    private final CyclomaticComplexityExtractor cyclomaticComplexityExtractor;
    private final MaximumNestingDepthExtractor maximumNestingDepthExtractor;
    private final LoopCountExtractor loopCountExtractor;
    private final ConditionalCountExtractor conditionalCountExtractor;

    public MethodMetricsExtractor() {

        cyclomaticComplexityExtractor = new CyclomaticComplexityExtractor();
        maximumNestingDepthExtractor = new MaximumNestingDepthExtractor();
        loopCountExtractor = new LoopCountExtractor();
        conditionalCountExtractor = new ConditionalCountExtractor();

    }

    public MethodMetrics extract(MethodDeclaration method) {

        MethodMetrics metrics = new MethodMetrics();

        // Parameter Count
        metrics.setParameterCount(method.getParameters().size());

        // Local Variables
        metrics.setLocalVariableCount(
                method.findAll(VariableDeclarator.class).size());

        // Method Calls
        metrics.setMethodCallCount(
                method.findAll(MethodCallExpr.class).size());

        // Object Creations
        metrics.setObjectCreationCount(
                method.findAll(ObjectCreationExpr.class).size());

        // Return Statements
        metrics.setReturnCount(
                method.findAll(ReturnStmt.class).size());

        // Lines of Code
        if (method.getBegin().isPresent() && method.getEnd().isPresent()) {

            int start = method.getBegin().get().line;
            int end = method.getEnd().get().line;

            metrics.setLinesOfCode(end - start + 1);

        }

        // Cyclomatic Complexity
        metrics.setCyclomaticComplexity(
                cyclomaticComplexityExtractor.extract(method));

        metrics.setMaximumNestingDepth(
        maximumNestingDepthExtractor.extract(method));

        metrics.setLoopCount(
        loopCountExtractor.extract(method));

        metrics.setConditionalCount(
        conditionalCountExtractor.extract(method));

        return metrics;
    }

}