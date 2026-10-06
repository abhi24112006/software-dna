package com.softwaredna.parser.nlp;

import java.util.List;

import com.softwaredna.knowledge.GraphNode;
import com.softwaredna.knowledge.Subgraph;
import com.softwaredna.model.ClassMetrics;

/**
 * Represents the result of executing a natural-language query.
 *
 * A query result may contain:
 * - graph nodes for structural queries
 * - metrics for metric queries
 * - a focused subgraph for graph-grounded reasoning
 */
public class QueryResult {

    private final String originalQuestion;
    private final QueryIntent intent;
    private final GraphNode entity;
    private final List<GraphNode> nodes;
    private final ClassMetrics metrics;
    private final Subgraph subgraph;

    /**
     * Backward-compatible constructor for structural queries.
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
                null,
                null
        );
    }

    /**
     * Backward-compatible constructor for metric queries.
     */
    public QueryResult(
            String originalQuestion,
            QueryIntent intent,
            GraphNode entity,
            List<GraphNode> nodes,
            ClassMetrics metrics) {

        this(
                originalQuestion,
                intent,
                entity,
                nodes,
                metrics,
                null
        );
    }

    /**
     * Creates a query result with metrics and/or a focused subgraph.
     */
    public QueryResult(
            String originalQuestion,
            QueryIntent intent,
            GraphNode entity,
            List<GraphNode> nodes,
            ClassMetrics metrics,
            Subgraph subgraph) {

        if (originalQuestion == null || originalQuestion.isBlank()) {
    throw new IllegalArgumentException(
            "Original question cannot be null or blank."
    );
}

        if (intent == null) {
            throw new IllegalArgumentException(
                    "Query intent cannot be null."
            );
        }

        if (entity == null) {
            throw new IllegalArgumentException(
                    "Query entity cannot be null."
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
        this.nodes = List.copyOf(nodes);
        this.metrics = metrics;
        this.subgraph = subgraph;
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

    public List<GraphNode> getNodes() {
        return nodes;
    }

    public ClassMetrics getMetrics() {
        return metrics;
    }

    public Subgraph getSubgraph() {
        return subgraph;
    }

    public boolean hasResults() {
        return !nodes.isEmpty();
    }

    public boolean hasMetrics() {
        return metrics != null;
    }

    public boolean hasSubgraph() {
        return subgraph != null;
    }

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
                ", hasSubgraph=" + (subgraph != null) +
                '}';
    }
}