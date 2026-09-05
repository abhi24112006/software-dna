package com.softwaredna.parser.nlp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.softwaredna.knowledge.EdgeType;
import com.softwaredna.knowledge.GraphEdge;
import com.softwaredna.knowledge.GraphNode;
import com.softwaredna.knowledge.KnowledgeGraph;
import com.softwaredna.knowledge.NodeType;

class NaturalLanguageQueryEngineLLMTest {

    @Test
    void shouldGenerateAnswerUsingConfiguredLLM() {

        KnowledgeGraph graph =
                createGraph();

        LLMClient mockClient = prompt -> {

            assertTrue(
                    prompt.contains(
                            "UserController"
                    )
            );

            assertTrue(
                    prompt.contains(
                            "UserService"
                    )
            );

            return "The UserController depends on UserService.";
        };

        NaturalLanguageQueryEngine engine =
                new NaturalLanguageQueryEngine(
                        graph,
                        mockClient
                );

        String answer =
                engine.askAndAnswerWithLLM(
                        "What does UserController depend on?"
                );

        assertEquals(
                "The UserController depends on UserService.",
                answer
        );
    }

    @Test
    void shouldKeepDeterministicAnswerGenerationAvailable() {

        KnowledgeGraph graph =
                createGraph();

        LLMClient mockClient = prompt ->
                "LLM answer.";

        NaturalLanguageQueryEngine engine =
                new NaturalLanguageQueryEngine(
                        graph,
                        mockClient
                );

        String answer =
                engine.askAndAnswer(
                        "What does UserController depend on?"
                );

        assertEquals(
                "UserController depends on UserService.",
                answer
        );
    }

    @Test
    void shouldFallbackWhenLLMIsUnavailable() {

        KnowledgeGraph graph =
                createGraph();

        LLMClient failingClient = prompt -> {
            throw new RuntimeException(
                    "LLM unavailable"
            );
        };

        NaturalLanguageQueryEngine engine =
                new NaturalLanguageQueryEngine(
                        graph,
                        failingClient
                );

        String answer =
                engine.askAndAnswerWithLLM(
                        "What does UserController depend on?"
                );

        assertEquals(
                "UserController depends on UserService.",
                answer
        );
    }

    private KnowledgeGraph createGraph() {

        KnowledgeGraph graph =
                new KnowledgeGraph();

        GraphNode controller =
                new GraphNode(
                        "UserController",
                        "UserController",
                        NodeType.CLASS
                );

        GraphNode service =
                new GraphNode(
                        "UserService",
                        "UserService",
                        NodeType.CLASS
                );

        graph.addNode(controller);
        graph.addNode(service);

        graph.addEdge(
                new GraphEdge(
                        controller,
                        service,
                        EdgeType.DEPENDS_ON
                )
        );

        return graph;
    }
}