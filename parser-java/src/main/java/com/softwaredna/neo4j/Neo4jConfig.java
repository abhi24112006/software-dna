package com.softwaredna.neo4j;

public class Neo4jConfig {

    private final String uri;
    private final String username;
    private final String password;
    private final String database;


    public Neo4jConfig(
            String uri,
            String username,
            String password,
            String database) {

        this.uri = uri;
        this.username = username;
        this.password = password;
        this.database = database;

    }


    public String getUri() {
        return uri;
    }


    public String getUsername() {
        return username;
    }


    public String getPassword() {
        return password;
    }


    public String getDatabase() {
        return database;
    }


    /*
     * -------------------------------------------------------
     * Load configuration from environment variables
     * -------------------------------------------------------
     */

    public static Neo4jConfig fromEnvironment() {

        String uri =
                getOrDefault(
                        "NEO4J_URI",
                        "bolt://localhost:7687"
                );

        String username =
                getOrDefault(
                        "NEO4J_USERNAME",
                        "neo4j"
                );

        String password =
                System.getenv("NEO4J_PASSWORD");

        String database =
                getOrDefault(
                        "NEO4J_DATABASE",
                        "neo4j"
                );


        if (password == null
                || password.isBlank()) {

            throw new IllegalStateException(
                    "NEO4J_PASSWORD environment variable is not set."
            );

        }


        return new Neo4jConfig(
                uri,
                username,
                password,
                database
        );

    }


    private static String getOrDefault(
            String variable,
            String defaultValue) {

        String value =
                System.getenv(variable);

        if (value == null
                || value.isBlank()) {

            return defaultValue;

        }

        return value;

    }

}