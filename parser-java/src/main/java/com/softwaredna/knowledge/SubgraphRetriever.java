package com.softwaredna.knowledge;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Retrieves focused subgraphs from the complete KnowledgeGraph.
 *
 * The initial retrieval strategy returns the target node together with
 * every node and edge directly connected to it.
 */
public class SubgraphRetriever {

    private final KnowledgeGraph graph;

    public SubgraphRetriever(KnowledgeGraph graph) {
        if (graph == null) {
            throw new IllegalArgumentException(
                    "KnowledgeGraph cannot be null."
            );
        }

        this.graph = graph;
    }

    /**
     * Retrieves the one-hop neighborhood around the target node.
     *
     * Both incoming and outgoing edges are included.
     *
     * @param targetNode target node around which the subgraph is built
     * @return one-hop subgraph containing the target and its neighbors
     */
    public Subgraph retrieve(GraphNode targetNode) {
        if (targetNode == null) {
            throw new IllegalArgumentException(
                    "Target node cannot be null."
            );
        }

        Set<GraphNode> nodes = new LinkedHashSet<>();
        Set<GraphEdge> edges = new LinkedHashSet<>();

        nodes.add(targetNode);

        for (GraphEdge edge : graph.getEdges()) {

            if (edge.getSource().equals(targetNode)) {
                edges.add(edge);
                nodes.add(edge.getTarget());
            }

            else if (edge.getTarget().equals(targetNode)) {
                edges.add(edge);
                nodes.add(edge.getSource());
            }
        }

        return new Subgraph(nodes, edges);
    }
}