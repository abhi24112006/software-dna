package com.softwaredna.parser.nlp;

import java.util.List;

import com.softwaredna.knowledge.GraphNode;
import com.softwaredna.knowledge.query.KnowledgeGraphQuery;

/**
 * Executes a QueryPlan against the existing Knowledge Graph query layer.
 *
 * QueryExecutor connects the NLP query pipeline to
 * KnowledgeGraphQuery. It does not perform graph traversal itself.
 */
public class QueryExecutor {

    private final KnowledgeGraphQuery graphQuery;

    public QueryExecutor(KnowledgeGraphQuery graphQuery) {
        if (graphQuery == null) {
            throw new IllegalArgumentException(
                    "KnowledgeGraphQuery cannot be null."
            );
        }

        this.graphQuery = graphQuery;
    }

    /**
     * Executes a query plan using the supplied original question.
     *
     * @param plan the planned query
     * @param originalQuestion original natural-language question
     * @return structured query result
     */
    public QueryResult execute(
            QueryPlan plan,
            String originalQuestion) {

        if (plan == null) {
            throw new IllegalArgumentException(
                    "QueryPlan cannot be null."
            );
        }

        if (originalQuestion == null || originalQuestion.isBlank()) {
            throw new IllegalArgumentException(
                    "Original question cannot be null or blank."
            );
        }

        GraphNode entity = plan.getEntity();
        String entityId = entity.getId();

        List<GraphNode> nodes;

        switch (plan.getOperation()) {

            case GET_DEPENDENCIES:
                nodes = graphQuery.getDependencies(entityId);
                break;

            case GET_DEPENDENTS:
                nodes = graphQuery.getDependents(entityId);
                break;

            case GET_CALLEES:
                nodes = graphQuery.getCallees(entityId);
                break;

            case GET_CALLERS:
                nodes = graphQuery.getCallers(entityId);
                break;

            case GET_SUBCLASSES:
                nodes = graphQuery.getSubclasses(entityId);
                break;

            case GET_SUPERCLASS:
                nodes = graphQuery.getSuperclass(entityId);
                break;

            case GET_IMPLEMENTED_INTERFACES:
                nodes = graphQuery.getImplementedInterfaces(entityId);
                break;

            case GET_IMPLEMENTATIONS:
                nodes = graphQuery.getImplementations(entityId);
                break;

            case GET_IMPACT:
            case GET_REACHABILITY:
            case GET_ARCHITECTURE:
            case NONE:
            default:
                throw new UnsupportedOperationException(
                        "Query operation is not yet supported by " +
                        "KnowledgeGraphQuery: " +
                        plan.getOperation()
                );
        }

        return new QueryResult(
                originalQuestion,
                plan.getIntent(),
                entity,
                nodes
        );
    }

    /**
     * Backward-compatible convenience method.
     *
     * Generates a temporary description when the original
     * natural-language question is not supplied.
     *
     * @param plan query plan
     * @return structured query result
     */
    public QueryResult execute(QueryPlan plan) {

        if (plan == null) {
            throw new IllegalArgumentException(
                    "QueryPlan cannot be null."
            );
        }

        return execute(
                plan,
                plan.getIntent() +
                        " query for " +
                        plan.getEntity().getName()
        );
    }
}