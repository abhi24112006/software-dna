package com.softwaredna.neo4j;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;

import com.softwaredna.knowledge.EdgeType;


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
     * Get Reachable Nodes
     * -------------------------------------------------------
     */

    public List<String> getReachableNodes(
            String nodeId,
            int depth) {

        if (depth <= 0) {

            return new ArrayList<>();

        }


        String cypher =
                """
                MATCH (start:Entity {id: $nodeId})
                      -[*1..%d]->
                      (target:Entity)

                WHERE target.id <> $nodeId

                RETURN DISTINCT target.name AS name
                ORDER BY name
                """.formatted(depth);


        return executeNameQuery(
                cypher,
                nodeId
        );

    }


    /*
     * -------------------------------------------------------
     * Get Architecture Paths
     * -------------------------------------------------------
     *
     * Traverses all relationship types.
     *
     * Example:
     *
     * StudentService
     *      |
     *      | DEPENDS_ON
     *      v
     *    Teacher
     *      |
     *      | HAS_METHOD
     *      v
     * Teacher.teach()
     *
     */

    public List<String> getArchitecturePaths(
            String nodeId,
            int depth) {

        if (depth <= 0) {

            return new ArrayList<>();

        }


        String cypher =
                """
                MATCH path =
                    (start:Entity {id: $nodeId})
                    -[*1..%d]->
                    (target:Entity)

                WHERE target.id <> $nodeId

                WITH
                    [node IN nodes(path) |
                        node.name] AS nodes,

                    [relationship IN relationships(path) |
                        type(relationship)] AS relationships,

                    length(path) AS pathLength

                RETURN DISTINCT
                    nodes,
                    relationships,
                    pathLength

                ORDER BY pathLength
                """.formatted(depth);


        return executeArchitecturePathQuery(
                cypher,
                nodeId
        );

    }


    /*
     * -------------------------------------------------------
     * Get Relationship-Aware Architecture Paths
     * -------------------------------------------------------
     *
     * Traverses only the relationship types supplied.
     *
     * Example:
     *
     * Set.of(EdgeType.DEPENDS_ON)
     *
     * will only traverse DEPENDS_ON relationships.
     *
     */

    public List<String> getArchitecturePaths(
            String nodeId,
            int depth,
            Set<EdgeType> relationshipTypes) {

        if (depth <= 0) {

            return new ArrayList<>();

        }


        /*
         * If no relationship types were supplied,
         * fall back to the all-relationship traversal.
         */

        if (relationshipTypes == null
                || relationshipTypes.isEmpty()) {

            return getArchitecturePaths(
                    nodeId,
                    depth
            );

        }


        /*
         * Build the Cypher relationship pattern
         * from our controlled EdgeType enum.
         */

        String relationshipPattern =
                relationshipTypes
                        .stream()
                        .map(EdgeType::name)
                        .map(type -> ":" + type)
                        .collect(
                                Collectors.joining("|")
                        );


        String cypher =
                """
                MATCH path =
                    (start:Entity {id: $nodeId})
                    -[%s*1..%d]->
                    (target:Entity)

                WHERE target.id <> $nodeId

                WITH
                    [node IN nodes(path) |
                        node.name] AS nodes,

                    [relationship IN relationships(path) |
                        type(relationship)] AS relationships,

                    length(path) AS pathLength

                RETURN DISTINCT
                    nodes,
                    relationships,
                    pathLength

                ORDER BY pathLength
                """.formatted(
                        relationshipPattern,
                        depth
                );


        return executeArchitecturePathQuery(
                cypher,
                nodeId
        );

    }


    /*
     * -------------------------------------------------------
     * Execute Architecture Path Query
     * -------------------------------------------------------
     */

    private List<String> executeArchitecturePathQuery(
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


                Set<String> paths =
                        new LinkedHashSet<>();


                result.forEachRemaining(
                        record -> {

                            List<String> nodes =
                                    record
                                            .get("nodes")
                                            .asList(
                                                    value ->
                                                            value.asString()
                                            );


                            List<String> relationships =
                                    record
                                            .get("relationships")
                                            .asList(
                                                    value ->
                                                            value.asString()
                                            );


                            if (nodes.size() < 2) {

                                return;

                            }


                            StringBuilder path =
                                    new StringBuilder();


                            path.append(
                                    nodes.get(0)
                            );


                            for (
                                    int i = 0;
                                    i < relationships.size();
                                    i++
                            ) {

                                path.append(
                                        " --"
                                );

                                path.append(
                                        relationships.get(i)
                                );

                                path.append(
                                        "--> "
                                );

                                path.append(
                                        nodes.get(i + 1)
                                );

                            }


                            paths.add(
                                    path.toString()
                            );

                        }
                );


                return new ArrayList<>(paths);

            });

        }

    }


    /*
     * -------------------------------------------------------
     * Generic Name Query
     * -------------------------------------------------------
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

    public List<String> getAllNodes() {

    String cypher =
            """
            MATCH (n:Entity)
            RETURN n.id AS name
            ORDER BY name
            """;

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
                    tx.run(cypher);

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