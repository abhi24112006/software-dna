package com.softwaredna.analysis.coupling;

import com.softwaredna.model.RelationshipType;

import java.util.EnumMap;
import java.util.Map;

public class CouplingMetrics {

    /*
     * Total number of distinct coupled classes.
     */
    private int cbo;

    /*
     * Number of couplings grouped by
     * relationship type.
     */
    private final Map<RelationshipType, Integer> relationshipCounts;

    public CouplingMetrics() {

        relationshipCounts =
                new EnumMap<>(RelationshipType.class);

        for (RelationshipType type : RelationshipType.values()) {

            relationshipCounts.put(type, 0);

        }

    }

    public int getCbo() {
        return cbo;
    }

    public void setCbo(int cbo) {
        this.cbo = cbo;
    }

    public void incrementRelationshipCount(
            RelationshipType type) {

        relationshipCounts.put(
                type,
                relationshipCounts.get(type) + 1
        );

    }

    public int getRelationshipCount(
            RelationshipType type) {

        return relationshipCounts.get(type);

    }

    public Map<RelationshipType, Integer> getRelationshipCounts() {

        return relationshipCounts;

    }

}