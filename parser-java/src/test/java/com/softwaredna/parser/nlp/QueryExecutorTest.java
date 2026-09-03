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
    void shouldRejectNullPlan() {

        assertThrows(
                IllegalArgumentException.class,
                () -> executor.execute(null)
        );
    }

    @Test
    void shouldRejectUnsupportedOperation() {

        QueryPlan plan = new QueryPlan(
                QueryIntent.IMPACT,
                controller,
                QueryOperation.GET_IMPACT
        );

        assertThrows(
                UnsupportedOperationException.class,
                () -> executor.execute(plan)
        );
    }
}