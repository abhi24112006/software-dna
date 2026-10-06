package com.softwaredna.parser.nlp;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.softwaredna.analysis.architecture.ArchitectureReport;
import com.softwaredna.graph.GraphRepository;
import com.softwaredna.knowledge.EdgeType;
import com.softwaredna.knowledge.GraphEdge;
import com.softwaredna.knowledge.GraphNode;
import com.softwaredna.knowledge.KnowledgeGraph;
import com.softwaredna.knowledge.NodeType;
import com.softwaredna.model.ClassMetrics;
import com.softwaredna.model.ParsedClass;
import com.softwaredna.model.ParsedFile;
import com.softwaredna.model.RepositoryModel;

class NaturalLanguageQueryEngineTest {

    private KnowledgeGraph graph;
    private RepositoryModel repositoryModel;
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
        // Repository model and class metrics
        // -------------------------------------------------

        repositoryModel = new RepositoryModel();

        ParsedFile file = new ParsedFile();
        ParsedClass parsedController =
                new ParsedClass("UserController");

        ClassMetrics controllerMetrics =
                new ClassMetrics();
        controllerMetrics.setMethodCount(5);
        controllerMetrics.setFieldCount(3);
        controllerMetrics.setTotalLinesOfCode(120);
        controllerMetrics.setTotalCyclomaticComplexity(8);

        parsedController.setMetrics(controllerMetrics);
        file.getClasses().add(parsedController);
        repositoryModel.getFiles().add(file);

        // -------------------------------------------------
        // Create NLP engine
        // -------------------------------------------------

