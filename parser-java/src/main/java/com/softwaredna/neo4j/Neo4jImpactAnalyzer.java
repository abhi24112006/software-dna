package com.softwaredna.neo4j;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;

import com.softwaredna.knowledge.EdgeType;


public class Neo4jImpactAnalyzer {

    private final Driver driver;

    private final String database;


    public Neo4jImpactAnalyzer(
            Driver driver,
            String database) {

        this.driver = driver;
        this.database = database;

    }


    /*
     * -------------------------------------------------------
     * Standard Impact Analysis
     * -------------------------------------------------------
     */

    public List<String> getImpact(
            String nodeId) {

        String cypher =
                """
                MATCH (source:Entity)
                      -[:DEPENDS_ON]->
                      (target:Entity {id: $nodeId})
                RETURN source.id AS id,
                       source.name AS name
                ORDER BY source.name
                """;

        return executeNameQuery(
                cypher,
                nodeId
        );

    }


    /*
     * -------------------------------------------------------
     * Method Impact Analysis
     * -------------------------------------------------------
     */

    public List<String> getMethodImpact(
            String methodId) {

        String cypher =
                """
                MATCH (caller:Entity)
                      -[:CALLS]->
                      (target:Entity {id: $nodeId})
                RETURN caller.id AS id,
                       caller.name AS name
                ORDER BY caller.name
                """;

        return executeNameQuery(
                cypher,
                methodId
        );

    }


    /*
     * -------------------------------------------------------
     * Explainable Impact Paths
     * -------------------------------------------------------
     *
     * Finds paths leading INTO the changed node.
     *
     * Example:
     *
     * Student.study()
     *       |
     *       | CALLS
     *       v
     * Teacher.teach()
     *
     * If Teacher.teach() changes:
     *
     * Teacher.teach()
     *       ^
     *       |
     *     CALLS
     *       |
     * Student.study()
     *
     */

    public List<String> getImpactPaths(
            String nodeId,
            int depth,
            Set<EdgeType> relationshipTypes) {

        if (nodeId == null
                || nodeId.isBlank()
                || depth <= 0) {

            return new ArrayList<>();

        }


        if (relationshipTypes == null
                || relationshipTypes.isEmpty()) {

            return new ArrayList<>();

        }


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
                    <-[%s*1..%d]-
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


        return executeImpactPathQuery(
                cypher,
                nodeId
        );

    }


    /*
     * -------------------------------------------------------
     * Containment-Aware Impact Analysis
     * -------------------------------------------------------
     */

    public List<String> getContainmentAwareImpact(
            String nodeId) {

        List<String> impactedNodes =
                new ArrayList<>();


        /*
         * Verify starting node exists.
         */

        GraphEntity startNode =
                findNode(nodeId);


        if (startNode == null) {

            return impactedNodes;

        }


        /*
         * BFS structures.
         */

        Queue<GraphEntity> queue =
                new LinkedList<>();

        Set<String> visited =
                new LinkedHashSet<>();


        queue.add(startNode);

        visited.add(
                startNode.id
        );


        /*
         * BFS
         */

        while (!queue.isEmpty()) {

            GraphEntity current =
                    queue.poll();


            /*
             * =================================================
             * CLASS
             * =================================================
             */

            if ("CLASS".equals(
                    current.type)) {

                /*
                 * Find classes that depend on this class.
                 */

                List<GraphEntity> dependents =
                        getIncoming(
                                current.id,
                                "DEPENDS_ON"
                        );


                addNodes(
                        dependents,
                        queue,
                        visited,
                        impactedNodes
                );


                /*
                 * Only expand the ORIGINAL changed class
                 * into its methods.
                 */

                if (current.id.equals(nodeId)) {

                    List<GraphEntity> methods =
                            getOutgoing(
                                    current.id,
                                    "HAS_METHOD"
                            );


                    addNodes(
                            methods,
                            queue,
                            visited,
                            impactedNodes
                    );

                }

            }


            /*
             * =================================================
             * METHOD
             * =================================================
             */

            else if ("METHOD".equals(
                    current.type)) {

                /*
                 * Find methods that call this method.
                 */

                List<GraphEntity> callers =
                        getIncoming(
                                current.id,
                                "CALLS"
                        );


                addNodes(
                        callers,
                        queue,
                        visited,
                        impactedNodes
                );


                /*
                 * Find the class containing this method.
                 *
                 * Class -> HAS_METHOD -> Method
                 *
                 * Therefore traverse backwards.
                 */

                List<GraphEntity> ownerClasses =
                        getIncoming(
                                current.id,
                                "HAS_METHOD"
                        );


                addNodes(
                        ownerClasses,
                        queue,
                        visited,
                        impactedNodes
                );

            }

        }


        return impactedNodes;

    }


