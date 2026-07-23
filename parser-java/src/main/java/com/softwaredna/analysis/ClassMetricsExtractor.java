package com.softwaredna.analysis;

import com.softwaredna.model.ClassMetrics;
import com.softwaredna.model.MethodMetrics;
import com.softwaredna.model.ParsedClass;
import com.softwaredna.model.ParsedMethod;

public class ClassMetricsExtractor {

    public ClassMetrics extract(ParsedClass parsedClass) {

        ClassMetrics metrics = new ClassMetrics();

        // Basic Counts
        metrics.setFieldCount(
                parsedClass.getFields().size());

        metrics.setConstructorCount(
                parsedClass.getConstructors().size());

        metrics.setMethodCount(
                parsedClass.getMethods().size());

        int publicMethods = 0;
        int privateMethods = 0;
        int protectedMethods = 0;

        int totalLinesOfCode = 0;
        int totalParameters = 0;
        int totalLocalVariables = 0;
        int totalMethodCalls = 0;
        int totalObjectCreations = 0;
        int totalReturnStatements = 0;

        for (ParsedMethod method : parsedClass.getMethods()) {

            /*
             * Skip methods that somehow do not
             * have metrics attached.
             */
            if (method.getMetrics() == null) {
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

        if (metrics.getMethodCount() > 0) {

            metrics.setAverageMethodLinesOfCode(
                    totalLinesOfCode /
                    metrics.getMethodCount());

        }

        return metrics;
    }

}