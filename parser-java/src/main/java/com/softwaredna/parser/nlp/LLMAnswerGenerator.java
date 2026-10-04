package com.softwaredna.parser.nlp;

import com.softwaredna.knowledge.KnowledgeGraph;

/**
 * Generates natural-language answers using an LLM.
 *
 * The LLM receives only graph-derived context through
 * GroundedContext and GroundedPromptBuilder.
 *
 * If the LLM fails, the deterministic AnswerGenerator
 * can be used as a fallback.
 */
public class LLMAnswerGenerator {

    private final GroundedPromptBuilder promptBuilder;
    private final LLMClient llmClient;
    private final AnswerGenerator fallbackGenerator;
    private final KnowledgeGraph knowledgeGraph;

    /**
     * Creates an LLM-backed answer generator without a Knowledge Graph.
     *
     * This constructor preserves the existing behavior.
     *
     * @param llmClient configured LLM client
     */
    public LLMAnswerGenerator(LLMClient llmClient) {

        this(llmClient, null);
    }

    /**
     * Creates an LLM-backed answer generator with a Knowledge Graph.
     *
     * @param llmClient configured LLM client
     * @param knowledgeGraph source Knowledge Graph for grounded relationships
     */
    public LLMAnswerGenerator(
            LLMClient llmClient,
            KnowledgeGraph knowledgeGraph) {

        if (llmClient == null) {
            throw new IllegalArgumentException(
                    "LLMClient cannot be null."
            );
        }

        this.promptBuilder = new GroundedPromptBuilder();
        this.llmClient = llmClient;
        this.fallbackGenerator = new AnswerGenerator();
        this.knowledgeGraph = knowledgeGraph;
    }

    /**
     * Generates an answer using the configured LLM.
     *
     * If the LLM throws an exception or returns an empty response,
     * the deterministic graph-based answer generator is used.
     *
     * @param result graph-derived query result
     * @return natural-language answer
     */
    public String generate(QueryResult result) {

        if (result == null) {
            throw new IllegalArgumentException(
                    "QueryResult cannot be null."
            );
        }

        GroundedContext context;

        if (knowledgeGraph == null) {
            context = new GroundedContext(result);
        } else {
            context = new GroundedContext(
                    result,
                    knowledgeGraph
            );
        }

        String prompt =
                promptBuilder.build(context);

        try {

            String response =
                    llmClient.generate(prompt);

            if (response != null
                    && !response.isBlank()) {

                return response.trim();
            }

        } catch (RuntimeException ignored) {
            // Fall back to deterministic generation.
        }

        return fallbackGenerator.generate(result);
    }
}