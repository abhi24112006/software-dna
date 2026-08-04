package com.softwaredna.knowledge;

import java.util.*;

public class KnowledgeGraph {

    private final Map<String, GraphNode> nodes;

    private final Set<GraphEdge> edges;

    public KnowledgeGraph() {

        nodes = new LinkedHashMap<>();
        edges = new LinkedHashSet<>();

    }

    public void addNode(
            GraphNode node) {

        nodes.put(
                node.getId(),
                node);

    }

    public void addEdge(
            GraphEdge edge) {

        edges.add(edge);

    }

    public Collection<GraphNode> getNodes() {

        return nodes.values();

    }

    public Set<GraphEdge> getEdges() {

        return edges;

    }

    public GraphNode getNode(
            String id) {

        return nodes.get(id);

    }

}