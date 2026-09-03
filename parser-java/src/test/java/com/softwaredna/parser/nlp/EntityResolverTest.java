package com.softwaredna.parser.nlp;

import com.softwaredna.knowledge.GraphNode;
import com.softwaredna.knowledge.KnowledgeGraph;
import com.softwaredna.knowledge.NodeType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EntityResolverTest {

    @Test
    void shouldResolveExactEntityName() {

        KnowledgeGraph graph = new KnowledgeGraph();

        GraphNode controller =
                new GraphNode(
                        "controller-id",
                        "UserController",
                        NodeType.CLASS
                );

        graph.addNode(controller);

        EntityResolver resolver =
                new EntityResolver(graph);

        GraphNode result =
                resolver.resolveUnique("UserController");

        assertNotNull(result);
        assertEquals(
                "UserController",
                result.getName()
        );
    }

    @Test
    void shouldResolveEntityIgnoringCase() {

        KnowledgeGraph graph = new KnowledgeGraph();

        GraphNode service =
                new GraphNode(
                        "service-id",
                        "UserService",
                        NodeType.CLASS
                );

        graph.addNode(service);

        EntityResolver resolver =
                new EntityResolver(graph);

        GraphNode result =
                resolver.resolveUnique("userservice");

        assertNotNull(result);
        assertEquals(
                "UserService",
                result.getName()
        );
    }

    @Test
    void shouldReturnMultipleMatchesForAmbiguousEntity() {

        KnowledgeGraph graph = new KnowledgeGraph();

        GraphNode firstUser =
                new GraphNode(
                        "com.foo.User",
                        "User",
                        NodeType.CLASS
                );

        GraphNode secondUser =
                new GraphNode(
                        "com.bar.User",
                        "User",
                        NodeType.CLASS
                );

        graph.addNode(firstUser);
        graph.addNode(secondUser);

        EntityResolver resolver =
                new EntityResolver(graph);

        List<GraphNode> matches =
                resolver.resolve("User");

        assertEquals(2, matches.size());
    }

    @Test
    void shouldReturnNullForAmbiguousUniqueResolution() {

        KnowledgeGraph graph = new KnowledgeGraph();

        graph.addNode(
                new GraphNode(
                        "com.foo.User",
                        "User",
                        NodeType.CLASS
                )
        );

        graph.addNode(
                new GraphNode(
                        "com.bar.User",
                        "User",
                        NodeType.CLASS
                )
        );

        EntityResolver resolver =
                new EntityResolver(graph);

        assertNull(
                resolver.resolveUnique("User")
        );
    }

    @Test
    void shouldReturnEmptyForUnknownEntity() {

        KnowledgeGraph graph =
                new KnowledgeGraph();

        graph.addNode(
                new GraphNode(
                        "service-id",
                        "UserService",
                        NodeType.CLASS
                )
        );

        EntityResolver resolver =
                new EntityResolver(graph);

        assertTrue(
                resolver.resolve("PaymentService").isEmpty()
        );
    }

    @Test
    void shouldHandleBlankEntity() {

        KnowledgeGraph graph =
                new KnowledgeGraph();

        EntityResolver resolver =
                new EntityResolver(graph);

        assertTrue(
                resolver.resolve("   ").isEmpty()
        );
    }
}