    /*
     * -------------------------------------------------------
     * Find Node
     * -------------------------------------------------------
     */

    private GraphEntity findNode(
            String nodeId) {

        String cypher =
                """
                MATCH (n:Entity {id: $nodeId})
                RETURN n.id AS id,
                       n.name AS name,
                       n.type AS type
                """;


        try (
                Session session =
                        createSession()
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


                if (!result.hasNext()) {

                    return null;

                }


                var record =
                        result.single();


                return new GraphEntity(
                        record.get("id").asString(),
                        record.get("name").asString(),
                        record.get("type").asString()
                );

            });

        }

    }


    /*
     * -------------------------------------------------------
     * Execute Impact Path Query
     * -------------------------------------------------------
     */

    private List<String> executeImpactPathQuery(
            String cypher,
            String nodeId) {

        try (
                Session session =
                        createSession()
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


                            /*
                             * Since this is impact analysis,
                             * display the changed node first.
                             *
                             * Neo4j returns the path in the
                             * traversal direction, so reverse
                             * the representation.
                             */

                            path.append(
                                    nodes.get(
                                            nodes.size() - 1
                                    )
                            );


                            for (
                                    int i =
                                            relationships.size() - 1;
                                    i >= 0;
                                    i--
                            ) {

                                path.append(
                                        " <--"
                                );

                                path.append(
                                        relationships.get(i)
                                );

                                path.append(
                                        "-- "
                                );

                                path.append(
                                        nodes.get(i)
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
     * Get Incoming Relationship
     * -------------------------------------------------------
     *
     * source -[:TYPE]-> current
     */

    private List<GraphEntity> getIncoming(
            String nodeId,
            String relationshipType) {

        String cypher =
                """
                MATCH (source:Entity)
                      -[r:%s]->
                      (target:Entity {id: $nodeId})
                RETURN source.id AS id,
                       source.name AS name,
                       source.type AS type
                ORDER BY source.name
                """.formatted(
                        relationshipType
                );


        return executeEntityQuery(
                cypher,
                nodeId
        );

    }


    /*
     * -------------------------------------------------------
     * Get Outgoing Relationship
     * -------------------------------------------------------
     *
     * current -[:TYPE]-> target
     */

    private List<GraphEntity> getOutgoing(
            String nodeId,
            String relationshipType) {

        String cypher =
                """
                MATCH (source:Entity {id: $nodeId})
                      -[r:%s]->
                      (target:Entity)
                RETURN target.id AS id,
                       target.name AS name,
                       target.type AS type
                ORDER BY target.name
                """.formatted(
                        relationshipType
                );


        return executeEntityQuery(
                cypher,
                nodeId
        );

    }


    /*
     * -------------------------------------------------------
     * Execute Entity Query
     * -------------------------------------------------------
     */

    private List<GraphEntity> executeEntityQuery(
            String cypher,
            String nodeId) {

        try (
                Session session =
                        createSession()
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
                                new GraphEntity(
                                        record.get("id")
                                                .asString(),

                                        record.get("name")
                                                .asString(),

                                        record.get("type")
                                                .asString()
                                )
                );

            });

        }

    }


    /*
     * -------------------------------------------------------
     * Execute Simple Name Query
     * -------------------------------------------------------
     */

    private List<String> executeNameQuery(
            String cypher,
            String nodeId) {

        try (
                Session session =
                        createSession()
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
                                record.get("name")
                                        .asString()
                );

            });

        }

    }


    /*
     * -------------------------------------------------------
     * Add Nodes To BFS
     * -------------------------------------------------------
     */

    private void addNodes(
            List<GraphEntity> nodes,
            Queue<GraphEntity> queue,
            Set<String> visited,
            List<String> impactedNodes) {

        for (GraphEntity node :
                nodes) {

            if (visited.contains(
                    node.id)) {

                continue;

            }


            visited.add(
                    node.id
            );


            impactedNodes.add(
                    node.name
            );


            queue.add(
                    node
            );

        }

    }


    /*
     * -------------------------------------------------------
     * Create Neo4j Session
     * -------------------------------------------------------
     */

    private Session createSession() {

        return driver.session(
                SessionConfig.builder()
                        .withDatabase(database)
                        .build()
        );

    }


    /*
     * -------------------------------------------------------
     * Internal Graph Entity
     * -------------------------------------------------------
     */

    private static class GraphEntity {

        private final String id;

        private final String name;

        private final String type;


        private GraphEntity(
                String id,
                String name,
                String type) {

            this.id = id;
            this.name = name;
            this.type = type;

        }

    }

}