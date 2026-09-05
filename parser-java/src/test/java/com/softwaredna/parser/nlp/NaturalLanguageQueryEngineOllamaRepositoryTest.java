package com.softwaredna.parser.nlp;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import com.softwaredna.knowledge.KnowledgeGraph;
import com.softwaredna.knowledge.KnowledgeGraphBuilder;
import com.softwaredna.model.RepositoryModel;
import com.softwaredna.parser.RepositoryParser;

class NaturalLanguageQueryEngineOllamaRepositoryTest {

    @Test
    void shouldAnswerQuestionFromParsedRepositoryUsingOllama()
            throws Exception {

        Path repositoryPath =
                Files.createTempDirectory("software-dna-ollama-test");

        Path controllerFile =
                repositoryPath.resolve("UserController.java");

        Path serviceFile =
                repositoryPath.resolve("UserService.java");

        Files.writeString(
                serviceFile,
                """
                public class UserService {

                    public void createUser() {
                        System.out.println("Creating user");
                    }
                }
                """
        );

        Files.writeString(
                controllerFile,
                """
                public class UserController {

                    private UserService userService;

                    public UserController(UserService userService) {
                        this.userService = userService;
                    }

                    public void createUser() {
                        userService.createUser();
                    }
                }
                """
        );

        /*
         * Phase 1:
         * Parse the real Java repository.
         */
        RepositoryParser parser =
                new RepositoryParser();

        RepositoryModel repository =
                parser.parseRepository(
                        repositoryPath.toString()
                );

        assertNotNull(repository);
        assertEquals(2, repository.getFiles().size());

        /*
         * Phase 2:
         * Build the Knowledge Graph from
         * the parsed repository.
         */
        KnowledgeGraphBuilder graphBuilder =
                new KnowledgeGraphBuilder();

        KnowledgeGraph graph =
                graphBuilder.build(repository);

        assertNotNull(graph);
        assertFalse(graph.getNodes().isEmpty());
        assertFalse(graph.getEdges().isEmpty());

        /*
         * Phase 3:
         * Create the NLP engine with Ollama.
         */
        NaturalLanguageQueryEngine engine =
                new NaturalLanguageQueryEngine(
                        graph,
                        new OllamaClient("llama3:latest")
                );

        /*
         * Phase 4:
         * Ask a question about the
         * actual parsed repository.
         */
        String answer =
                engine.askAndAnswerWithLLM(
                        "What does UserController depend on?"
                );

        assertNotNull(answer);
        assertFalse(answer.isBlank());

        System.out.println();
        System.out.println("==========================================");
        System.out.println("REAL REPOSITORY OLLAMA ANSWER");
        System.out.println("==========================================");
        System.out.println(answer);
        System.out.println("==========================================");
    }
}