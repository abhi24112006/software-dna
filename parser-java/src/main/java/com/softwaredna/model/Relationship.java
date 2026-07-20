package com.softwaredna.model;

import java.util.Objects;

public class Relationship {

    private EntityReference source;

    private EntityReference target;

    private RelationshipType type;

    public Relationship() {

    }

    public Relationship(
            EntityReference source,
            EntityReference target,
            RelationshipType type) {

        this.source = source;
        this.target = target;
        this.type = type;

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

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {

            return true;

        }

        if (obj == null || getClass() != obj.getClass()) {

            return false;

        }

        Relationship other = (Relationship) obj;

        return Objects.equals(source.getId(), other.source.getId())
                && Objects.equals(target.getId(), other.target.getId())
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