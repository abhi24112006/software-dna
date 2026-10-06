package com.softwaredna.knowledge;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Represents a focused subgraph extracted from the full KnowledgeGraph.
 *
 * A subgraph contains only the nodes and edges relevant to a particular
 * query or analysis operation.
 */
public class Subgraph {

    private final Set<GraphNode> nodes;
    private final Set<GraphEdge> edges;

    public Subgraph(Set<GraphNode> nodes, Set<GraphEdge> edges) {
        if (nodes == null) {
            throw new IllegalArgumentException("Nodes cannot be null.");
        }

        if (edges == null) {
            throw new IllegalArgumentException("Edges cannot be null.");
        }

        this.nodes = Collections.unmodifiableSet(
                new LinkedHashSet<>(nodes)
        );

        this.edges = Collections.unmodifiableSet(
                new LinkedHashSet<>(edges)
        );
    }

    public Set<GraphNode> getNodes() {
        return nodes;
    }

    public Set<GraphEdge> getEdges() {
        return edges;
    }

    public int getNodeCount() {
        return nodes.size();
    }

    public int getEdgeCount() {
        return edges.size();
    }

    @Override
    public String toString() {
        return "Subgraph{" +
                "nodes=" + nodes.size() +
                ", edges=" + edges.size() +
                '}';
    }
}