package com.softwaredna.analysis.architecture;

public class ArchitectureAnomaly {

    private final String source;

    private final ArchitectureLayer sourceLayer;

    private final String relationship;

    private final String target;

    private final ArchitectureLayer targetLayer;

    private final String description;

    private final String severity;


    public ArchitectureAnomaly(
            String source,
            ArchitectureLayer sourceLayer,
            String relationship,
            String target,
            ArchitectureLayer targetLayer,
            String description,
            String severity) {

        this.source = source;

        this.sourceLayer =
                sourceLayer;

        this.relationship =
                relationship;

        this.target =
                target;

        this.targetLayer =
                targetLayer;

        this.description =
                description;

        this.severity =
                severity;

    }


    public String getSource() {

        return source;

    }


    public ArchitectureLayer getSourceLayer() {

        return sourceLayer;

    }


    public String getRelationship() {

        return relationship;

    }


    public String getTarget() {

        return target;

    }


    public ArchitectureLayer getTargetLayer() {

        return targetLayer;

    }


    public String getDescription() {

        return description;

    }


    public String getSeverity() {

        return severity;

    }


    @Override
    public String toString() {

        return source
                + " --"
                + relationship
                + "--> "
                + target
                + " : "
                + description
                + " ["
                + severity
                + "]";

    }

}