package com.softwaredna.parser.nlp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.softwaredna.knowledge.GraphNode;
import com.softwaredna.model.ClassMetrics;

/**
 * Represents the result of executing a natural-language query
 * against the Software DNA Knowledge Graph.
 *
 * QueryResult keeps the original query context together with
 * the graph facts returned by the query executor.
 *
 * For metric queries, the result can additionally contain
 * ClassMetrics for the requested class.
 */
public class QueryResult {

    private final String originalQuestion;
    private final QueryIntent intent;
    private final GraphNode entity;
    private final List<GraphNode> nodes;
    private final ClassMetrics metrics;

    /**
     * Backward-compatible constructor for structural graph queries.
     */
    public QueryResult(
            String originalQuestion,
            QueryIntent intent,
            GraphNode entity,
            List<GraphNode> nodes) {

        this(
                originalQuestion,
                intent,
                entity,
                nodes,
                null
        );
    }

    /**
     * Constructor that also supports metric results.
     */
    public QueryResult(
            String originalQuestion,
            QueryIntent intent,
            GraphNode entity,
            List<GraphNode> nodes,
            ClassMetrics metrics) {

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
        this.metrics = metrics;
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
     * Returns the class metrics associated with this query.
     *
     * This value is null for non-metric queries.
     */
    public ClassMetrics getMetrics() {
        return metrics;
    }

    /**
     * Returns whether metric data is present.
     */
    public boolean hasMetrics() {
        return metrics != null;
    }

    /**
     * Returns whether the query produced at least one result.
     *
     * For structural queries this is based on graph nodes.
     * Metric presence is handled separately through hasMetrics().
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
                ", hasMetrics=" + (metrics != null) +
                '}';
    }
}