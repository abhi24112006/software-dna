package com.softwaredna.knowledge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class SubgraphRetrieverTest {

    @Test
    void shouldRetrieveOutgoingEdgesAndNeighbors() {
        KnowledgeGraph graph = new KnowledgeGraph();

        GraphNode controller = new GraphNode(
                "class:UserController",
                "UserController",
                NodeType.CLASS
        );

        GraphNode service = new GraphNode(
                "class:UserService",
                "UserService",
                NodeType.CLASS
        );

        GraphEdge edge = new GraphEdge(
                controller,
                service,
                EdgeType.DEPENDS_ON
        );

        graph.addNode(controller);
        graph.addNode(service);
        graph.addEdge(edge);

        SubgraphRetriever retriever = new SubgraphRetriever(graph);

        Subgraph subgraph = retriever.retrieve(controller);

        assertEquals(2, subgraph.getNodeCount());
        assertEquals(1, subgraph.getEdgeCount());
        assertTrue(subgraph.getNodes().contains(controller));
        assertTrue(subgraph.getNodes().contains(service));
        assertTrue(subgraph.getEdges().contains(edge));
    }

    @Test
    void shouldRetrieveIncomingEdgesAndNeighbors() {
        KnowledgeGraph graph = new KnowledgeGraph();

        GraphNode service = new GraphNode(
                "class:UserService",
                "UserService",
                NodeType.CLASS
        );

        GraphNode repository = new GraphNode(
                "class:UserRepository",
                "UserRepository",
                NodeType.CLASS
        );

        GraphEdge edge = new GraphEdge(
                service,
                repository,
                EdgeType.DEPENDS_ON
        );

        graph.addNode(service);
        graph.addNode(repository);
        graph.addEdge(edge);

        SubgraphRetriever retriever = new SubgraphRetriever(graph);

        Subgraph subgraph = retriever.retrieve(repository);

        assertEquals(2, subgraph.getNodeCount());
        assertEquals(1, subgraph.getEdgeCount());
        assertTrue(subgraph.getNodes().contains(repository));
        assertTrue(subgraph.getNodes().contains(service));
        assertTrue(subgraph.getEdges().contains(edge));
    }

    @Test
    void shouldRetrieveIncomingAndOutgoingEdges() {
        KnowledgeGraph graph = new KnowledgeGraph();

        GraphNode controller = new GraphNode(
                "class:UserController",
                "UserController",
                NodeType.CLASS
        );

        GraphNode service = new GraphNode(
                "class:UserService",
                "UserService",
                NodeType.CLASS
        );

        GraphNode repository = new GraphNode(
                "class:UserRepository",
                "UserRepository",
                NodeType.CLASS
        );

        GraphEdge outgoing = new GraphEdge(
                controller,
                service,
                EdgeType.DEPENDS_ON
        );

        GraphEdge incoming = new GraphEdge(
                repository,
                controller,
                EdgeType.DEPENDS_ON
        );

        graph.addNode(controller);
        graph.addNode(service);
        graph.addNode(repository);

        graph.addEdge(outgoing);
        graph.addEdge(incoming);

        SubgraphRetriever retriever = new SubgraphRetriever(graph);

        Subgraph subgraph = retriever.retrieve(controller);

        assertEquals(3, subgraph.getNodeCount());
        assertEquals(2, subgraph.getEdgeCount());

        assertTrue(subgraph.getNodes().contains(controller));
        assertTrue(subgraph.getNodes().contains(service));
        assertTrue(subgraph.getNodes().contains(repository));

        assertTrue(subgraph.getEdges().contains(outgoing));
        assertTrue(subgraph.getEdges().contains(incoming));
    }

    @Test
    void shouldExcludeUnconnectedNodes() {
        KnowledgeGraph graph = new KnowledgeGraph();

        GraphNode controller = new GraphNode(
                "class:UserController",
                "UserController",
                NodeType.CLASS
        );

        GraphNode service = new GraphNode(
                "class:UserService",
                "UserService",
                NodeType.CLASS
        );

        GraphNode unrelated = new GraphNode(
                "class:PaymentService",
                "PaymentService",
                NodeType.CLASS
        );

        GraphEdge edge = new GraphEdge(
                controller,
                service,
                EdgeType.DEPENDS_ON
        );

        graph.addNode(controller);
        graph.addNode(service);
        graph.addNode(unrelated);
        graph.addEdge(edge);

        SubgraphRetriever retriever = new SubgraphRetriever(graph);

        Subgraph subgraph = retriever.retrieve(controller);

        assertEquals(2, subgraph.getNodeCount());
        assertEquals(1, subgraph.getEdgeCount());

        assertTrue(subgraph.getNodes().contains(controller));
        assertTrue(subgraph.getNodes().contains(service));
        assertFalse(subgraph.getNodes().contains(unrelated));
    }

    @Test
    void shouldReturnOnlyTargetWhenNoEdgesExist() {
        KnowledgeGraph graph = new KnowledgeGraph();

        GraphNode controller = new GraphNode(
                "class:UserController",
                "UserController",
                NodeType.CLASS
        );

        graph.addNode(controller);

        SubgraphRetriever retriever = new SubgraphRetriever(graph);

        Subgraph subgraph = retriever.retrieve(controller);

        assertEquals(1, subgraph.getNodeCount());
        assertEquals(0, subgraph.getEdgeCount());
        assertTrue(subgraph.getNodes().contains(controller));
    }

    @Test
    void shouldRejectNullTarget() {
        KnowledgeGraph graph = new KnowledgeGraph();

        SubgraphRetriever retriever = new SubgraphRetriever(graph);

        assertThrows(
                IllegalArgumentException.class,
                () -> retriever.retrieve(null)
        );
    }

    @Test
    void shouldRejectNullGraph() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new SubgraphRetriever(null)
        );
    }
}