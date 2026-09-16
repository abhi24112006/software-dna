package com.softwaredna.graph;

import com.softwaredna.model.EntityReference;
import com.softwaredna.model.Relationship;
import com.softwaredna.model.RelationshipType;
import com.softwaredna.model.RepositoryModel;
import com.softwaredna.model.SourceEvidence;

public class KnowledgeGraphBuilder {

    /**
     * Adds a relationship without source evidence.
     *
     * <p>This method is retained for backward compatibility.
     * Existing relationship extraction code can continue to
     * create relationships without evidence while evidence
     * is introduced incrementally.</p>
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
     *
     * @param repository repository model
     * @param source source entity
     * @param target target entity
     * @param relationshipType relationship type
     * @param sourceEvidence evidence describing where the
     *                       relationship was discovered
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