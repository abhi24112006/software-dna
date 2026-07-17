package com.softwaredna.parser;

import com.softwaredna.model.*;

public class ParserApplication {

    public static void main(String[] args) {

        try {

            RepositoryParser parser = new RepositoryParser();

            RepositoryModel repository =
                    parser.parseRepository("../sample_projects");

            System.out.println("======================================");
            System.out.println("     Software DNA Repository Engine");
            System.out.println("======================================");
            System.out.println();

            System.out.println("Repository : "
                    + repository.getRepositoryName());

            System.out.println("Files Parsed : "
                    + repository.getFiles().size());

            System.out.println();

            for (ParsedFile file : repository.getFiles()) {

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
                                "  - " + parsedInterface.getName()
                        );

                        System.out.println("    Annotations");

                        if (parsedInterface.getAnnotations().isEmpty()) {

                            System.out.println("      None");

                        } else {

                            parsedInterface.getAnnotations().forEach(annotation ->
                                    System.out.println(
                                            "      - @" + annotation.getName()
                                    )
                            );

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
                                "  - " + parsedEnum.getName()
                        );

                        System.out.println("    Annotations");

                        if (parsedEnum.getAnnotations().isEmpty()) {

                            System.out.println("      None");

                        } else {

                            parsedEnum.getAnnotations().forEach(annotation ->
                                    System.out.println(
                                            "      - @" + annotation.getName()
                                    )
                            );

                        }

                        System.out.println();

                    }

                }

                // ---------------- RECORDS ----------------

                System.out.println("Records");

                if (file.getRecords().isEmpty()) {

                    System.out.println("  None");

                } else {

                    for (ParsedRecord parsedRecord : file.getRecords()) {

    System.out.println(
            "  - " + parsedRecord.getName()
    );

    System.out.println("    Annotations");

    if (parsedRecord.getAnnotations().isEmpty()) {

        System.out.println("      None");

    } else {

        parsedRecord.getAnnotations().forEach(annotation ->
                System.out.println(
                        "      - @" + annotation.getName()
                )
        );

    }

    System.out.println();

}

                }

                System.out.println();

                // ---------------- CLASSES ----------------

                System.out.println("Classes");

                for (ParsedClass clazz : file.getClasses()) {

                    System.out.println("  Class : "
                            + clazz.getName());

                    System.out.println();

                    // ---------- ANNOTATIONS ----------

                    System.out.println("    Annotations");

                    if (clazz.getAnnotations().isEmpty()) {

                        System.out.println("      None");

                    } else {

                        clazz.getAnnotations().forEach(annotation ->
                                System.out.println(
                                        "      - @" + annotation.getName()
                                )
                        );

                    }

                    System.out.println();

                    // ---------- FIELDS ----------

                    System.out.println("    Fields");

                    if (clazz.getFields().isEmpty()) {

                        System.out.println("      None");

                    } else {

                        for (ParsedField field :
                                clazz.getFields()) {

                            System.out.println(
                                    "      - "
                                            + field.getName()
                                            + " : "
                                            + field.getType()
                            );

                        }

                    }

                    System.out.println();

                    // ---------- CONSTRUCTORS ----------

                    System.out.println("    Constructors");

                    if (clazz.getConstructors().isEmpty()) {

                        System.out.println("      None");

                    } else {

                        for (ParsedConstructor constructor :
        clazz.getConstructors()) {

    System.out.println(
            "      - " + constructor.getName() + "()"
    );

    if (constructor.getParameters().isEmpty()) {

        System.out.println("        Parameters : None");

    } else {

        System.out.println("        Parameters");

        constructor.getParameters().forEach(parameter ->
                System.out.println(
                        "          - "
                                + parameter.getType()
                                + " "
                                + parameter.getName()
                )
        );

    }

}

                    }

                    System.out.println();

                    // ---------- METHODS ----------

                    System.out.println("    Methods");

                    if (clazz.getMethods().isEmpty()) {

                        System.out.println("      None");

                    } else {

                        for (ParsedMethod method :
        clazz.getMethods()) {

    System.out.println(
            "      - "
                    + method.getName()
                    + "() : "
                    + method.getReturnType()
    );

    if (method.getParameters().isEmpty()) {

        System.out.println("        Parameters : None");

    } else {

        System.out.println("        Parameters");

        method.getParameters().forEach(parameter ->
                System.out.println(
                        "          - "
                                + parameter.getType()
                                + " "
                                + parameter.getName()
                )
        );

    }

}

                    }

                    System.out.println();

                }

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

}