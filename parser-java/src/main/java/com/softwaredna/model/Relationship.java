package com.softwaredna.model;

import java.util.Objects;

public class Relationship {

    private EntityReference source;

    private EntityReference target;

    private RelationshipType type;

    private SourceEvidence sourceEvidence;

    public Relationship() {

    }

    /**
     * Creates a relationship without source evidence.
     *
     * <p>This constructor is retained for backward compatibility.
     * Existing relationship creation code can continue to work while
     * source evidence is introduced incrementally.</p>
     */
    public Relationship(
            EntityReference source,
            EntityReference target,
            RelationshipType type) {

        this.source = source;
        this.target = target;
        this.type = type;
    }

    /**
     * Creates a relationship with source evidence.
     */
    public Relationship(
            EntityReference source,
            EntityReference target,
            RelationshipType type,
            SourceEvidence sourceEvidence) {

        this.source = source;
        this.target = target;
        this.type = type;
        this.sourceEvidence = sourceEvidence;
    }

    public EntityReference getSource() {

        return source;
    }

    public void setSource(
            EntityReference source) {

        this.source = source;
    }

    public EntityReference getTarget() {

        return target;
    }

    public void setTarget(
            EntityReference target) {

        this.target = target;
    }

    public RelationshipType getType() {

        return type;
    }

    public void setType(
            RelationshipType type) {

        this.type = type;
    }

    public SourceEvidence getSourceEvidence() {

        return sourceEvidence;
    }

    public void setSourceEvidence(
            SourceEvidence sourceEvidence) {

        this.sourceEvidence = sourceEvidence;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null
                || getClass() != obj.getClass()) {

            return false;
        }

        Relationship other =
                (Relationship) obj;

        /*
         * Source evidence is deliberately NOT part of
         * relationship identity.
         *
         * A relationship is identified by:
         * source + target + type.
         *
         * Evidence describes where that relationship
         * was discovered.
         */
        return Objects.equals(
                        source.getId(),
                        other.source.getId()
                )
                && Objects.equals(
                        target.getId(),
                        other.target.getId()
                )
                && type == other.type;
    }

    @Override
    public int hashCode() {

        return Objects.hash(
                source.getId(),
                target.getId(),
                type
        );
    }
}