package com.softwaredna.analysis.architecture;

public class ArchitectureGraphEdge {

    private final String source;

    private final String target;

    private final String relationship;


    public ArchitectureGraphEdge(
            String source,
            String target,
            String relationship) {

        this.source = source;

        this.target = target;

        this.relationship = relationship;

    }


    public String getSource() {

        return source;

    }


    public String getTarget() {

        return target;

    }


    public String getRelationship() {

        return relationship;

    }

}