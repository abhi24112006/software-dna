package com.softwaredna.graph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.softwaredna.model.EntityReference;
import com.softwaredna.model.EntityType;
import com.softwaredna.model.RelationshipType;
import com.softwaredna.model.RepositoryModel;
import com.softwaredna.model.SourceEvidence;

class RelationshipDeduplicationTest {

    @Test
    void shouldDeduplicateIdenticalRelationships() {
        RepositoryModel repository = new RepositoryModel();

        EntityReference source =
                new EntityReference(
                        "com.example.A",
                        "A",
                        EntityType.CLASS);

        EntityReference target =
                new EntityReference(
                        "com.example.B",
                        "B",
                        EntityType.CLASS);

        KnowledgeGraphBuilder builder =
                new KnowledgeGraphBuilder();

        builder.addRelationship(
                repository,
                source,
                target,
                RelationshipType.METHOD_CALL_INTERNAL);

        builder.addRelationship(
                repository,
                source,
                target,
                RelationshipType.METHOD_CALL_INTERNAL);

        assertEquals(
                1,
                repository.getRelationships().size(),
                "Identical relationships should be stored only once");
    }

    @Test
    void shouldDeduplicateRelationshipsWithDifferentSourceEvidence() {
        RepositoryModel repository = new RepositoryModel();

        EntityReference source =
                new EntityReference(
                        "com.example.A",
                        "A",
                        EntityType.CLASS);

        EntityReference target =
                new EntityReference(
                        "com.example.B",
                        "B",
                        EntityType.CLASS);

        KnowledgeGraphBuilder builder =
                new KnowledgeGraphBuilder();

        SourceEvidence firstEvidence =
                new SourceEvidence(
                        "A.java",
                        10);

        SourceEvidence secondEvidence =
                new SourceEvidence(
                        "A.java",
                        20);

        builder.addRelationship(
                repository,
                source,
                target,
                RelationshipType.METHOD_CALL_INTERNAL,
                firstEvidence);

        builder.addRelationship(
                repository,
                source,
                target,
                RelationshipType.METHOD_CALL_INTERNAL,
                secondEvidence);

        assertEquals(
                1,
                repository.getRelationships().size(),
                "Source evidence should not make duplicate relationships distinct");
    }
}