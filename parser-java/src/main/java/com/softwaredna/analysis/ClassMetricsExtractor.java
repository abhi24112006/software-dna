package com.softwaredna.analysis;

import com.softwaredna.model.ClassMetrics;
import com.softwaredna.model.MethodMetrics;
import com.softwaredna.model.ParsedClass;
import com.softwaredna.model.ParsedMethod;

public class ClassMetricsExtractor {

    public ClassMetrics extract(ParsedClass parsedClass) {

        ClassMetrics metrics = new ClassMetrics();

        // Basic Counts
        metrics.setFieldCount(parsedClass.getFields().size());
        metrics.setConstructorCount(parsedClass.getConstructors().size());
        metrics.setMethodCount(parsedClass.getMethods().size());

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

            if (method.getMetrics() == null) {
                continue;
            }

            MethodMetrics methodMetrics = method.getMetrics();

            totalLinesOfCode += methodMetrics.getLinesOfCode();
            totalParameters += methodMetrics.getParameterCount();
            totalLocalVariables += methodMetrics.getLocalVariableCount();
            totalMethodCalls += methodMetrics.getMethodCallCount();
            totalObjectCreations += methodMetrics.getObjectCreationCount();
            totalReturnStatements += methodMetrics.getReturnCount();

            // ==========================
            // Cyclomatic Complexity
            // ==========================

            totalCyclomaticComplexity +=
                    methodMetrics.getCyclomaticComplexity();

            maximumCyclomaticComplexity =
                    Math.max(maximumCyclomaticComplexity,
                            methodMetrics.getCyclomaticComplexity());

            // ==========================
            // Loop Count
            // ==========================

            totalLoopCount +=
                    methodMetrics.getLoopCount();

            maximumLoopCount =
                    Math.max(maximumLoopCount,
                            methodMetrics.getLoopCount());

            // ==========================
            // Conditional Count
            // ==========================

            totalConditionalCount +=
                    methodMetrics.getConditionalCount();

            maximumConditionalCount =
                    Math.max(maximumConditionalCount,
                            methodMetrics.getConditionalCount());

            // ==========================
            // Maximum Nesting Depth
            // ==========================

            maximumNestingDepth =
                    Math.max(maximumNestingDepth,
                            methodMetrics.getMaximumNestingDepth());

            /*
             * Visibility counts
             *
             * NOTE:
             * ParsedMethod currently does not store
             * visibility information.
             *
             * For now we assume all extracted methods
             * are public.
             *
             * Once ParsedMethod stores modifiers,
             * update this logic accordingly.
             */
            publicMethods++;
        }

        metrics.setPublicMethodCount(publicMethods);
        metrics.setPrivateMethodCount(privateMethods);
        metrics.setProtectedMethodCount(protectedMethods);

        metrics.setTotalLinesOfCode(totalLinesOfCode);
        metrics.setTotalParameters(totalParameters);
        metrics.setTotalLocalVariables(totalLocalVariables);
        metrics.setTotalMethodCalls(totalMethodCalls);
        metrics.setTotalObjectCreations(totalObjectCreations);
        metrics.setTotalReturnStatements(totalReturnStatements);

        metrics.setTotalCyclomaticComplexity(totalCyclomaticComplexity);
        metrics.setMaximumCyclomaticComplexity(maximumCyclomaticComplexity);

        metrics.setTotalLoopCount(totalLoopCount);
        metrics.setMaximumLoopCount(maximumLoopCount);

        metrics.setTotalConditionalCount(totalConditionalCount);
        metrics.setMaximumConditionalCount(maximumConditionalCount);

        metrics.setMaximumNestingDepth(maximumNestingDepth);

        if (metrics.getMethodCount() > 0) {

            metrics.setAverageMethodLinesOfCode(
                    totalLinesOfCode / metrics.getMethodCount());

            metrics.setAverageCyclomaticComplexity(
                    (double) totalCyclomaticComplexity /
                            metrics.getMethodCount());

            metrics.setAverageLoopCount(
                    (double) totalLoopCount /
                            metrics.getMethodCount());

            metrics.setAverageConditionalCount(
                    (double) totalConditionalCount /
                            metrics.getMethodCount());
        }

        return metrics;
    }
}