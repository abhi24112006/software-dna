package com.softwaredna.analysis.metrics;

import com.softwaredna.model.ClassMetrics;
import com.softwaredna.model.MethodMetrics;
import com.softwaredna.model.ParsedClass;
import com.softwaredna.model.ParsedMethod;

/**
 * Common class-level metric aggregation engine.
 *
 * <p>This component is intentionally independent of any
 * language-specific AST implementation. Language-specific
 * parsers are responsible for producing MethodMetrics.
 * This class aggregates those metrics into ClassMetrics.</p>
 *
 * <p>The same component can therefore be reused by Java,
 * Python, JavaScript, and future language adapters.</p>
 */
public class ClassMetricAggregator {

    /**
     * Aggregates method-level metrics and structural information
     * from a parsed class into class-level metrics.
     *
     * @param parsedClass normalized parsed class
     * @return aggregated class metrics
     */
    public ClassMetrics aggregate(ParsedClass parsedClass) {

        if (parsedClass == null) {
            throw new IllegalArgumentException(
                    "ParsedClass cannot be null."
            );
        }

        ClassMetrics metrics = new ClassMetrics();

        // ==========================
        // Basic Counts
        // ==========================

        metrics.setFieldCount(
                parsedClass.getFields().size()
        );

        metrics.setConstructorCount(
                parsedClass.getConstructors().size()
        );

        metrics.setMethodCount(
                parsedClass.getMethods().size()
        );

        int publicMethods = 0;
        int privateMethods = 0;
        int protectedMethods = 0;

        int totalLinesOfCode = 0;
        int totalParameters = 0;
        int totalLocalVariables = 0;
        int totalMethodCalls = 0;
        int totalObjectCreations = 0;
        int totalReturnStatements = 0;

        // ==========================
        // Aggregated Method Metrics
        // ==========================

        int totalCyclomaticComplexity = 0;
        int maximumCyclomaticComplexity = 0;

        int totalLoopCount = 0;
        int maximumLoopCount = 0;

        int totalConditionalCount = 0;
        int maximumConditionalCount = 0;

        int maximumNestingDepth = 0;

        for (ParsedMethod method : parsedClass.getMethods()) {

            if (method == null || method.getMetrics() == null) {
                continue;
            }

            MethodMetrics methodMetrics =
                    method.getMetrics();

            totalLinesOfCode +=
                    methodMetrics.getLinesOfCode();

            totalParameters +=
                    methodMetrics.getParameterCount();

            totalLocalVariables +=
                    methodMetrics.getLocalVariableCount();

            totalMethodCalls +=
                    methodMetrics.getMethodCallCount();

            totalObjectCreations +=
                    methodMetrics.getObjectCreationCount();

            totalReturnStatements +=
                    methodMetrics.getReturnCount();

            // ==========================
            // Cyclomatic Complexity
            // ==========================

            totalCyclomaticComplexity +=
                    methodMetrics.getCyclomaticComplexity();

            maximumCyclomaticComplexity =
                    Math.max(
                            maximumCyclomaticComplexity,
                            methodMetrics.getCyclomaticComplexity()
                    );

            // ==========================
            // Loop Count
            // ==========================

            totalLoopCount +=
                    methodMetrics.getLoopCount();

            maximumLoopCount =
                    Math.max(
                            maximumLoopCount,
                            methodMetrics.getLoopCount()
                    );

            // ==========================
            // Conditional Count
            // ==========================

            totalConditionalCount +=
                    methodMetrics.getConditionalCount();

            maximumConditionalCount =
                    Math.max(
                            maximumConditionalCount,
                            methodMetrics.getConditionalCount()
                    );

            // ==========================
            // Maximum Nesting Depth
            // ==========================

            maximumNestingDepth =
                    Math.max(
                            maximumNestingDepth,
                            methodMetrics.getMaximumNestingDepth()
                    );

            /*
             * Visibility counts
             *
             * ParsedMethod currently does not store
             * visibility information.
             *
             * Until modifiers are represented in the
             * normalized model, extracted methods are
             * treated as public.
             */
            publicMethods++;
        }

        // ==========================
        // Store Aggregated Metrics
        // ==========================

        metrics.setPublicMethodCount(
                publicMethods
        );

        metrics.setPrivateMethodCount(
                privateMethods
        );

        metrics.setProtectedMethodCount(
                protectedMethods
        );

        metrics.setTotalLinesOfCode(
                totalLinesOfCode
        );

        metrics.setTotalParameters(
                totalParameters
        );

        metrics.setTotalLocalVariables(
                totalLocalVariables
        );

        metrics.setTotalMethodCalls(
                totalMethodCalls
        );

        metrics.setTotalObjectCreations(
                totalObjectCreations
        );

        metrics.setTotalReturnStatements(
                totalReturnStatements
        );

        metrics.setTotalCyclomaticComplexity(
                totalCyclomaticComplexity
        );

        metrics.setMaximumCyclomaticComplexity(
                maximumCyclomaticComplexity
        );

        metrics.setTotalLoopCount(
                totalLoopCount
        );

        metrics.setMaximumLoopCount(
                maximumLoopCount
        );

        metrics.setTotalConditionalCount(
                totalConditionalCount
        );

        metrics.setMaximumConditionalCount(
                maximumConditionalCount
        );

        metrics.setMaximumNestingDepth(
                maximumNestingDepth
        );

        // ==========================
        // Averages
        // ==========================

        if (metrics.getMethodCount() > 0) {

            metrics.setAverageMethodLinesOfCode(
                    totalLinesOfCode /
                            metrics.getMethodCount()
            );

            metrics.setAverageCyclomaticComplexity(
                    (double) totalCyclomaticComplexity /
                            metrics.getMethodCount()
            );

            metrics.setAverageLoopCount(
                    (double) totalLoopCount /
                            metrics.getMethodCount()
            );

            metrics.setAverageConditionalCount(
                    (double) totalConditionalCount /
                            metrics.getMethodCount()
            );
        }

        return metrics;
    }
}