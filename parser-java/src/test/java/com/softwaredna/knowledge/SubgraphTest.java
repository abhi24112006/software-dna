package com.softwaredna.knowledge;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SubgraphTest {

    @Test
    void shouldStoreNodesAndEdges() {
        GraphNode user = new GraphNode(
                "class:User",
                "User",
                NodeType.CLASS
        );

        GraphNode userService = new GraphNode(
                "class:UserService",
                "UserService",
                NodeType.CLASS
        );

        GraphEdge edge = new GraphEdge(
                userService,
                user,
                EdgeType.DEPENDS_ON
        );

        Set<GraphNode> nodes = new LinkedHashSet<>();
        nodes.add(user);
        nodes.add(userService);

        Set<GraphEdge> edges = new LinkedHashSet<>();
        edges.add(edge);

        Subgraph subgraph = new Subgraph(nodes, edges);

        assertEquals(2, subgraph.getNodeCount());
        assertEquals(1, subgraph.getEdgeCount());
        assertEquals(nodes, subgraph.getNodes());
        assertEquals(edges, subgraph.getEdges());
    }

    @Test
    void shouldRejectNullNodes() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Subgraph(null, new LinkedHashSet<>())
        );
    }

    @Test
    void shouldRejectNullEdges() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Subgraph(new LinkedHashSet<>(), null)
        );
    }

    @Test
    void shouldReturnUnmodifiableNodes() {
        Set<GraphNode> nodes = new LinkedHashSet<>();

        Subgraph subgraph = new Subgraph(
                nodes,
                new LinkedHashSet<>()
        );

        assertThrows(
                UnsupportedOperationException.class,
                () -> subgraph.getNodes().add(
                        new GraphNode(
                                "class:User",
                                "User",
                                NodeType.CLASS
                        )
                )
        );
    }

    @Test
    void shouldReturnUnmodifiableEdges() {
        Subgraph subgraph = new Subgraph(
                new LinkedHashSet<>(),
                new LinkedHashSet<>()
        );

        GraphNode user = new GraphNode(
                "class:User",
                "User",
                NodeType.CLASS
        );

        GraphNode service = new GraphNode(
                "class:Service",
                "Service",
                NodeType.CLASS
        );

        GraphEdge edge = new GraphEdge(
                service,
                user,
                EdgeType.DEPENDS_ON
        );

        assertThrows(
                UnsupportedOperationException.class,
                () -> subgraph.getEdges().add(edge)
        );
    }
}