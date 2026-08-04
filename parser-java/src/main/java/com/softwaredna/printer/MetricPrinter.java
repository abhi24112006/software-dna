package com.softwaredna.printer;

import com.softwaredna.model.ClassMetrics;
import com.softwaredna.model.MethodMetrics;

public class MetricPrinter {

    public void print(MethodMetrics metrics) {

        System.out.println("        Metrics");

        System.out.println(
                "          Lines of Code            : "
                        + metrics.getLinesOfCode());

        System.out.println(
                "          Parameter Count          : "
                        + metrics.getParameterCount());

        System.out.println(
                "          Local Variables          : "
                        + metrics.getLocalVariableCount());

        System.out.println(
                "          Method Calls             : "
                        + metrics.getMethodCallCount());

        System.out.println(
                "          Object Creations         : "
                        + metrics.getObjectCreationCount());

        System.out.println(
                "          Return Statements        : "
                        + metrics.getReturnCount());

        System.out.println();

        System.out.println("          Complexity Metrics");

        System.out.println(
                "            Cyclomatic Complexity  : "
                        + metrics.getCyclomaticComplexity());

        System.out.println(
                "            Maximum Nesting Depth  : "
                        + metrics.getMaximumNestingDepth());

        System.out.println(
                "            Loop Count             : "
                        + metrics.getLoopCount());

        System.out.println(
                "            Conditional Count      : "
                        + metrics.getConditionalCount());
    }

    public void print(ClassMetrics metrics) {

        System.out.println("    Class Metrics");

        if (metrics == null) {

            System.out.println("      None");
            return;
        }

        System.out.println();

        System.out.println("      Basic Statistics");

        System.out.println(
                "        Fields                     : "
                        + metrics.getFieldCount());

        System.out.println(
                "        Constructors               : "
                        + metrics.getConstructorCount());

        System.out.println(
                "        Methods                    : "
                        + metrics.getMethodCount());

        System.out.println(
                "        Public Methods             : "
                        + metrics.getPublicMethodCount());

        System.out.println(
                "        Private Methods            : "
                        + metrics.getPrivateMethodCount());

        System.out.println(
                "        Protected Methods          : "
                        + metrics.getProtectedMethodCount());

        System.out.println();

        System.out.println("      Code Statistics");

        System.out.println(
                "        Total Method LOC           : "
                        + metrics.getTotalLinesOfCode());

        System.out.println(
                "        Average Method LOC         : "
                        + metrics.getAverageMethodLinesOfCode());

        System.out.println(
                "        Total Parameters           : "
                        + metrics.getTotalParameters());

        System.out.println(
                "        Total Local Variables      : "
                        + metrics.getTotalLocalVariables());

        System.out.println(
                "        Total Method Calls         : "
                        + metrics.getTotalMethodCalls());

        System.out.println(
                "        Total Object Creations     : "
                        + metrics.getTotalObjectCreations());

        System.out.println(
                "        Total Return Statements    : "
                        + metrics.getTotalReturnStatements());

        System.out.println();

        System.out.println("      Complexity Summary");

        System.out.println(
                "        Total Cyclomatic Complexity: "
                        + metrics.getTotalCyclomaticComplexity());

        System.out.println(
                "        Average Cyclomatic Comp.   : "
                        + metrics.getAverageCyclomaticComplexity());

        System.out.println(
                "        Maximum Cyclomatic Comp.   : "
                        + metrics.getMaximumCyclomaticComplexity());

        System.out.println();

        System.out.println(
                "        Total Loop Count           : "
                        + metrics.getTotalLoopCount());

        System.out.println(
                "        Average Loop Count         : "
                        + metrics.getAverageLoopCount());

        System.out.println(
                "        Maximum Loop Count         : "
                        + metrics.getMaximumLoopCount());

        System.out.println();

        System.out.println(
                "        Total Conditional Count    : "
                        + metrics.getTotalConditionalCount());

        System.out.println(
                "        Average Conditional Count  : "
                        + metrics.getAverageConditionalCount());

        System.out.println(
                "        Maximum Conditional Count  : "
                        + metrics.getMaximumConditionalCount());

        System.out.println();

        System.out.println(
                "        Maximum Nesting Depth      : "
                        + metrics.getMaximumNestingDepth());

        System.out.println();
System.out.println("      Architecture Metrics");

System.out.printf(
        "        Fan-Out                   : %d%n",
        metrics.getFanOut());

System.out.printf(
        "        Fan-In                    : %d%n",
        metrics.getFanIn());

        System.out.printf(
        "        CBO                       : %d%n",
        metrics.getCbo());

System.out.printf(
        "        DIT                       : %d%n",
        metrics.getDit()
);

System.out.printf(
        "        NOC                       : %d%n",
        metrics.getNoc());

        System.out.printf(
        "        RFC                       : %d%n",
        metrics.getRfc());
    }
}