        engine = new NaturalLanguageQueryEngine(
                graph,
                repositoryModel
        );
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
void shouldIncludeRetrievedSubgraphForDependenciesQuestion() {

    QueryResult result =
            engine.ask(
                    "What does UserController depend on?"
            );

    assertTrue(result.hasSubgraph());

    assertEquals(
            4,
            result.getSubgraph().getNodeCount()
    );

    assertEquals(
            3,
            result.getSubgraph().getEdgeCount()
    );

    assertTrue(
            result.getSubgraph()
                    .getNodes()
                    .contains(controller)
    );

    assertTrue(
            result.getSubgraph()
                    .getNodes()
                    .contains(service)
    );

    assertTrue(
            result.getSubgraph()
                    .getNodes()
                    .contains(user)
    );

    assertTrue(
            result.getSubgraph()
                    .getNodes()
                    .contains(controllerCreate)
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

    @Test
    void shouldAnswerReachabilityQuestion() {

        QueryResult result =
                engine.ask(
                        "What can UserController reach?"
                );

        assertEquals(
                QueryIntent.REACHABILITY,
                result.getIntent()
        );

        assertEquals(
                "UserController",
                result.getEntity().getName()
        );

        assertEquals(3, result.getResultCount());

        assertEquals(
                "UserService",
                result.getNodes().get(0).getName()
        );

        assertEquals(
                "User",
                result.getNodes().get(1).getName()
        );

        assertEquals(
                "UserRepository",
                result.getNodes().get(2).getName()
        );
    }

    @Test
    void shouldGenerateNaturalLanguageReachabilityAnswer() {

        String answer =
                engine.askAndAnswer(
                        "What can UserController reach?"
                );

        assertEquals(
                "UserController can reach UserService, User, UserRepository.",
                answer
        );
    }

    @Test
    void shouldAnswerMetricsQuestion() {

        QueryResult result =
                engine.ask(
                        "What are the metrics of UserController?"
                );

        assertEquals(
                QueryIntent.METRICS,
                result.getIntent()
        );

        assertEquals(
                "UserController",
                result.getEntity().getName()
        );

        assertTrue(result.hasMetrics());
        assertEquals(5, result.getMetrics().getMethodCount());
        assertEquals(3, result.getMetrics().getFieldCount());
        assertEquals(120, result.getMetrics().getTotalLinesOfCode());
        assertEquals(8, result.getMetrics().getTotalCyclomaticComplexity());
    }

    @Test
    void shouldAnswerMethodCountMetricsQuestion() {

        QueryResult result =
                engine.ask(
                        "How many methods does UserController have?"
                );

        assertEquals(
                QueryIntent.METRICS,
                result.getIntent()
        );

        assertEquals(
                "UserController",
                result.getEntity().getName()
        );

        assertEquals(
                5,
                result.getMetrics().getMethodCount()
        );
    }

    @Test
    void shouldAnswerMetricsQuestionCaseInsensitively() {

        QueryResult result =
                engine.ask(
                        "what are the metrics of usercontroller?"
                );

        assertEquals(
                "UserController",
                result.getEntity().getName()
        );

        assertTrue(result.hasMetrics());
    }

    @Test
    void shouldAnalyzeArchitectureQuestion() {

        GraphRepository architectureRepository =
                new TestGraphRepository();

        NaturalLanguageQueryEngine architectureEngine =
                new NaturalLanguageQueryEngine(
                        graph,
                        architectureRepository
                );

        ArchitectureReport report =
                architectureEngine.askArchitecture(
                        "What is the architecture?"
                );

        assertNotNull(report);

        assertEquals(
                "LAYERED",
                report.getArchitectureStyle()
        );

        assertEquals(
                4,
                report.getLayers().size()
        );

        assertEquals(
                0,
                report.getViolations().size()
        );
    }

    @Test
    void shouldRejectArchitectureAnalysisWithoutRepository() {

        assertThrows(
                IllegalStateException.class,
                () -> engine.askArchitecture(
                        "What is the architecture?"
                )
        );
    }

    /**
     * Small in-memory GraphRepository used only for testing
     * ArchitectureAnalyzer integration.
     *
     * It represents a simple layered architecture:
     *
     * UserController
     *        ↓
     * UserService
     *        ↓
     * UserRepository
     *        ↓
     * User
     */
    private static class TestGraphRepository
            implements GraphRepository {

        @Override
        public void save(KnowledgeGraph graph) {
            // Not required for this test.
        }

        @Override
        public List<String> getAllNodes() {
            return Collections.emptyList();
        }

        @Override
public List<String> getDependencies(String nodeId) {

    switch (nodeId) {

        case "controller.UserController":
            return List.of(
                    "service.UserService"
            );

        case "service.UserService":
            return List.of(
                    "repository.UserRepository"
            );

        case "repository.UserRepository":
            return List.of(
                    "model.User"
            );

        case "model.User":
            return Collections.emptyList();

        default:
            return Collections.emptyList();
    }
}

        @Override
        public List<String> getDependents(String nodeId) {
            return Collections.emptyList();
        }

        @Override
        public List<String> getCallees(String methodId) {
            return Collections.emptyList();
        }

        @Override
        public List<String> getCallers(String methodId) {
            return Collections.emptyList();
        }

        @Override
        public List<String> getSubclasses(String classId) {
            return Collections.emptyList();
        }

        @Override
        public List<String> getSuperclass(String classId) {
            return Collections.emptyList();
        }

        @Override
        public List<String> getImplementedInterfaces(String classId) {
            return Collections.emptyList();
        }

        @Override
        public List<String> getImplementations(String interfaceId) {
            return Collections.emptyList();
        }

        @Override
        public List<String> getImpact(String nodeId) {
            return Collections.emptyList();
        }

        @Override
        public List<String> getMethodImpact(String methodId) {
            return Collections.emptyList();
        }

        @Override
        public List<String> getContainmentAwareImpact(String nodeId) {
            return Collections.emptyList();
        }

        @Override
        public List<String> getReachableNodes(
                String nodeId,
                int depth) {

            return Collections.emptyList();
        }

        @Override
        public List<String> getArchitecturePaths(
                String nodeId,
                int depth) {

            return Collections.emptyList();
        }

        @Override
        public List<String> getArchitecturePaths(
                String nodeId,
                int depth,
                Set<EdgeType> relationshipTypes) {

            return Collections.emptyList();
        }

        @Override
        public List<String> getImpactPaths(
                String nodeId,
                int depth,
                Set<EdgeType> relationshipTypes) {

            return Collections.emptyList();
        }

        @Override
public List<String> getClassNodes() {

    return List.of(
            "controller.UserController",
            "service.UserService",
            "repository.UserRepository",
            "model.User"
    );
}
    }
}