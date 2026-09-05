package com.softwaredna.parser.nlp;

/**
 * Test implementation of LLMClient.
 *
 * This implementation does not contact an external LLM.
 * It is used to verify the LLM integration pipeline
 * without requiring network access or API credentials.
 */
public class MockLLMClient implements LLMClient {

    @Override
    public String generate(String prompt) {

        if (prompt == null || prompt.isBlank()) {
            throw new IllegalArgumentException(
                    "Prompt cannot be null or blank."
            );
        }

        return "Mock LLM response generated from grounded graph context.";
    }
}