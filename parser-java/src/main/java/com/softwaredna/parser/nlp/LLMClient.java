package com.softwaredna.parser.nlp;

/**
 * Abstraction for an external Large Language Model.
 *
 * Implementations of this interface are responsible only for
 * sending a prompt to an LLM and returning its response.
 *
 * The LLM does not determine graph facts. Graph facts are supplied
 * by the caller as part of the prompt.
 */
public interface LLMClient {

    /**
     * Sends a prompt to the configured LLM.
     *
     * @param prompt grounded prompt containing graph-derived facts
     * @return LLM response
     */
    String generate(String prompt);
}