package com.softwaredna.neo4j;

import java.util.List;

public class Neo4jImpactTest {

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


            Neo4jImpactAnalyzer analyzer =
                    new Neo4jImpactAnalyzer(
                            connection.getDriver(),
                            database
                    );


            /*
             * ------------------------------------------------
             * Standard Impact
             * ------------------------------------------------
             */

            System.out.println();
            System.out.println(
                    "======================================"
            );

            System.out.println(
                    "Neo4j Impact Analysis"
            );

            System.out.println(
                    "======================================"
            );


            System.out.println();
            System.out.println(
                    "Impact of changing Student:"
            );


            List<String> studentImpact =
                    analyzer.getImpact(
                            "Default Package.Student"
                    );


            for (String name :
                    studentImpact) {

                System.out.println(
                        "  -> " + name
                );

            }


            /*
             * ------------------------------------------------
             * Method Impact
             * ------------------------------------------------
             */

            System.out.println();
            System.out.println(
                    "Impact of changing Teacher.teach():"
            );


            List<String> methodImpact =
                    analyzer.getMethodImpact(
                            "Default Package.Teacher#teach()"
                    );


            for (String name :
                    methodImpact) {

                System.out.println(
                        "  -> " + name
                );

            }


            /*
             * ------------------------------------------------
             * Containment-Aware Impact
             * ------------------------------------------------
             */

            System.out.println();
            System.out.println(
                    "Containment-Aware Impact Analysis"
            );


            System.out.println();
            System.out.println(
                    "Impact of changing Teacher:"
            );


            List<String> containmentImpact =
                    analyzer.getContainmentAwareImpact(
                            "Default Package.Teacher"
                    );


            for (String name :
                    containmentImpact) {

                System.out.println(
                        "  -> " + name
                );

            }

        }

        catch (Exception e) {

            System.out.println();
            System.out.println(
                    "Neo4j impact analysis failed!"
            );

            e.printStackTrace();

        }

    }

}