package com.softwaredna.model;

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

}