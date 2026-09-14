package com.softwaredna.analysis.metrics.java;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.softwaredna.analysis.ConditionalCountExtractor;
import com.softwaredna.analysis.CyclomaticComplexityExtractor;
import com.softwaredna.analysis.LoopCountExtractor;
import com.softwaredna.analysis.MaximumNestingDepthExtractor;
import com.softwaredna.analysis.metrics.MethodMetricProvider;
import com.softwaredna.model.MethodMetrics;

/**
 * Java implementation of the common method metric provider.
 *
 * <p>This class contains JavaParser-specific AST traversal.
 * It converts a JavaParser {@link MethodDeclaration} into the
 * language-neutral {@link MethodMetrics} representation.</p>
 */
public class JavaMethodMetricProvider
        implements MethodMetricProvider<MethodDeclaration> {

    private final CyclomaticComplexityExtractor
            cyclomaticComplexityExtractor;

    private final MaximumNestingDepthExtractor
            maximumNestingDepthExtractor;

    private final LoopCountExtractor
            loopCountExtractor;

    private final ConditionalCountExtractor
            conditionalCountExtractor;

    public JavaMethodMetricProvider() {

        cyclomaticComplexityExtractor =
                new CyclomaticComplexityExtractor();

        maximumNestingDepthExtractor =
                new MaximumNestingDepthExtractor();

        loopCountExtractor =
                new LoopCountExtractor();

        conditionalCountExtractor =
                new ConditionalCountExtractor();
    }

    /**
     * Calculates common method metrics from a JavaParser method.
     *
     * @param method JavaParser method declaration
     * @return common method metrics
     */
    @Override
    public MethodMetrics calculate(
            MethodDeclaration method) {

        if (method == null) {
            throw new IllegalArgumentException(
                    "MethodDeclaration cannot be null."
            );
        }

        MethodMetrics metrics =
                new MethodMetrics();

        // ==========================
        // Parameter Count
        // ==========================

        metrics.setParameterCount(
                method.getParameters().size()
        );

        // ==========================
        // Local Variables
        // ==========================

        metrics.setLocalVariableCount(
                method.findAll(
                        VariableDeclarator.class
                ).size()
        );

        // ==========================
        // Method Calls
        // ==========================

        metrics.setMethodCallCount(
                method.findAll(
                        MethodCallExpr.class
                ).size()
        );

        // ==========================
        // Object Creations
        // ==========================

        metrics.setObjectCreationCount(
                method.findAll(
                        ObjectCreationExpr.class
                ).size()
        );

        // ==========================
        // Return Statements
        // ==========================

        metrics.setReturnCount(
                method.findAll(
                        ReturnStmt.class
                ).size()
        );

        // ==========================
        // Lines of Code
        // ==========================

        if (method.getBegin().isPresent()
                && method.getEnd().isPresent()) {

            int start =
                    method.getBegin().get().line;

            int end =
                    method.getEnd().get().line;

            metrics.setLinesOfCode(
                    end - start + 1
            );
        }

        // ==========================
        // Cyclomatic Complexity
        // ==========================

        metrics.setCyclomaticComplexity(
                cyclomaticComplexityExtractor.extract(
                        method
                )
        );

        // ==========================
        // Maximum Nesting Depth
        // ==========================

        metrics.setMaximumNestingDepth(
                maximumNestingDepthExtractor.extract(
                        method
                )
        );

        // ==========================
        // Loop Count
        // ==========================

        metrics.setLoopCount(
                loopCountExtractor.extract(
                        method
                )
        );

        // ==========================
        // Conditional Count
        // ==========================

        metrics.setConditionalCount(
                conditionalCountExtractor.extract(
                        method
                )
        );

        return metrics;
    }
}