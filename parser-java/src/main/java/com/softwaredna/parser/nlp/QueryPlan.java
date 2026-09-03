package com.softwaredna.parser.nlp;

import com.softwaredna.knowledge.GraphNode;

/**
 * Represents a planned graph query derived from a natural-language question.
 *
 * QueryPlan does not execute the query.
 * It only describes:
 * - what the user wants to know
 * - which graph entity the question refers to
 * - which graph operation should eventually be executed
 */
public class QueryPlan {

    private final QueryIntent intent;
    private final GraphNode entity;
    private final QueryOperation operation;

    public QueryPlan(
            QueryIntent intent,
            GraphNode entity,
            QueryOperation operation) {

        if (intent == null) {
            throw new IllegalArgumentException("QueryIntent cannot be null.");
        }

        if (entity == null) {
            throw new IllegalArgumentException("GraphNode entity cannot be null.");
        }

        if (operation == null) {
            throw new IllegalArgumentException("QueryOperation cannot be null.");
        }

        this.intent = intent;
        this.entity = entity;
        this.operation = operation;
    }

    public QueryIntent getIntent() {
        return intent;
    }

    public GraphNode getEntity() {
        return entity;
    }

    public QueryOperation getOperation() {
        return operation;
    }

    @Override
    public String toString() {
        return "QueryPlan{" +
                "intent=" + intent +
                ", entity=" + entity.getName() +
                ", operation=" + operation +
                '}';
    }
}