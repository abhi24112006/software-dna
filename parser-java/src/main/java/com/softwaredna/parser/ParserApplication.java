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

                System.out.println("Imports");

                if (file.getImports().isEmpty()) {

                    System.out.println("  None");

                } else {

                    file.getImports()
                            .forEach(i ->
                                    System.out.println("  - " + i));

                }

                System.out.println();

                System.out.println("Classes");

                for (ParsedClass clazz : file.getClasses()) {

                    System.out.println("  Class : "
                            + clazz.getName());

                    System.out.println();

                    System.out.println("    Fields");

                    if (clazz.getFields().isEmpty()) {

                        System.out.println("      None");

                    } else {

                        for (ParsedField field : clazz.getFields()) {

                            System.out.println(
                                    "      - "
                                            + field.getName()
                                            + " : "
                                            + field.getType()
                            );

                        }

                    }

                    System.out.println();

                    System.out.println("    Methods");

                    if (clazz.getMethods().isEmpty()) {

                        System.out.println("      None");

                    } else {

                        for (ParsedMethod method : clazz.getMethods()) {

                            System.out.println(
                                    "      - "
                                            + method.getName()
                                            + "() : "
                                            + method.getReturnType()
                            );

                        }

                    }

                    System.out.println();

                }

            }

        }

        catch (Exception e) {

            e.printStackTrace();

        }

    }

}