package com.softwaredna.parser;

import com.softwaredna.model.ParsedClass;
import com.softwaredna.model.ParsedFile;
import com.softwaredna.model.ParsedMethod;
import com.softwaredna.model.RepositoryModel;

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

            System.out.println();

            System.out.println("Files Parsed : "
                    + repository.getFiles().size());

            System.out.println();

            for (ParsedFile file : repository.getFiles()) {

                System.out.println("--------------------------------------");

                System.out.println("Package : "
                        + file.getPackageName());

                System.out.println();

                System.out.println("Imports");

                file.getImports()
                        .forEach(i -> System.out.println("  - " + i));

                System.out.println();

                System.out.println("Classes");

                for (ParsedClass clazz : file.getClasses()) {

                    System.out.println("  Class : "
                            + clazz.getName());

                    System.out.println("    Methods");

                    for (ParsedMethod method : clazz.getMethods()) {

                        System.out.println("      - "
                                + method.getName()
                                + "() : "
                                + method.getReturnType());

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