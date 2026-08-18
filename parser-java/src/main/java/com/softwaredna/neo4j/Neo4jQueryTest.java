package com.softwaredna.neo4j;

import java.util.List;

public class Neo4jQueryTest {

    public static void main(
            String[] args) {

        String uri =
                "bolt://localhost:7687";

        String username =
                "neo4j";

        String password =
                System.getenv("NEO4J_PASSWORD");

        String database =
                "neo4j";


        try (
                Neo4jConnection connection =
                        new Neo4jConnection(
                                uri,
                                username,
                                password
                        )
        ) {

            connection.verifyConnection();


            Neo4jQueryService query =
                    new Neo4jQueryService(
                            connection.getDriver(),
                            database
                    );


            /*
             * ------------------------------------------------
             * Dependencies
             * ------------------------------------------------
             */

            System.out.println();
            System.out.println(
                    "Dependencies of StudentService:"
            );

            List<String> dependencies =
                    query.getDependencies(
                            "Default Package.StudentService"
                    );

            for (String name :
                    dependencies) {

                System.out.println(
                        "  -> " + name
                );

            }


            /*
             * ------------------------------------------------
             * Dependents
             * ------------------------------------------------
             */

            System.out.println();
            System.out.println(
                    "Dependents of Student:"
            );

            List<String> dependents =
                    query.getDependents(
                            "Default Package.Student"
                    );

            for (String name :
                    dependents) {

                System.out.println(
                        "  -> " + name
                );

            }


            /*
             * ------------------------------------------------
             * Callees
             * ------------------------------------------------
             */

            System.out.println();
            System.out.println(
                    "Callees of Student.study():"
            );

            List<String> callees =
                    query.getCallees(
                            "Default Package.Student#study()"
                    );

            for (String name :
                    callees) {

                System.out.println(
                        "  -> " + name
                );

            }


            /*
             * ------------------------------------------------
             * Callers
             * ------------------------------------------------
             */

            System.out.println();
            System.out.println(
                    "Callers of Teacher.teach():"
            );

            List<String> callers =
                    query.getCallers(
                            "Default Package.Teacher#teach()"
                    );

            for (String name :
                    callers) {

                System.out.println(
                        "  -> " + name
                );

            }


            /*
             * ------------------------------------------------
             * Subclasses
             * ------------------------------------------------
             */

            System.out.println();
            System.out.println(
                    "Subclasses of Animal:"
            );

            for (String name :
                    query.getSubclasses(
                            "Default Package.Animal"
                    )) {

                System.out.println(
                        "  -> " + name
                );

            }


            /*
             * ------------------------------------------------
             * Superclass
             * ------------------------------------------------
             */

            System.out.println();
            System.out.println(
                    "Superclass of Mammal:"
            );

            for (String name :
                    query.getSuperclass(
                            "Default Package.Mammal"
                    )) {

                System.out.println(
                        "  -> " + name
                );

            }


            /*
             * ------------------------------------------------
             * Implemented Interfaces
             * ------------------------------------------------
             */

            System.out.println();
            System.out.println(
                    "Interfaces implemented by Report:"
            );

            for (String name :
                    query.getImplementedInterfaces(
                            "demo.Report"
                    )) {

                System.out.println(
                        "  -> " + name
                );

            }


            /*
             * ------------------------------------------------
             * Implementations
             * ------------------------------------------------
             */

            System.out.println();
            System.out.println(
                    "Implementations of Printable:"
            );

            for (String name :
                    query.getImplementations(
                            "com.demo.Printable"
                    )) {

                System.out.println(
                        "  -> " + name
                );

            }

        }

        catch (Exception e) {

            System.out.println();
            System.out.println(
                    "Neo4j query failed!"
            );

            e.printStackTrace();

        }

    }

}