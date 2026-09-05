package com.softwaredna.parser.nlp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.softwaredna.knowledge.EdgeType;
import com.softwaredna.knowledge.GraphNode;
import com.softwaredna.knowledge.KnowledgeGraph;
import com.softwaredna.knowledge.NodeType;

class EntityResolverTest {

    private KnowledgeGraph graph;
    private EntityResolver resolver;

    @BeforeEach
    void setUp() {

        graph = new KnowledgeGraph();

        GraphNode userClass =
                new GraphNode(
                        "User",
                        "User",
                        NodeType.CLASS
                );

        GraphNode userMethod =
                new GraphNode(
                        "User.getName()",
                        "User.getName()",
                        NodeType.METHOD
                );

        GraphNode userController =
                new GraphNode(
                        "UserController",
                        "UserController",
                        NodeType.CLASS
                );

        graph.addNode(userClass);
        graph.addNode(userMethod);
        graph.addNode(userController);

        graph.addEdge(
                new com.softwaredna.knowledge.GraphEdge(
                        userController,
                        userClass,
                        EdgeType.DEPENDS_ON
                )
        );

        resolver = new EntityResolver(graph);
    }

    @Test
    void shouldResolveUserAsClass() {

        GraphNode result =
                resolver.resolveUnique(
                        "User",
                        NodeType.CLASS
                );

        assertNotNull(result);
        assertEquals("User", result.getName());
        assertEquals(NodeType.CLASS, result.getType());
    }

    @Test
    void shouldResolveUserGetNameAsMethod() {

        GraphNode result =
                resolver.resolveUnique(
                        "User.getName()",
                        NodeType.METHOD
                );

        assertNotNull(result);
        assertEquals(
                "User.getName()",
                result.getName()
        );
        assertEquals(
                NodeType.METHOD,
                result.getType()
        );
    }

    @Test
    void shouldNotConfuseClassAndMethodWithSameSimpleName() {

        GraphNode classResult =
                resolver.resolveUnique(
                        "User",
                        NodeType.CLASS
                );

        GraphNode methodResult =
                resolver.resolveUnique(
                        "User.getName()",
                        NodeType.METHOD
                );

        assertNotNull(classResult);
        assertNotNull(methodResult);

        assertEquals(
                NodeType.CLASS,
                classResult.getType()
        );

        assertEquals(
                NodeType.METHOD,
                methodResult.getType()
        );
    }
}