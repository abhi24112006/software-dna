package com.softwaredna.neo4j;

import com.softwaredna.knowledge.GraphEdge;
import com.softwaredna.knowledge.GraphNode;
import com.softwaredna.knowledge.KnowledgeGraph;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionContext;

public class Neo4jGraphExporter {

    private final Driver driver;


    public Neo4jGraphExporter(
            Driver driver) {

        this.driver = driver;

    }


    /*
     * -------------------------------------------------------
     * Export Entire Knowledge Graph
     * -------------------------------------------------------
     */

    public void export(
            KnowledgeGraph graph) {

        if (graph == null) {
            return;
        }


        /*
         * ---------------------------------------------------
         * Export Nodes
         * ---------------------------------------------------
         */

        try (Session session = driver.session()) {

            session.executeWrite(tx -> {

                for (GraphNode node :
                        graph.getNodes()) {

                    createNode(
                            tx,
                            node
                    );

                }

                return null;

            });

        }


        /*
         * ---------------------------------------------------
         * Export Relationships
         * ---------------------------------------------------
         */

        try (Session session = driver.session()) {

            session.executeWrite(tx -> {

                for (GraphEdge edge :
                        graph.getEdges()) {

                    createRelationship(
                            tx,
                            edge
                    );

                }

                return null;

            });

        }


        System.out.println();
        System.out.println(
                "Knowledge Graph exported to Neo4j"
        );

        System.out.println(
                "Nodes exported : "
                        + graph.getNodes().size()
        );

        System.out.println(
                "Edges exported : "
                        + graph.getEdges().size()
        );

        System.out.println();

    }


    /*
     * -------------------------------------------------------
     * Create Neo4j Node
     * -------------------------------------------------------
     */

    private void createNode(
            TransactionContext tx,
            GraphNode node) {

        String query =
                """
                MERGE (n:Entity {id: $id})
                SET n.name = $name,
                    n.type = $type
                """;


        tx.run(
                query,
                java.util.Map.of(
                        "id",
                        node.getId(),

                        "name",
                        node.getName(),

                        "type",
                        node.getType().name()
                )
        );

    }


    /*
     * -------------------------------------------------------
     * Create Neo4j Relationship
     * -------------------------------------------------------
     */

    private void createRelationship(
            TransactionContext tx,
            GraphEdge edge) {

        String relationshipType =
                edge.getType().name();


        /*
         * Relationship types come from our controlled
         * EdgeType enum.
         */

        String query =
                """
                MATCH (source:Entity {id: $sourceId})
                MATCH (target:Entity {id: $targetId})
                MERGE (source)-[r:%s]->(target)
                """.formatted(
                        relationshipType
                );


        tx.run(
                query,
                java.util.Map.of(
                        "sourceId",
                        edge.getSource().getId(),

                        "targetId",
                        edge.getTarget().getId()
                )
        );

    }

}