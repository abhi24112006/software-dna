package com.softwaredna.analysis.graph;

import com.softwaredna.model.RelationshipType;

import java.util.LinkedHashSet;
import java.util.Set;

public class TypedDependency {

    private final String sourceId;

    private final String targetId;

    private final Set<RelationshipType> relationshipTypes;

    public TypedDependency(
            String sourceId,
            String targetId) {

        this.sourceId = sourceId;
        this.targetId = targetId;
        this.relationshipTypes = new LinkedHashSet<>();

    }

    public String getSourceId() {
        return sourceId;
    }

    public String getTargetId() {
        return targetId;
    }

    public Set<RelationshipType> getRelationshipTypes() {
        return relationshipTypes;
    }

    public void addRelationshipType(
            RelationshipType relationshipType) {

        relationshipTypes.add(relationshipType);

    }

    public boolean hasRelationshipType(
            RelationshipType relationshipType) {

        return relationshipTypes.contains(relationshipType);

    }

    public int getRelationshipCount() {

        return relationshipTypes.size();

    }

}