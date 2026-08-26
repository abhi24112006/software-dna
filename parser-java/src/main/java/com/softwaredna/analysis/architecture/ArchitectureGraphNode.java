package com.softwaredna.analysis.architecture;

public class ArchitectureGraphNode {

    private final String id;

    private final String name;

    private final ArchitectureLayer layer;


    public ArchitectureGraphNode(
            String id,
            String name,
            ArchitectureLayer layer) {

        this.id = id;

        this.name = name;

        this.layer = layer;

    }


    public String getId() {

        return id;

    }


    public String getName() {

        return name;

    }


    public ArchitectureLayer getLayer() {

        return layer;

    }

}