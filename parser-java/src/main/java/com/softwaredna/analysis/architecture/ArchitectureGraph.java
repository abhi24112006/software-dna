package com.softwaredna.analysis.architecture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArchitectureGraph {

    private final List<ArchitectureGraphNode> nodes;

    private final List<ArchitectureGraphEdge> edges;


    public ArchitectureGraph(
            List<ArchitectureGraphNode> nodes,
            List<ArchitectureGraphEdge> edges) {

        this.nodes =
                Collections.unmodifiableList(
                        new ArrayList<>(nodes)
                );

        this.edges =
                Collections.unmodifiableList(
                        new ArrayList<>(edges)
                );

    }


    public List<ArchitectureGraphNode>
    getNodes() {

        return nodes;

    }


    public List<ArchitectureGraphEdge>
    getEdges() {

        return edges;

    }

}