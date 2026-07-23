package com.softwaredna.printer;

import com.softwaredna.model.MethodMetrics;
import com.softwaredna.model.ParsedMethod;

public class MethodPrinter {

    private final MetricPrinter metricPrinter;

    public MethodPrinter() {

        metricPrinter = new MetricPrinter();

    }

    public void print(ParsedMethod method) {

        System.out.println(
                "      - "
                        + method.getName()
                        + "() : "
                        + method.getReturnType());

        if (method.getParameters().isEmpty()) {

            System.out.println(
                    "        Parameters : None");

        } else {

            System.out.println("        Parameters");

            method.getParameters().forEach(parameter ->
                    System.out.println(
                            "          - "
                                    + parameter.getType()
                                    + " "
                                    + parameter.getName()));

        }

        MethodMetrics metrics =
                method.getMetrics();

        if (metrics != null) {

            System.out.println();

            metricPrinter.print(metrics);

        } else {

            System.out.println();
            System.out.println("        Metrics : None");

        }

    }

}