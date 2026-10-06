package com.softwaredna.parser.nlp;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.softwaredna.knowledge.GraphNode;
import com.softwaredna.knowledge.NodeType;
import com.softwaredna.knowledge.Subgraph;
import com.softwaredna.model.ClassMetrics;

class QueryResultTest {

    private final GraphNode controller =
            new GraphNode(
                    "class:UserController",
                    "UserController",
                    NodeType.CLASS
            );

    private final GraphNode service =
            new GraphNode(
                    "class:UserService",
                    "UserService",
                    NodeType.CLASS
            );

    @Test
    void shouldStoreQueryInformation() {

        QueryResult result = new QueryResult(
                "What does UserController depend on?",
                QueryIntent.DEPENDENCIES,
                controller,
                List.of(service)
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
                controller,
                result.getEntity()
        );
    }

    @Test
    void shouldStoreResultNodes() {

        QueryResult result = new QueryResult(
                "What does UserController depend on?",
                QueryIntent.DEPENDENCIES,
                controller,
                List.of(service)
        );

        assertEquals(1, result.getNodes().size());
        assertEquals(service, result.getNodes().get(0));
    }

    @Test
    void shouldReportWhenResultsExist() {

        QueryResult result = new QueryResult(
                "What does UserController depend on?",
                QueryIntent.DEPENDENCIES,
                controller,
                List.of(service)
        );

        assertTrue(result.hasResults());
        assertEquals(1, result.getResultCount());
    }

    @Test
    void shouldReportWhenNoResultsExist() {

        QueryResult result = new QueryResult(
                "What does UserController depend on?",
                QueryIntent.DEPENDENCIES,
                controller,
                List.of()
        );

        assertFalse(result.hasResults());
        assertEquals(0, result.getResultCount());
    }

    @Test
    void shouldProtectInternalResultList() {

        QueryResult result = new QueryResult(
                "What does UserController depend on?",
                QueryIntent.DEPENDENCIES,
                controller,
                List.of(service)
        );

        assertThrows(
                UnsupportedOperationException.class,
                () -> result.getNodes().clear()
        );

        assertEquals(1, result.getResultCount());
    }

    @Test
    void shouldRejectBlankQuestion() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new QueryResult(
                        " ",
                        QueryIntent.DEPENDENCIES,
                        controller,
                        List.of(service)
                )
        );
    }

    @Test
    void shouldRejectNullIntent() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new QueryResult(
                        "What does UserController depend on?",
                        null,
                        controller,
                        List.of(service)
                )
        );
    }

    @Test
    void shouldRejectNullEntity() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new QueryResult(
                        "What does UserController depend on?",
                        QueryIntent.DEPENDENCIES,
                        null,
                        List.of(service)
                )
        );
    }

    @Test
    void shouldRejectNullResultNodes() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new QueryResult(
                        "What does UserController depend on?",
                        QueryIntent.DEPENDENCIES,
                        controller,
                        null
                )
        );
    }

        @Test
    void shouldStoreClassMetrics() {

        ClassMetrics metrics = new ClassMetrics();
        metrics.setMethodCount(5);
        metrics.setFieldCount(3);
        metrics.setTotalLinesOfCode(120);

        QueryResult result = new QueryResult(
                "What are the metrics of UserController?",
                QueryIntent.METRICS,
                controller,
                List.of(),
                metrics
        );

        assertTrue(result.hasMetrics());
        assertEquals(metrics, result.getMetrics());
        assertEquals(5, result.getMetrics().getMethodCount());
        assertEquals(3, result.getMetrics().getFieldCount());
        assertEquals(120, result.getMetrics().getTotalLinesOfCode());
    }

    @Test
    void shouldHaveNoMetricsForStructuralQuery() {

        QueryResult result = new QueryResult(
                "What does UserController depend on?",
                QueryIntent.DEPENDENCIES,
                controller,
                List.of(service)
        );

        assertFalse(result.hasMetrics());
        assertEquals(null, result.getMetrics());
    }

    @Test
void shouldStoreSubgraph() {
    GraphNode user = new GraphNode(
            "class:User",
            "User",
            NodeType.CLASS
    );

    Subgraph subgraph = new Subgraph(
            Set.of(user),
            Set.of()
    );

    QueryResult result = new QueryResult(
            "What does User depend on?",
            QueryIntent.DEPENDENCIES,
            user,
            List.of(),
            null,
            subgraph
    );

    assertTrue(result.hasSubgraph());
    assertEquals(subgraph, result.getSubgraph());
}

@Test
void shouldHaveNoSubgraphForExistingConstructor() {
    GraphNode user = new GraphNode(
            "class:User",
            "User",
            NodeType.CLASS
    );

    QueryResult result = new QueryResult(
            "What does User depend on?",
            QueryIntent.DEPENDENCIES,
            user,
            List.of()
    );

    assertFalse(result.hasSubgraph());
    assertEquals(null, result.getSubgraph());
}
}