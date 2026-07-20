package com.softwaredna.graph;

import com.softwaredna.model.EntityReference;
import com.softwaredna.model.Relationship;
import com.softwaredna.model.RelationshipType;
import com.softwaredna.model.RepositoryModel;

public class KnowledgeGraphBuilder {

    public void addRelationship(
            RepositoryModel repository,
            EntityReference source,
            EntityReference target,
            RelationshipType relationshipType) {

        if (source == null || target == null) {
            return;
        }

        Relationship relationship =
                new Relationship(
                        source,
                        target,
                        relationshipType
                );


        repository.getRelationships().add(relationship);

    }

}