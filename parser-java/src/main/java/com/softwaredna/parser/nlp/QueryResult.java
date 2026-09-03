package com.softwaredna.parser.nlp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.softwaredna.knowledge.GraphNode;

/**
 * Represents the result of executing a natural-language query
 * against the Software DNA Knowledge Graph.
 *
 * QueryResult keeps the original query context together with
 * the graph facts returned by the query executor.
 */
public class QueryResult {

    private final String originalQuestion;
    private final QueryIntent intent;
    private final GraphNode entity;
    private final List<GraphNode> nodes;

    public QueryResult(
            String originalQuestion,
            QueryIntent intent,
            GraphNode entity,
            List<GraphNode> nodes) {

        if (originalQuestion == null || originalQuestion.isBlank()) {
            throw new IllegalArgumentException(
                    "Original question cannot be null or blank."
            );
        }

        if (intent == null) {
            throw new IllegalArgumentException(
                    "QueryIntent cannot be null."
            );
        }

        if (entity == null) {
            throw new IllegalArgumentException(
                    "GraphNode entity cannot be null."
            );
        }

        if (nodes == null) {
            throw new IllegalArgumentException(
                    "Result nodes cannot be null."
            );
        }

        this.originalQuestion = originalQuestion;
        this.intent = intent;
        this.entity = entity;
        this.nodes = new ArrayList<>(nodes);
    }

    public String getOriginalQuestion() {
        return originalQuestion;
    }

    public QueryIntent getIntent() {
        return intent;
    }

    public GraphNode getEntity() {
        return entity;
    }

    /**
     * Returns the graph nodes found by the query.
     *
     * The returned list is read-only so callers cannot
     * accidentally modify the stored result.
     */
    public List<GraphNode> getNodes() {
        return Collections.unmodifiableList(nodes);
    }

    /**
     * Returns whether the query produced at least one result.
     */
    public boolean hasResults() {
        return !nodes.isEmpty();
    }

    /**
     * Returns the number of graph nodes returned by the query.
     */
    public int getResultCount() {
        return nodes.size();
    }

    @Override
    public String toString() {
        return "QueryResult{" +
                "originalQuestion='" + originalQuestion + '\'' +
                ", intent=" + intent +
                ", entity=" + entity.getName() +
                ", resultCount=" + nodes.size() +
                '}';
    }
}