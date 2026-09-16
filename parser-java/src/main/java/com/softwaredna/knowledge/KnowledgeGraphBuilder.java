package com.softwaredna.knowledge;

import com.softwaredna.knowledge.builder.EdgeBuilder;
import com.softwaredna.knowledge.builder.NodeBuilder;
import com.softwaredna.model.EntityReference;
import com.softwaredna.model.Relationship;
import com.softwaredna.model.RelationshipType;
import com.softwaredna.model.RepositoryModel;
import com.softwaredna.model.SourceEvidence;

public class KnowledgeGraphBuilder {

    private final NodeBuilder nodeBuilder;
    private final EdgeBuilder edgeBuilder;

    public KnowledgeGraphBuilder() {

        nodeBuilder =
                new NodeBuilder();

        edgeBuilder =
                new EdgeBuilder();
    }

    /**
     * Builds the complete in-memory Knowledge Graph
     * from the parsed repository model.
     *
     * @param repository parsed repository model
     * @return constructed knowledge graph
     */
    public KnowledgeGraph build(
            RepositoryModel repository) {

        if (repository == null) {
            throw new IllegalArgumentException(
                    "RepositoryModel cannot be null."
            );
        }

        KnowledgeGraph graph =
                new KnowledgeGraph();

        nodeBuilder.buildNodes(
                repository,
                graph
        );

        edgeBuilder.buildEdges(
                repository,
                graph
        );

        System.out.println();
        System.out.println("Knowledge Graph Built");
        System.out.println(
                "Nodes : " + graph.getNodes().size()
        );
        System.out.println(
                "Edges : " + graph.getEdges().size()
        );
        System.out.println();

        return graph;
    }

    /**
     * Adds a relationship without source evidence.
     *
     * <p>Retained for backward compatibility.</p>
     */
    public void addRelationship(
            RepositoryModel repository,
            EntityReference source,
            EntityReference target,
            RelationshipType relationshipType) {

        addRelationship(
                repository,
                source,
                target,
                relationshipType,
                null
        );
    }

    /**
     * Adds a relationship with source evidence.
     */
    public void addRelationship(
            RepositoryModel repository,
            EntityReference source,
            EntityReference target,
            RelationshipType relationshipType,
            SourceEvidence sourceEvidence) {

        if (repository == null
                || source == null
                || target == null
                || relationshipType == null) {

            return;
        }

        Relationship relationship =
                new Relationship(
                        source,
                        target,
                        relationshipType,
                        sourceEvidence
                );

        repository.getRelationships()
                .add(relationship);
    }
}