package com.softwaredna.printer;

import com.softwaredna.model.*;

public class FilePrinter {

    private final ClassPrinter classPrinter;

    public FilePrinter() {

        classPrinter = new ClassPrinter();

    }

    public void print(ParsedFile file) {

        System.out.println("--------------------------------------");

        System.out.println("Package : "
                + file.getPackageName());

        System.out.println();

        // ---------------- IMPORTS ----------------

        System.out.println("Imports");

        if (file.getImports().isEmpty()) {

            System.out.println("  None");

        } else {

            file.getImports().forEach(importName ->
                    System.out.println("  - " + importName));

        }

        System.out.println();

        // ---------------- INTERFACES ----------------

        System.out.println("Interfaces");

        if (file.getInterfaces().isEmpty()) {

            System.out.println("  None");

        } else {

            for (ParsedInterface parsedInterface :
                    file.getInterfaces()) {

                System.out.println(
                        "  - " + parsedInterface.getName());

                System.out.println("    Annotations");

                if (parsedInterface.getAnnotations().isEmpty()) {

                    System.out.println("      None");

                } else {

                    parsedInterface.getAnnotations().forEach(annotation ->
                            System.out.println(
                                    "      - @" + annotation.getName()));

                }

                System.out.println();

            }

        }

        // ---------------- ENUMS ----------------

        System.out.println("Enums");

        if (file.getEnums().isEmpty()) {

            System.out.println("  None");

        } else {

            for (ParsedEnum parsedEnum :
                    file.getEnums()) {

                System.out.println(
                        "  - " + parsedEnum.getName());

                System.out.println("    Annotations");

                if (parsedEnum.getAnnotations().isEmpty()) {

                    System.out.println("      None");

                } else {

                    parsedEnum.getAnnotations().forEach(annotation ->
                            System.out.println(
                                    "      - @" + annotation.getName()));

                }

                System.out.println();

            }

        }

        // ---------------- RECORDS ----------------

        System.out.println("Records");

        if (file.getRecords().isEmpty()) {

            System.out.println("  None");

        } else {

            for (ParsedRecord parsedRecord :
                    file.getRecords()) {

                System.out.println(
                        "  - " + parsedRecord.getName());

                System.out.println("    Annotations");

                if (parsedRecord.getAnnotations().isEmpty()) {

                    System.out.println("      None");

                } else {

                    parsedRecord.getAnnotations().forEach(annotation ->
                            System.out.println(
                                    "      - @" + annotation.getName()));

                }

                System.out.println();

            }

        }

        System.out.println();

        // ---------------- CLASSES ----------------

        System.out.println("Classes");

        for (ParsedClass clazz : file.getClasses()) {

            classPrinter.print(clazz);

        }

    }

}