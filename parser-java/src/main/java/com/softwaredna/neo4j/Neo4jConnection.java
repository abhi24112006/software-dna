package com.softwaredna.neo4j;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

public class Neo4jConnection implements AutoCloseable {

    private final Driver driver;

    public Neo4jConnection(
            String uri,
            String username,
            String password) {

        driver = GraphDatabase.driver(
                uri,
                AuthTokens.basic(
                        username,
                        password
                )
        );

    }


    /*
     * -------------------------------------------------------
     * Verify Connection
     * -------------------------------------------------------
     */

    public void verifyConnection() {

        driver.verifyConnectivity();

        System.out.println();
        System.out.println(
                "Neo4j connection successful!"
        );
        System.out.println();

    }


    /*
     * -------------------------------------------------------
     * Get Driver
     * -------------------------------------------------------
     */

    public Driver getDriver() {

        return driver;

    }


    /*
     * -------------------------------------------------------
     * Close Driver
     * -------------------------------------------------------
     */

    @Override
    public void close() {

        driver.close();

    }

}