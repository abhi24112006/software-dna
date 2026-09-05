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
    private GraphNode user;

    private GraphNode controllerCreate;
    private GraphNode serviceCreateUser;
    private GraphNode repositorySave;

    @BeforeEach
    void setUp() {

        graph = new KnowledgeGraph();

        // -------------------------------------------------
        // Classes
        // -------------------------------------------------

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

        user = new GraphNode(
                "class:User",
                "User",
                NodeType.CLASS
        );

        // -------------------------------------------------
        // Methods
        // -------------------------------------------------

        controllerCreate = new GraphNode(
                "method:UserController.create",
                "UserController.create()",
                NodeType.METHOD
        );

        serviceCreateUser = new GraphNode(
                "method:UserService.create_user",
                "UserService.create_user()",
                NodeType.METHOD
        );

        repositorySave = new GraphNode(
                "method:UserRepository.save",
                "UserRepository.save()",
                NodeType.METHOD
        );

        // -------------------------------------------------
        // Add nodes
        // -------------------------------------------------

        graph.addNode(controller);
        graph.addNode(service);
        graph.addNode(repository);
        graph.addNode(user);

        graph.addNode(controllerCreate);
        graph.addNode(serviceCreateUser);
        graph.addNode(repositorySave);

        // -------------------------------------------------
        // Class dependencies
        // -------------------------------------------------

        graph.addEdge(
                new GraphEdge(
                        controller,
                        service,
                        EdgeType.DEPENDS_ON
                )
        );

        graph.addEdge(
                new GraphEdge(
                        controller,
                        user,
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

        graph.addEdge(
                new GraphEdge(
                        service,
                        user,
                        EdgeType.DEPENDS_ON
                )
        );

        graph.addEdge(
                new GraphEdge(
                        repository,
                        user,
                        EdgeType.DEPENDS_ON
                )
        );

        // -------------------------------------------------
        // Class → Method containment
        // -------------------------------------------------

        graph.addEdge(
                new GraphEdge(
                        controller,
                        controllerCreate,
                        EdgeType.HAS_METHOD
                )
        );

        graph.addEdge(
                new GraphEdge(
                        service,
                        serviceCreateUser,
                        EdgeType.HAS_METHOD
                )
        );

        graph.addEdge(
                new GraphEdge(
                        repository,
                        repositorySave,
                        EdgeType.HAS_METHOD
                )
        );

        // -------------------------------------------------
        // Method calls
        // -------------------------------------------------

        graph.addEdge(
                new GraphEdge(
                        controllerCreate,
                        serviceCreateUser,
                        EdgeType.CALLS
                )
        );

        graph.addEdge(
                new GraphEdge(
                        serviceCreateUser,
                        repositorySave,
                        EdgeType.CALLS
                )
        );

        // -------------------------------------------------
        // Create NLP engine
        // -------------------------------------------------

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

        assertEquals(2, result.getResultCount());

        assertEquals(
                "UserService",
                result.getNodes().get(0).getName()
        );

        assertEquals(
                "User",
                result.getNodes().get(1).getName()
        );
    }

    @Test
    void shouldAnswerDependentsQuestion() {

        QueryResult result =
                engine.ask(
                        "Who depends on User?"
                );

        assertEquals(
                QueryIntent.DEPENDENTS,
                result.getIntent()
        );

        assertEquals(
                "User",
                result.getEntity().getName()
        );

        assertEquals(3, result.getResultCount());

        assertEquals(
                "UserController",
                result.getNodes().get(0).getName()
        );

        assertEquals(
                "UserService",
                result.getNodes().get(1).getName()
        );

        assertEquals(
                "UserRepository",
                result.getNodes().get(2).getName()
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

        assertEquals(2, result.getResultCount());

        assertEquals(
                "UserService",
                result.getNodes().get(0).getName()
        );

        assertEquals(
                "User",
                result.getNodes().get(1).getName()
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

    @Test
    void shouldGenerateNaturalLanguageDependencyAnswer() {

        String answer =
                engine.askAndAnswer(
                        "What does UserController depend on?"
                );

        assertEquals(
                "UserController depends on UserService, User.",
                answer
        );
    }

    @Test
    void shouldGenerateNaturalLanguageDependentAnswer() {

        String answer =
                engine.askAndAnswer(
                        "Who depends on User?"
                );

        assertEquals(
                "User is depended on by UserController, UserService, UserRepository.",
                answer
        );
    }

    @Test
    void shouldGenerateNaturalLanguageCalleeAnswer() {

        String answer =
                engine.askAndAnswer(
                        "What methods does UserController call?"
                );

        assertEquals(
                "UserController calls UserService.create_user().",
                answer
        );
    }

    @Test
    void shouldGenerateNaturalLanguageCallerAnswer() {

        String answer =
                engine.askAndAnswer(
                        "Who calls UserRepository.save()?"
                );

        assertEquals(
                "UserRepository.save() is called by UserService.create_user().",
                answer
        );
    }
}