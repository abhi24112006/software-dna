package com.softwaredna.printer;

import com.softwaredna.model.ClassMetrics;
import com.softwaredna.model.MethodMetrics;

public class MetricPrinter {

    public void print(MethodMetrics metrics) {

        System.out.println("        Metrics");

        System.out.println(
                "          Lines of Code : "
                        + metrics.getLinesOfCode());

        System.out.println(
                "          Parameter Count : "
                        + metrics.getParameterCount());

        System.out.println(
                "          Local Variables : "
                        + metrics.getLocalVariableCount());

        System.out.println(
                "          Method Calls : "
                        + metrics.getMethodCallCount());

        System.out.println(
                "          Object Creations : "
                        + metrics.getObjectCreationCount());

        System.out.println(
                "          Return Statements : "
                        + metrics.getReturnCount());

        System.out.println(
                "          Cyclomatic Complexity : "
                        + metrics.getCyclomaticComplexity());

    }

    public void print(ClassMetrics metrics) {

        System.out.println("    Class Metrics");

        if (metrics == null) {

            System.out.println("      None");

            return;

        }

        System.out.println(
                "      Fields               : "
                        + metrics.getFieldCount());

        System.out.println(
                "      Constructors         : "
                        + metrics.getConstructorCount());

        System.out.println(
                "      Methods              : "
                        + metrics.getMethodCount());

        System.out.println(
                "      Public Methods       : "
                        + metrics.getPublicMethodCount());

        System.out.println(
                "      Private Methods      : "
                        + metrics.getPrivateMethodCount());

        System.out.println(
                "      Protected Methods    : "
                        + metrics.getProtectedMethodCount());

        System.out.println(
                "      Total Method LOC     : "
                        + metrics.getTotalLinesOfCode());

        System.out.println(
                "      Average Method LOC   : "
                        + metrics.getAverageMethodLinesOfCode());

        System.out.println(
                "      Total Parameters     : "
                        + metrics.getTotalParameters());

        System.out.println(
                "      Total Local Variables: "
                        + metrics.getTotalLocalVariables());

        System.out.println(
                "      Total Method Calls   : "
                        + metrics.getTotalMethodCalls());

        System.out.println(
                "      Object Creations     : "
                        + metrics.getTotalObjectCreations());

        System.out.println(
                "      Return Statements    : "
                        + metrics.getTotalReturnStatements());

    }

}