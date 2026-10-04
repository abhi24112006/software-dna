package com.softwaredna.parser.nlp;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.softwaredna.knowledge.EdgeType;
import com.softwaredna.knowledge.GraphEdge;
import com.softwaredna.knowledge.GraphNode;
import com.softwaredna.knowledge.KnowledgeGraph;
import com.softwaredna.knowledge.NodeType;
import com.softwaredna.model.SourceEvidence;

class GroundedPromptBuilderTest {

    @Test
    void shouldBuildPromptUsingGraphDerivedFacts() {

        GraphNode userController =
                new GraphNode(
                        "UserController",
                        "UserController",
                        NodeType.CLASS);

        GraphNode userService =
                new GraphNode(
                        "UserService",
                        "UserService",
                        NodeType.CLASS);

        QueryResult result =
                new QueryResult(
                        "What does UserController depend on?",
                        QueryIntent.DEPENDENCIES,
                        userController,
                        List.of(userService));

        GroundedContext context =
                new GroundedContext(result);

        GroundedPromptBuilder builder =
                new GroundedPromptBuilder();

        String prompt =
                builder.build(context);

        assertTrue(
                prompt.contains(
                        "What does UserController depend on?"));

        assertTrue(
                prompt.contains("DEPENDENCIES"));

        assertTrue(
                prompt.contains("UserController [CLASS]"));

        assertTrue(
                prompt.contains("UserService [CLASS]"));

        assertTrue(
                prompt.contains("GRAPH-DERIVED FACTS:"));

        assertTrue(
                prompt.contains(
                        "Use only the graph-derived facts"));
    }

    @Test
    void shouldRepresentEmptyGraphResults() {

        GraphNode user =
                new GraphNode(
                        "User",
                        "User",
                        NodeType.CLASS);

        QueryResult result =
                new QueryResult(
                        "Who depends on User?",
                        QueryIntent.DEPENDENTS,
                        user,
                        List.of());

        GroundedContext context =
                new GroundedContext(result);

        GroundedPromptBuilder builder =
                new GroundedPromptBuilder();

        String prompt =
                builder.build(context);

        assertTrue(
                prompt.contains(
                        "No related graph entities were found."));
    }

    @Test
    void shouldRenderSourceEvidenceForGroundedRelationship() {

        GraphNode userController =
                new GraphNode(
                        "UserController",
                        "UserController",
                        NodeType.CLASS);

        GraphNode userService =
                new GraphNode(
                        "UserService",
                        "UserService",
                        NodeType.CLASS);

        SourceEvidence evidence =
                new SourceEvidence(
                        "src/main/java/UserController.java",
                        42);

        KnowledgeGraph graph =
                new KnowledgeGraph();

        graph.addNode(userController);
        graph.addNode(userService);

        graph.addEdge(
                new GraphEdge(
                        userController,
                        userService,
                        EdgeType.DEPENDS_ON,
                        evidence));

        QueryResult result =
                new QueryResult(
                        "What does UserController depend on?",
                        QueryIntent.DEPENDENCIES,
                        userController,
                        List.of(userService));

        GroundedContext context =
                new GroundedContext(
                        result,
                        graph);

        GroundedPromptBuilder builder =
                new GroundedPromptBuilder();

        String prompt =
                builder.build(context);

        assertTrue(
                prompt.contains(
                        "UserController -- DEPENDS_ON --> UserService"));

        assertTrue(
                prompt.contains(
                        "Evidence: src/main/java/UserController.java:42"));
    }
}