package com.softwaredna.parser.nlp;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.softwaredna.knowledge.GraphNode;
import com.softwaredna.knowledge.NodeType;
import com.softwaredna.model.ClassMetrics;

class AnswerGeneratorTest {

    private AnswerGenerator answerGenerator;

    private GraphNode controller;
    private GraphNode service;
    private GraphNode repository;

    @BeforeEach
    void setUp() {

        answerGenerator = new AnswerGenerator();

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
    }

    @Test
    void shouldGenerateDependencyAnswer() {

        QueryResult result = new QueryResult(
                "What does UserController depend on?",
                QueryIntent.DEPENDENCIES,
                controller,
                List.of(service, repository)
        );

        String answer = answerGenerator.generate(result);

        assertEquals(
                "UserController depends on UserService, UserRepository.",
                answer
        );
    }

    @Test
    void shouldGenerateDependentAnswer() {

        QueryResult result = new QueryResult(
                "Who depends on UserService?",
                QueryIntent.DEPENDENTS,
                service,
                List.of(controller)
        );

        String answer = answerGenerator.generate(result);

        assertEquals(
                "UserService is depended on by UserController.",
                answer
        );
    }

    @Test
    void shouldGenerateCalleeAnswer() {

        GraphNode method = new GraphNode(
                "method:UserService.create",
                "UserService.create()",
                NodeType.METHOD
        );

        QueryResult result = new QueryResult(
                "What methods does UserController call?",
                QueryIntent.CALLEES,
                controller,
                List.of(method)
        );

        String answer = answerGenerator.generate(result);

        assertEquals(
                "UserController calls UserService.create().",
                answer
        );
    }

    @Test
    void shouldGenerateCallerAnswer() {

        GraphNode method = new GraphNode(
                "method:UserService.create",
                "UserService.create()",
                NodeType.METHOD
        );

        QueryResult result = new QueryResult(
                "Who calls UserService.create()?",
                QueryIntent.CALLERS,
                method,
                List.of(controller)
        );

        String answer = answerGenerator.generate(result);

        assertEquals(
                "UserService.create() is called by UserController.",
                answer
        );
    }

    @Test
    void shouldHandleEmptyResults() {

        QueryResult result = new QueryResult(
                "What does UserRepository depend on?",
                QueryIntent.DEPENDENCIES,
                repository,
                List.of()
        );

        String answer = answerGenerator.generate(result);

        assertEquals(
                "UserRepository depends on no known entities.",
                answer
        );
    }

    @Test
    void shouldGenerateMultipleResults() {

        GraphNode user = new GraphNode(
                "class:User",
                "User",
                NodeType.CLASS
        );

        QueryResult result = new QueryResult(
                "Who depends on User?",
                QueryIntent.DEPENDENTS,
                user,
                List.of(controller, service)
        );

        String answer = answerGenerator.generate(result);

        assertEquals(
                "User is depended on by UserController, UserService.",
                answer
        );
    }

    @Test
    void shouldGenerateMetricsAnswer() {

        ClassMetrics metrics = new ClassMetrics();

        metrics.setMethodCount(5);
        metrics.setFieldCount(3);
        metrics.setTotalLinesOfCode(120);
        metrics.setTotalCyclomaticComplexity(8);
        metrics.setFanIn(4);
        metrics.setFanOut(2);
        metrics.setCbo(6);
        metrics.setDit(2);
        metrics.setNoc(3);
        metrics.setRfc(10);

        QueryResult result = new QueryResult(
                "What are the metrics of UserController?",
                QueryIntent.METRICS,
                controller,
                List.of(),
                metrics
        );

        String answer = answerGenerator.generate(result);

        assertEquals(
                "UserController metrics: " +
                        "Methods: 5, " +
                        "Fields: 3, " +
                        "LOC: 120, " +
                        "Cyclomatic Complexity: 8, " +
                        "Fan-In: 4, " +
                        "Fan-Out: 2, " +
                        "CBO: 6, " +
                        "DIT: 2, " +
                        "NOC: 3, " +
                        "RFC: 10.",
                answer
        );
    }

    @Test
    void shouldHandleMissingMetrics() {

        QueryResult result = new QueryResult(
                "What are the metrics of UserController?",
                QueryIntent.METRICS,
                controller,
                List.of()
        );

        String answer = answerGenerator.generate(result);

        assertEquals(
                "No metrics are available for UserController.",
                answer
        );
    }
}