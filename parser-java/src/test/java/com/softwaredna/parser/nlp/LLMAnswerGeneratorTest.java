package com.softwaredna.parser.nlp;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.softwaredna.knowledge.GraphNode;
import com.softwaredna.knowledge.NodeType;

class LLMAnswerGeneratorTest {

    @Test
    void shouldUseLLMResponse() {

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

        QueryResult result =
                new QueryResult(
                        "What does UserController depend on?",
                        QueryIntent.DEPENDENCIES,
                        controller,
                        List.of(service)
                );

        LLMClient client = prompt -> {
            assertTrue(
                    prompt.contains("UserController")
            );

            assertTrue(
                    prompt.contains("UserService")
            );

            assertTrue(
                    prompt.contains("GRAPH-DERIVED FACTS:")
            );

            return "UserController depends on UserService.";
        };

        LLMAnswerGenerator generator =
                new LLMAnswerGenerator(client);

        String answer =
                generator.generate(result);

        assertEquals(
                "UserController depends on UserService.",
                answer
        );
    }

    @Test
    void shouldFallbackToDeterministicAnswerWhenLLMFails() {

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

        QueryResult result =
                new QueryResult(
                        "What does UserController depend on?",
                        QueryIntent.DEPENDENCIES,
                        controller,
                        List.of(service)
                );

        LLMClient failingClient = prompt -> {
            throw new RuntimeException(
                    "LLM unavailable"
            );
        };

        LLMAnswerGenerator generator =
                new LLMAnswerGenerator(failingClient);

        String answer =
                generator.generate(result);

        assertEquals(
                "UserController depends on UserService.",
                answer
        );
    }

    @Test
    void shouldFallbackWhenLLMReturnsEmptyResponse() {

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

        QueryResult result =
                new QueryResult(
                        "What does UserController depend on?",
                        QueryIntent.DEPENDENCIES,
                        controller,
                        List.of(service)
                );

        LLMClient emptyClient = prompt -> "";

        LLMAnswerGenerator generator =
                new LLMAnswerGenerator(emptyClient);

        String answer =
                generator.generate(result);

        assertEquals(
                "UserController depends on UserService.",
                answer
        );
    }
}