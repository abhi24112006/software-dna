package com.softwaredna.parser.nlp;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.softwaredna.knowledge.EdgeType;
import com.softwaredna.knowledge.GraphEdge;
import com.softwaredna.knowledge.GraphNode;
import com.softwaredna.knowledge.KnowledgeGraph;
import com.softwaredna.knowledge.NodeType;

class GroundedContextTest {

    @Test
    void shouldIncludeRelationshipBetweenTargetAndResultNode() {

        GraphNode controller =
                new GraphNode(
                        "class:UserController",
                        "UserController",
                        NodeType.CLASS
                );

        GraphNode service =
                new GraphNode(
                        "class:UserService",
                        "UserService",
                        NodeType.CLASS
                );

        KnowledgeGraph graph =
                new KnowledgeGraph();

        graph.addNode(controller);
        graph.addNode(service);

        graph.addEdge(
                new GraphEdge(
                        controller,
                        service,
                        EdgeType.DEPENDS_ON
                )
        );

        QueryResult queryResult =
                new QueryResult(
                        "What does UserController depend on?",
                        QueryIntent.DEPENDENCIES,
                        controller,
                        List.of(service)
                );

        GroundedContext context =
                new GroundedContext(
                        queryResult,
                        graph
                );

        assertEquals(
                1,
                context.getRelationships().size()
        );

        GroundedRelationship relationship =
                context.getRelationships().get(0);

        assertEquals(
                controller,
                relationship.getSource()
        );

        assertEquals(
                service,
                relationship.getTarget()
        );

        assertEquals(
                EdgeType.DEPENDS_ON,
                relationship.getType()
        );
    }

    @Test
    void shouldIncludeRelationshipBetweenResultNodes() {

        GraphNode controller =
                new GraphNode(
                        "class:UserController",
                        "UserController",
                        NodeType.CLASS
                );

        GraphNode service =
                new GraphNode(
                        "class:UserService",
                        "UserService",
                        NodeType.CLASS
                );

        GraphNode repository =
                new GraphNode(
                        "class:UserRepository",
                        "UserRepository",
                        NodeType.CLASS
                );

        KnowledgeGraph graph =
                new KnowledgeGraph();

        graph.addNode(controller);
        graph.addNode(service);
        graph.addNode(repository);

        graph.addEdge(
                new GraphEdge(
                        controller,
                        service,
                        EdgeType.DEPENDS_ON
                )
        );

        graph.addEdge(
                new GraphEdge(
                        service,
                        repository,
                        EdgeType.DEPENDS_ON
                )
        );

        QueryResult queryResult =
                new QueryResult(
                        "What does UserController depend on?",
                        QueryIntent.DEPENDENCIES,
                        controller,
                        List.of(service, repository)
                );

        GroundedContext context =
                new GroundedContext(
                        queryResult,
                        graph
                );

        assertEquals(
                2,
                context.getRelationships().size()
        );
    }

    @Test
    void shouldExcludeUnrelatedGraphRelationships() {

        GraphNode controller =
                new GraphNode(
                        "class:UserController",
                        "UserController",
                        NodeType.CLASS
                );

        GraphNode service =
                new GraphNode(
                        "class:UserService",
                        "UserService",
                        NodeType.CLASS
                );

        GraphNode repository =
                new GraphNode(
                        "class:UserRepository",
                        "UserRepository",
                        NodeType.CLASS
                );

        GraphNode unrelated =
                new GraphNode(
                        "class:AdminService",
                        "AdminService",
                        NodeType.CLASS
                );

        KnowledgeGraph graph =
                new KnowledgeGraph();

        graph.addNode(controller);
        graph.addNode(service);
        graph.addNode(repository);
        graph.addNode(unrelated);

        graph.addEdge(
                new GraphEdge(
                        controller,
                        service,
                        EdgeType.DEPENDS_ON
                )
        );

        graph.addEdge(
                new GraphEdge(
                        repository,
                        unrelated,
                        EdgeType.DEPENDS_ON
                )
        );

        QueryResult queryResult =
                new QueryResult(
                        "What does UserController depend on?",
                        QueryIntent.DEPENDENCIES,
                        controller,
                        List.of(service)
                );

        GroundedContext context =
                new GroundedContext(
                        queryResult,
                        graph
                );

        assertEquals(
                1,
                context.getRelationships().size()
        );

        GroundedRelationship relationship =
                context.getRelationships().get(0);

        assertEquals(
                "UserController",
                relationship.getSource().getName()
        );

        assertEquals(
                "UserService",
                relationship.getTarget().getName()
        );
    }

    @Test
    void shouldReturnImmutableRelationshipList() {

        GraphNode controller =
                new GraphNode(
                        "class:UserController",
                        "UserController",
                        NodeType.CLASS
                );

        GraphNode service =
                new GraphNode(
                        "class:UserService",
                        "UserService",
                        NodeType.CLASS
                );

        KnowledgeGraph graph =
                new KnowledgeGraph();

        graph.addNode(controller);
        graph.addNode(service);

        graph.addEdge(
                new GraphEdge(
                        controller,
                        service,
                        EdgeType.DEPENDS_ON
                )
        );

        QueryResult queryResult =
                new QueryResult(
                        "What does UserController depend on?",
                        QueryIntent.DEPENDENCIES,
                        controller,
                        List.of(service)
                );

        GroundedContext context =
                new GroundedContext(
                        queryResult,
                        graph
                );

        assertThrows(
                UnsupportedOperationException.class,
                () -> context.getRelationships().clear()
        );
    }

    @Test
    void shouldRejectNullKnowledgeGraph() {

        GraphNode controller =
                new GraphNode(
                        "class:UserController",
                        "UserController",
                        NodeType.CLASS
                );

        QueryResult queryResult =
                new QueryResult(
                        "What does UserController depend on?",
                        QueryIntent.DEPENDENCIES,
                        controller,
                        List.of()
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> new GroundedContext(
                        queryResult,
                        null
                )
        );
    }

    @Test
    void shouldPreserveExistingConstructorBehavior() {

        GraphNode controller =
                new GraphNode(
                        "class:UserController",
                        "UserController",
                        NodeType.CLASS
                );

        QueryResult queryResult =
                new QueryResult(
                        "What does UserController depend on?",
                        QueryIntent.DEPENDENCIES,
                        controller,
                        List.of()
                );

        GroundedContext context =
                new GroundedContext(queryResult);

        assertTrue(
                context.getRelationships().isEmpty()
        );
    }
}