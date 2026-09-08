package com.softwaredna.parser.nlp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.softwaredna.knowledge.EdgeType;
import com.softwaredna.knowledge.GraphEdge;
import com.softwaredna.knowledge.GraphNode;
import com.softwaredna.knowledge.KnowledgeGraph;
import com.softwaredna.knowledge.NodeType;
import com.softwaredna.knowledge.query.KnowledgeGraphQuery;

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
    void shouldRejectNullPlan() {

        assertThrows(
                IllegalArgumentException.class,
                () -> executor.execute(null)
        );
    }
}