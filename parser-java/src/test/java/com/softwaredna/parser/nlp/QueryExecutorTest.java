package com.softwaredna.parser.nlp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.softwaredna.analysis.metrics.MetricQueryService;
import com.softwaredna.knowledge.EdgeType;
import com.softwaredna.knowledge.GraphEdge;
import com.softwaredna.knowledge.GraphNode;
import com.softwaredna.knowledge.KnowledgeGraph;
import com.softwaredna.knowledge.NodeType;
import com.softwaredna.knowledge.query.KnowledgeGraphQuery;
import com.softwaredna.model.ClassMetrics;
import com.softwaredna.model.ParsedClass;
import com.softwaredna.model.ParsedFile;
import com.softwaredna.model.RepositoryModel;

class QueryExecutorTest {

    private KnowledgeGraph graph;
    private KnowledgeGraphQuery graphQuery;
    private QueryExecutor executor;

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

        graphQuery = new KnowledgeGraphQuery(graph);
        executor = new QueryExecutor(graphQuery);
    }

    @Test
    void shouldExecuteDependenciesQuery() {

        QueryPlan plan = new QueryPlan(
                QueryIntent.DEPENDENCIES,
                controller,
                QueryOperation.GET_DEPENDENCIES
        );

        QueryResult result = executor.execute(plan);

        assertEquals(
                QueryIntent.DEPENDENCIES,
                result.getIntent()
        );

        assertEquals(
                controller,
                result.getEntity()
        );

        assertEquals(1, result.getResultCount());

        assertEquals(
                "UserService",
                result.getNodes().get(0).getName()
        );
    }

    @Test
    void shouldExecuteDependentsQuery() {

        QueryPlan plan = new QueryPlan(
                QueryIntent.DEPENDENTS,
                service,
                QueryOperation.GET_DEPENDENTS
        );

        QueryResult result = executor.execute(plan);

        assertEquals(
                QueryIntent.DEPENDENTS,
                result.getIntent()
        );

        assertEquals(
                service,
                result.getEntity()
        );

        assertEquals(1, result.getResultCount());

        assertEquals(
                "UserController",
                result.getNodes().get(0).getName()
        );
    }

    @Test
    void shouldReturnEmptyWhenNoDependenciesExist() {

        QueryPlan plan = new QueryPlan(
                QueryIntent.DEPENDENCIES,
                repository,
                QueryOperation.GET_DEPENDENCIES
        );

        QueryResult result = executor.execute(plan);

        assertFalse(result.hasResults());
        assertEquals(0, result.getResultCount());
        assertTrue(result.getNodes().isEmpty());
    }

    @Test
    void shouldExecuteImpactQuery() {

        GraphNode serviceMethod = new GraphNode(
                "method:UserService.createUser",
                "UserService.createUser()",
                NodeType.METHOD
        );

        GraphNode controllerMethod = new GraphNode(
                "method:UserController.createUser",
                "UserController.createUser()",
                NodeType.METHOD
        );

        graph.addNode(serviceMethod);
        graph.addNode(controllerMethod);

        /*
         * UserService contains createUser().
         */
        graph.addEdge(
                new GraphEdge(
                        service,
                        serviceMethod,
                        EdgeType.HAS_METHOD
                )
        );

        /*
         * UserController.createUser() calls
         * UserService.createUser().
         */
        graph.addEdge(
                new GraphEdge(
                        controllerMethod,
                        serviceMethod,
                        EdgeType.CALLS
                )
        );

        QueryPlan plan = new QueryPlan(
                QueryIntent.IMPACT,
                service,
                QueryOperation.GET_IMPACT
        );

        QueryResult result = executor.execute(plan);

        assertEquals(
                QueryIntent.IMPACT,
                result.getIntent()
        );

        assertEquals(
                service,
                result.getEntity()
        );

        assertTrue(result.hasResults());

        assertTrue(
                result.getNodes().contains(controller)
        );

        assertTrue(
                result.getNodes().contains(serviceMethod)
        );

        assertTrue(
                result.getNodes().contains(controllerMethod)
        );
    }

    @Test
    void shouldExecuteReachabilityQuery() {

        QueryPlan plan = new QueryPlan(
                QueryIntent.REACHABILITY,
                controller,
                QueryOperation.GET_REACHABILITY
        );

        QueryResult result = executor.execute(plan);

        assertEquals(
                QueryIntent.REACHABILITY,
                result.getIntent()
        );

        assertEquals(
                controller,
                result.getEntity()
        );

        assertTrue(result.hasResults());

        assertEquals(2, result.getResultCount());

        assertTrue(
                result.getNodes().contains(service)
        );

        assertTrue(
                result.getNodes().contains(repository)
        );
    }

    @Test
    void shouldReturnEmptyWhenNoReachableNodesExist() {

        QueryPlan plan = new QueryPlan(
                QueryIntent.REACHABILITY,
                repository,
                QueryOperation.GET_REACHABILITY
        );

        QueryResult result = executor.execute(plan);

        assertFalse(result.hasResults());
        assertEquals(0, result.getResultCount());
        assertTrue(result.getNodes().isEmpty());
    }

    @Test
    void shouldExecuteMetricsQuery() {

        RepositoryModel repositoryModel =
                new RepositoryModel();

        ParsedFile file =
                new ParsedFile();

        ParsedClass parsedClass =
                new ParsedClass("UserController");

        ClassMetrics metrics =
                new ClassMetrics();

        metrics.setMethodCount(5);
        metrics.setFieldCount(3);
        metrics.setTotalLinesOfCode(120);
        metrics.setTotalCyclomaticComplexity(8);
        metrics.setFanIn(4);
        metrics.setFanOut(2);

        parsedClass.setMetrics(metrics);
        file.getClasses().add(parsedClass);
        repositoryModel.getFiles().add(file);

        MetricQueryService metricQueryService =
                new MetricQueryService(repositoryModel);

        QueryExecutor metricExecutor =
                new QueryExecutor(
                        graphQuery,
                        metricQueryService
                );

        QueryPlan plan = new QueryPlan(
                QueryIntent.METRICS,
                controller,
                QueryOperation.GET_METRICS
        );

        QueryResult result =
                metricExecutor.execute(plan);

        assertEquals(
                QueryIntent.METRICS,
                result.getIntent()
        );

        assertEquals(
                controller,
                result.getEntity()
        );

        assertTrue(result.hasMetrics());

        assertEquals(
                5,
                result.getMetrics().getMethodCount()
        );

        assertEquals(
                3,
                result.getMetrics().getFieldCount()
        );

        assertEquals(
                120,
                result.getMetrics().getTotalLinesOfCode()
        );

        assertEquals(
                8,
                result.getMetrics().getTotalCyclomaticComplexity()
        );

        assertEquals(
                4,
                result.getMetrics().getFanIn()
        );

        assertEquals(
                2,
                result.getMetrics().getFanOut()
        );

        assertTrue(result.getNodes().isEmpty());
    }

    @Test
    void shouldRejectMetricsQueryWithoutMetricService() {

        QueryPlan plan = new QueryPlan(
                QueryIntent.METRICS,
                controller,
                QueryOperation.GET_METRICS
        );

        IllegalStateException exception =
                assertThrows(
                        IllegalStateException.class,
                        () -> executor.execute(plan)
                );

        assertEquals(
                "MetricQueryService is required for GET_METRICS queries.",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectMetricsForNonClassEntity() {

        GraphNode method = new GraphNode(
                "method:UserController.createUser",
                "UserController.createUser()",
                NodeType.METHOD
        );

        RepositoryModel repositoryModel =
                new RepositoryModel();

        MetricQueryService metricQueryService =
                new MetricQueryService(repositoryModel);

        QueryExecutor metricExecutor =
                new QueryExecutor(
                        graphQuery,
                        metricQueryService
                );

        QueryPlan plan = new QueryPlan(
                QueryIntent.METRICS,
                method,
                QueryOperation.GET_METRICS
        );

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> metricExecutor.execute(plan)
                );

        assertEquals(
                "Metrics are currently supported only for class entities.",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectNullPlan() {

        assertThrows(
                IllegalArgumentException.class,
                () -> executor.execute(null)
        );
    }
}