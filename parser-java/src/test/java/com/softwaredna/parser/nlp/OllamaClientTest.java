package com.softwaredna.parser.nlp;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

class OllamaClientTest {

    private HttpServer server;
    private String baseUrl;

    @BeforeEach
    void setUp() throws IOException {
        server = HttpServer.create(
                new InetSocketAddress("localhost", 0),
                0
        );

        server.createContext("/api/generate", this::handleGenerate);

        server.start();

        baseUrl = "http://localhost:" + server.getAddress().getPort();
    }

    @AfterEach
    void tearDown() {
        server.stop(0);
    }

    @Test
    void shouldGenerateResponseFromOllama() {

        OllamaClient client =
                new OllamaClient(baseUrl, "llama3:latest");

        String response = client.generate(
                "What is a software dependency graph?"
        );

        assertEquals(
                "This is a mock Ollama response.",
                response
        );
    }

    @Test
    void shouldRejectBlankPrompt() {

        OllamaClient client =
                new OllamaClient(baseUrl, "llama3:latest");

        assertThrows(
                IllegalArgumentException.class,
                () -> client.generate("")
        );
    }

    @Test
    void shouldRejectBlankModel() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new OllamaClient(baseUrl, "")
        );
    }

    private void handleGenerate(HttpExchange exchange) throws IOException {

        String response = """
                {
                  "model": "llama3:latest",
                  "response": "This is a mock Ollama response.",
                  "done": true
                }
                """;

        byte[] responseBytes =
                response.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders()
                .set("Content-Type", "application/json");

        exchange.sendResponseHeaders(
                200,
                responseBytes.length
        );

        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(responseBytes);
        }
    }
}