package com.softwaredna.printer;

import com.softwaredna.model.*;

public class ClassPrinter {

    private final MethodPrinter methodPrinter;
    private final MetricPrinter metricPrinter;

    public ClassPrinter() {

        methodPrinter = new MethodPrinter();
        metricPrinter = new MetricPrinter();

    }

    public void print(ParsedClass clazz) {

        System.out.println("  Class : "
                + clazz.getName());

        System.out.println();

        // ---------------- ANNOTATIONS ----------------

        System.out.println("    Annotations");

        if (clazz.getAnnotations().isEmpty()) {

            System.out.println("      None");

        } else {

            clazz.getAnnotations().forEach(annotation ->
                    System.out.println(
                            "      - @" + annotation.getName()));

        }

        System.out.println();

        // ---------------- FIELDS ----------------

        System.out.println("    Fields");

        if (clazz.getFields().isEmpty()) {

            System.out.println("      None");

        } else {

            for (ParsedField field : clazz.getFields()) {

                System.out.println(
                        "      - "
                                + field.getName()
                                + " : "
                                + field.getType());

            }

        }

        System.out.println();

        // ---------------- CONSTRUCTORS ----------------

        System.out.println("    Constructors");

        if (clazz.getConstructors().isEmpty()) {

            System.out.println("      None");

        } else {

            for (ParsedConstructor constructor :
                    clazz.getConstructors()) {

                System.out.println(
                        "      - "
                                + constructor.getName()
                                + "()");

                if (constructor.getParameters().isEmpty()) {

                    System.out.println(
                            "        Parameters : None");

                } else {

                    System.out.println("        Parameters");

                    constructor.getParameters().forEach(parameter ->
                            System.out.println(
                                    "          - "
                                            + parameter.getType()
                                            + " "
                                            + parameter.getName()));

                }

            }

        }

        System.out.println();

        // ---------------- METHODS ----------------

        System.out.println("    Methods");

        if (clazz.getMethods().isEmpty()) {

            System.out.println("      None");

        } else {

            for (ParsedMethod method :
                    clazz.getMethods()) {

                methodPrinter.print(method);

            }

        }

        System.out.println();

        metricPrinter.print(clazz.getMetrics());

        System.out.println();

    }

}