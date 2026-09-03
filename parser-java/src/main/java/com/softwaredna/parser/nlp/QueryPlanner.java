package com.softwaredna.parser.nlp;

import com.softwaredna.knowledge.GraphNode;

/**
 * Converts a detected query intent and resolved graph entity
 * into an executable query plan.
 *
 * The planner does not execute graph queries.
 * It only determines which existing graph operation should
 * eventually be used.
 */
public class QueryPlanner {

    /**
     * Creates a query plan for the supplied intent and entity.
     *
     * @param intent detected natural-language query intent
     * @param entity resolved Knowledge Graph entity
     * @return query plan
     */
    public QueryPlan plan(QueryIntent intent, GraphNode entity) {

        if (intent == null) {
            throw new IllegalArgumentException("QueryIntent cannot be null.");
        }

        if (entity == null) {
            throw new IllegalArgumentException("GraphNode entity cannot be null.");
        }

        QueryOperation operation = mapIntentToOperation(intent);

        if (operation == QueryOperation.NONE) {
            throw new IllegalArgumentException(
                    "No query operation is available for intent: " + intent
            );
        }

        return new QueryPlan(intent, entity, operation);
    }

    /**
     * Maps a natural-language query intent to an existing
     * graph operation.
     */
    private QueryOperation mapIntentToOperation(QueryIntent intent) {

        switch (intent) {

            case DEPENDENCIES:
                return QueryOperation.GET_DEPENDENCIES;

            case DEPENDENTS:
                return QueryOperation.GET_DEPENDENTS;

            case CALLEES:
                return QueryOperation.GET_CALLEES;

            case CALLERS:
                return QueryOperation.GET_CALLERS;

            case SUBCLASSES:
                return QueryOperation.GET_SUBCLASSES;

            case SUPERCLASS:
                return QueryOperation.GET_SUPERCLASS;

            case IMPLEMENTED_INTERFACES:
                return QueryOperation.GET_IMPLEMENTED_INTERFACES;

            case IMPLEMENTATIONS:
                return QueryOperation.GET_IMPLEMENTATIONS;

            case IMPACT:
                return QueryOperation.GET_IMPACT;

            case REACHABILITY:
                return QueryOperation.GET_REACHABILITY;

            case ARCHITECTURE:
                return QueryOperation.GET_ARCHITECTURE;

            case UNKNOWN:
            default:
                return QueryOperation.NONE;
        }
    }
}