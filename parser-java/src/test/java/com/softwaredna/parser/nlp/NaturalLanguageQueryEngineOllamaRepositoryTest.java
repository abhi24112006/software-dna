package com.softwaredna.parser.nlp;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.softwaredna.knowledge.KnowledgeGraph;
import com.softwaredna.knowledge.KnowledgeGraphBuilder;
import com.softwaredna.model.RepositoryModel;
import com.softwaredna.parser.RepositoryParser;

class NaturalLanguageQueryEngineOllamaRepositoryTest {

    @Test
    void shouldAnswerMultipleQuestionsFromParsedRepositoryUsingOllama()
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
         * ---------------------------------------------------------
         * QUESTION 1:
         * What does UserController depend on?
         * ---------------------------------------------------------
         */

        QueryResult dependencyResult =
                engine.ask(
                        "What does UserController depend on?"
                );

        assertNotNull(dependencyResult);
        assertTrue(
                dependencyResult.getNodes()
                        .stream()
                        .anyMatch(node ->
                                node.getName()
                                        .equals("UserService")
                        )
        );

        String dependencyAnswer =
                engine.askAndAnswerWithLLM(
                        "What does UserController depend on?"
                );

        assertNotNull(dependencyAnswer);
        assertFalse(dependencyAnswer.isBlank());

        /*
         * ---------------------------------------------------------
         * QUESTION 2:
         * What methods does UserController call?
         * ---------------------------------------------------------
         */

        QueryResult calleeResult =
                engine.ask(
                        "What methods does UserController call?"
                );

        assertNotNull(calleeResult);

        assertTrue(
                calleeResult.getNodes()
                        .stream()
                        .anyMatch(node ->
                                node.getName()
                                        .contains("UserService.createUser()")
                        )
        );

        String calleeAnswer =
                engine.askAndAnswerWithLLM(
                        "What methods does UserController call?"
                );

        assertNotNull(calleeAnswer);
        assertFalse(calleeAnswer.isBlank());

        /*
         * ---------------------------------------------------------
         * QUESTION 3:
         * Who calls UserService.createUser()?
         * ---------------------------------------------------------
         */

        QueryResult callerResult =
                engine.ask(
                        "Who calls UserService.createUser()?"
                );

        assertNotNull(callerResult);

        assertTrue(
                callerResult.getNodes()
                        .stream()
                        .anyMatch(node ->
                                node.getName()
                                        .contains("UserController.createUser()")
                        )
        );

        String callerAnswer =
                engine.askAndAnswerWithLLM(
                        "Who calls UserService.createUser()?"
                );

        assertNotNull(callerAnswer);
        assertFalse(callerAnswer.isBlank());

        /*
         * ---------------------------------------------------------
         * QUESTION 4:
         * What would be impacted if UserService changes?
         * ---------------------------------------------------------
         */

        QueryResult impactResult =
                engine.ask(
                        "What would be impacted if UserService changes?"
                );

        assertNotNull(impactResult);

        assertEquals(
                QueryIntent.IMPACT,
                impactResult.getIntent()
        );

        assertTrue(
                impactResult.getNodes()
                        .stream()
                        .anyMatch(node ->
                                node.getName()
                                        .equals("UserController")
                        )
        );

        assertTrue(
                impactResult.getNodes()
                        .stream()
                        .anyMatch(node ->
                                node.getName()
                                        .contains("UserService.createUser()")
                        )
        );

        assertTrue(
                impactResult.getNodes()
                        .stream()
                        .anyMatch(node ->
                                node.getName()
                                        .contains("UserController.createUser()")
                        )
        );

        String impactAnswer =
                engine.askAndAnswerWithLLM(
                        "What would be impacted if UserService changes?"
                );

        assertNotNull(impactAnswer);
        assertFalse(impactAnswer.isBlank());

        /*
         * ---------------------------------------------------------
         * Print all real Ollama answers.
         * ---------------------------------------------------------
         */

        System.out.println();
        System.out.println("==========================================");
        System.out.println("REAL REPOSITORY OLLAMA ANSWERS");
        System.out.println("==========================================");

        System.out.println();
        System.out.println("Q1: What does UserController depend on?");
        System.out.println("A1: " + dependencyAnswer);

        System.out.println();
        System.out.println("Q2: What methods does UserController call?");
        System.out.println("A2: " + calleeAnswer);

        System.out.println();
        System.out.println("Q3: Who calls UserService.createUser()?");
        System.out.println("A3: " + callerAnswer);

        System.out.println();
        System.out.println(
                "Q4: What would be impacted if UserService changes?"
        );
        System.out.println("A4: " + impactAnswer);

        System.out.println();
        System.out.println("==========================================");
    }
}