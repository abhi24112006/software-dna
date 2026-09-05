package com.softwaredna.parser.nlp;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * LLMClient implementation that communicates with a locally running Ollama server.
 *
 * Default Ollama endpoint:
 * http://localhost:11434/api/generate
 *
 * Default model:
 * llama3:latest
 */
public class OllamaClient implements LLMClient {

    private static final String DEFAULT_BASE_URL = "http://localhost:11434";
    private static final String DEFAULT_MODEL = "llama3:latest";

    private final HttpClient httpClient;
    private final String baseUrl;
    private final String model;

    /**
     * Creates an Ollama client using the default local Ollama server
     * and llama3:latest model.
     */
    public OllamaClient() {
        this(DEFAULT_BASE_URL, DEFAULT_MODEL);
    }

    /**
     * Creates an Ollama client with a custom model.
     *
     * @param model Ollama model name
     */
    public OllamaClient(String model) {
        this(DEFAULT_BASE_URL, model);
    }

    /**
     * Creates an Ollama client with a custom base URL and model.
     *
     * @param baseUrl Ollama server base URL
     * @param model Ollama model name
     */
    public OllamaClient(String baseUrl, String model) {
        if (baseUrl == null || baseUrl.isBlank()) {
            throw new IllegalArgumentException("baseUrl must not be blank");
        }

        if (model == null || model.isBlank()) {
            throw new IllegalArgumentException("model must not be blank");
        }

        this.baseUrl = removeTrailingSlash(baseUrl);
        this.model = model;

        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    /**
     * Sends a prompt to Ollama and returns the generated response.
     *
     * @param prompt prompt to send to the LLM
     * @return generated response
     */
    @Override
    public String generate(String prompt) {

        if (prompt == null || prompt.isBlank()) {
            throw new IllegalArgumentException("prompt must not be blank");
        }

        String escapedPrompt = escapeJson(prompt);

        String requestBody = """
                {
                  "model": "%s",
                  "prompt": "%s",
                  "stream": false
                }
                """.formatted(
                escapeJson(model),
                escapedPrompt
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/generate"))
                .timeout(Duration.ofMinutes(5))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        try {
            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new RuntimeException(
                        "Ollama request failed with HTTP "
                                + response.statusCode()
                                + ": "
                                + response.body()
                );
            }

            return extractResponse(response.body());

        } catch (IOException e) {
            throw new RuntimeException("Failed to communicate with Ollama", e);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Ollama request was interrupted", e);
        }
    }

    /**
     * Extracts the "response" field from Ollama's JSON response.
     */
    private String extractResponse(String json) {

        String key = "\"response\"";

        int keyIndex = json.indexOf(key);

        if (keyIndex == -1) {
            throw new RuntimeException(
                    "Ollama response did not contain a 'response' field: " + json
            );
        }

        int colonIndex = json.indexOf(':', keyIndex + key.length());

        if (colonIndex == -1) {
            throw new RuntimeException(
                    "Invalid Ollama response: " + json
            );
        }

        int startQuote = json.indexOf('"', colonIndex + 1);

        if (startQuote == -1) {
            throw new RuntimeException(
                    "Invalid Ollama response: " + json
            );
        }

        StringBuilder result = new StringBuilder();
        boolean escaped = false;

        for (int i = startQuote + 1; i < json.length(); i++) {

            char current = json.charAt(i);

            if (escaped) {
                switch (current) {
                    case 'n' -> result.append('\n');
                    case 'r' -> result.append('\r');
                    case 't' -> result.append('\t');
                    case '"' -> result.append('"');
                    case '\\' -> result.append('\\');
                    case '/' -> result.append('/');
                    default -> result.append(current);
                }

                escaped = false;
                continue;
            }

            if (current == '\\') {
                escaped = true;
                continue;
            }

            if (current == '"') {
                return result.toString();
            }

            result.append(current);
        }

        throw new RuntimeException(
                "Invalid Ollama response: unterminated response string"
        );
    }

    /**
     * Escapes a string for safe inclusion in JSON.
     */
    private String escapeJson(String value) {

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private String removeTrailingSlash(String value) {

        if (value.endsWith("/")) {
            return value.substring(0, value.length() - 1);
        }

        return value;
    }
}