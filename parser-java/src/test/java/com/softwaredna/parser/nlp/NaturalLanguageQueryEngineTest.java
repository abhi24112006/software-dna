package com.softwaredna.parser.nlp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.softwaredna.knowledge.EdgeType;
import com.softwaredna.knowledge.GraphEdge;
import com.softwaredna.knowledge.GraphNode;
import com.softwaredna.knowledge.KnowledgeGraph;
import com.softwaredna.knowledge.NodeType;

class NaturalLanguageQueryEngineTest {

    private KnowledgeGraph graph;
    private NaturalLanguageQueryEngine engine;

    private GraphNode controller;
    private GraphNode service;
    private GraphNode repository;

    @BeforeEach
    void setUp() {

        graph = new KnowledgeGraph();

        controller = new GraphNode(
                "class:UserController",
                "UserController",
                NodeType.CLASS
        );

        service = new GraphNode(
                "class:UserService",
                "UserService",
                NodeType.CLASS
        );

        repository = new GraphNode(
                "class:UserRepository",
                "UserRepository",
                NodeType.CLASS
        );

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

        engine = new NaturalLanguageQueryEngine(graph);
    }

    @Test
    void shouldAnswerDependenciesQuestion() {

        QueryResult result =
                engine.ask(
                        "What does UserController depend on?"
                );

        assertEquals(
                "What does UserController depend on?",
                result.getOriginalQuestion()
        );

        assertEquals(
                QueryIntent.DEPENDENCIES,
                result.getIntent()
        );

        assertEquals(
                "UserController",
                result.getEntity().getName()
        );

        assertEquals(1, result.getResultCount());

        assertEquals(
                "UserService",
                result.getNodes().get(0).getName()
        );
    }

    @Test
    void shouldAnswerDependentsQuestion() {

        QueryResult result =
                engine.ask(
                        "Who depends on UserService?"
                );

        assertEquals(
                QueryIntent.DEPENDENTS,
                result.getIntent()
        );

        assertEquals(
                "UserService",
                result.getEntity().getName()
        );

        assertEquals(1, result.getResultCount());

        assertEquals(
                "UserController",
                result.getNodes().get(0).getName()
        );
    }

    @Test
    void shouldHandleCaseInsensitiveEntity() {

        QueryResult result =
                engine.ask(
                        "What does usercontroller depend on?"
                );

        assertEquals(
                "UserController",
                result.getEntity().getName()
        );

        assertEquals(1, result.getResultCount());

        assertEquals(
                "UserService",
                result.getNodes().get(0).getName()
        );
    }

    @Test
    void shouldRejectUnknownQuestion() {

        assertThrows(
                IllegalArgumentException.class,
                () -> engine.ask(
                        "Tell me something interesting."
                )
        );
    }

    @Test
    void shouldRejectUnknownEntity() {

        assertThrows(
                IllegalArgumentException.class,
                () -> engine.ask(
                        "What does PaymentController depend on?"
                )
        );
    }

    @Test
    void shouldRejectBlankQuestion() {

        assertThrows(
                IllegalArgumentException.class,
                () -> engine.ask("   ")
        );
    }
}