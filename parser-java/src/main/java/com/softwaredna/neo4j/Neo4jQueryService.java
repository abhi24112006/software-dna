package com.softwaredna.neo4j;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Neo4jQueryService {

    private final Driver driver;

    private final String database;


    public Neo4jQueryService(
            Driver driver,
            String database) {

        this.driver = driver;
        this.database = database;

    }


    /*
     * -------------------------------------------------------
     * Get Dependencies
     * -------------------------------------------------------
     *
     * Example:
     *
     * StudentService
     *      |
     *      | DEPENDS_ON
     *      v
     * Student
     */

    public List<String> getDependencies(
            String nodeId) {

        String cypher =
                """
                MATCH (source:Entity {id: $nodeId})
                      -[:DEPENDS_ON]->
                      (target:Entity)
                RETURN target.name AS name
                ORDER BY name
                """;


        return executeNameQuery(
                cypher,
                nodeId
        );

    }


    /*
     * -------------------------------------------------------
     * Get Dependents
     * -------------------------------------------------------
     *
     * Example:
     *
     * StudentService
     *      |
     *      | DEPENDS_ON
     *      v
     * Student
     *
     * Query direction is reversed.
     */

    public List<String> getDependents(
            String nodeId) {

        String cypher =
                """
                MATCH (source:Entity)
                      -[:DEPENDS_ON]->
                      (target:Entity {id: $nodeId})
                RETURN source.name AS name
                ORDER BY name
                """;


        return executeNameQuery(
                cypher,
                nodeId
        );

    }


    /*
     * -------------------------------------------------------
     * Get Callees
     * -------------------------------------------------------
     *
     * Method that the given method calls.
     */

    public List<String> getCallees(
            String methodId) {

        String cypher =
                """
                MATCH (source:Entity {id: $nodeId})
                      -[:CALLS]->
                      (target:Entity)
                RETURN target.name AS name
                ORDER BY name
                """;


        return executeNameQuery(
                cypher,
                methodId
        );

    }


    /*
     * -------------------------------------------------------
     * Get Callers
     * -------------------------------------------------------
     *
     * Methods that call the given method.
     */

    public List<String> getCallers(
            String methodId) {

        String cypher =
                """
                MATCH (source:Entity)
                      -[:CALLS]->
                      (target:Entity {id: $nodeId})
                RETURN source.name AS name
                ORDER BY name
                """;


        return executeNameQuery(
                cypher,
                methodId
        );

    }


    /*
     * -------------------------------------------------------
     * Get Subclasses
     * -------------------------------------------------------
     */

    public List<String> getSubclasses(
            String classId) {

        String cypher =
                """
                MATCH (source:Entity)
                      -[:EXTENDS]->
                      (target:Entity {id: $nodeId})
                RETURN source.name AS name
                ORDER BY name
                """;


        return executeNameQuery(
                cypher,
                classId
        );

    }


    /*
     * -------------------------------------------------------
     * Get Superclass
     * -------------------------------------------------------
     */

    public List<String> getSuperclass(
            String classId) {

        String cypher =
                """
                MATCH (source:Entity {id: $nodeId})
                      -[:EXTENDS]->
                      (target:Entity)
                RETURN target.name AS name
                ORDER BY name
                """;


        return executeNameQuery(
                cypher,
                classId
        );

    }


    /*
     * -------------------------------------------------------
     * Get Implemented Interfaces
     * -------------------------------------------------------
     */

    public List<String> getImplementedInterfaces(
            String classId) {

        String cypher =
                """
                MATCH (source:Entity {id: $nodeId})
                      -[:IMPLEMENTS]->
                      (target:Entity)
                RETURN target.name AS name
                ORDER BY name
                """;


        return executeNameQuery(
                cypher,
                classId
        );

    }


    /*
     * -------------------------------------------------------
     * Get Implementations
     * -------------------------------------------------------
     */

    public List<String> getImplementations(
            String interfaceId) {

        String cypher =
                """
                MATCH (source:Entity)
                      -[:IMPLEMENTS]->
                      (target:Entity {id: $nodeId})
                RETURN source.name AS name
                ORDER BY name
                """;


        return executeNameQuery(
                cypher,
                interfaceId
        );

    }


    /*
     * -------------------------------------------------------
     * Generic Name Query
     * -------------------------------------------------------
     *
     * Executes a Cypher query and extracts the "name"
     * property from every returned record.
     */

    private List<String> executeNameQuery(
            String cypher,
            String nodeId) {

        try (
                Session session =
                        driver.session(
                                org.neo4j.driver.SessionConfig
                                        .builder()
                                        .withDatabase(database)
                                        .build()
                        )
        ) {

            return session.executeRead(tx -> {

                var result =
                        tx.run(
                                cypher,
                                Map.of(
                                        "nodeId",
                                        nodeId
                                )
                        );


                return result.list(
                        record ->
                                record
                                        .get("name")
                                        .asString()
                );

            });

        }

    }

}