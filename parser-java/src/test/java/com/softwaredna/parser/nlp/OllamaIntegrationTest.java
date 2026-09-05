package com.softwaredna.parser.nlp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OllamaIntegrationTest {

    @Test
    void shouldGenerateResponseFromLocalOllama() {

        OllamaClient client = new OllamaClient("llama3:latest");

        String response = client.generate(
                "Explain a software dependency graph in one short sentence."
        );

        assertNotNull(response);
        assertFalse(response.isBlank());

        System.out.println("Ollama response:");
        System.out.println(response);
    }
}