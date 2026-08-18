package com.softwaredna.neo4j;

public class Neo4jConnectionTest {

    public static void main(String[] args) {

        String uri =
                "bolt://localhost:7687";

        String username =
                "neo4j";

        String password =
                System.getenv("NEO4J_PASSWORD");


        try (
                Neo4jConnection connection =
                        new Neo4jConnection(
                                uri,
                                username,
                                password
                        )
        ) {

            connection.verifyConnection();

        }

        catch (Exception e) {

            System.out.println();
            System.out.println(
                    "Neo4j connection failed!"
            );
            System.out.println();

            e.printStackTrace();

        }

    }

}