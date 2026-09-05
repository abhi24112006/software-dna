package com.softwaredna.parser.nlp;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import com.softwaredna.knowledge.EdgeType;
import com.softwaredna.knowledge.GraphEdge;
import com.softwaredna.knowledge.GraphNode;
import com.softwaredna.knowledge.KnowledgeGraph;
import com.softwaredna.knowledge.NodeType;

class NaturalLanguageQueryEngineOllamaTest {

    @Test
    void shouldAnswerDependencyQuestionUsingOllama() {

        KnowledgeGraph graph = new KnowledgeGraph();

        GraphNode controller =
                new GraphNode(
                        "class:UserController",
                        "UserController",
                        NodeType.CLASS
                );

        GraphNode service =
                new GraphNode(
                        "class:UserService",
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

        NaturalLanguageQueryEngine engine =
                new NaturalLanguageQueryEngine(
                        graph,
                        new OllamaClient("llama3:latest")
                );

        String answer =
                engine.askAndAnswerWithLLM(
                        "What does UserController depend on?"
                );

        assertNotNull(answer);
        assertFalse(answer.isBlank());

        System.out.println();
        System.out.println("==========================================");
        System.out.println("SOFTWARE DNA OLLAMA ANSWER");
        System.out.println("==========================================");
        System.out.println(answer);
        System.out.println("==========================================");
    }
}