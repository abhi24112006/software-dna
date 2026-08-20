package com.softwaredna.analysis.architecture;

public class ArchitectureEvidence {

    private final String source;

    private final String relationship;

    private final String target;

    private final String explanation;


    public ArchitectureEvidence(
            String source,
            String relationship,
            String target,
            String explanation) {

        this.source = source;
        this.relationship = relationship;
        this.target = target;
        this.explanation = explanation;

    }


    public String getSource() {
        return source;
    }


    public String getRelationship() {
        return relationship;
    }


    public String getTarget() {
        return target;
    }


    public String getExplanation() {
        return explanation;
    }


    @Override
    public String toString() {

        return source
                + " --"
                + relationship
                + "--> "
                + target
                + " : "
                + explanation;

    }

}