package com.softwaredna.parser.nlp;

import java.util.List;
import java.util.stream.Collectors;

import com.softwaredna.knowledge.GraphNode;

/**
 * Generates natural-language answers from graph-derived query results.
 *
 * This is the deterministic answer-generation layer.
 * It does not invent facts and does not use an LLM.
 */
public class AnswerGenerator {

    /**
     * Generates a natural-language answer for a query result.
     *
     * @param result graph-derived query result
     * @return natural-language answer
     */
    public String generate(QueryResult result) {

        if (result == null) {
            throw new IllegalArgumentException(
                    "QueryResult cannot be null."
            );
        }

        GraphNode entity = result.getEntity();
        List<GraphNode> nodes = result.getNodes();

        switch (result.getIntent()) {

            case DEPENDENCIES:
                return generateRelationshipAnswer(
                        entity,
                        nodes,
                        "depends on"
                );

            case DEPENDENTS:
                return generateRelationshipAnswer(
                        entity,
                        nodes,
                        "is depended on by"
                );

            case CALLEES:
                return generateRelationshipAnswer(
                        entity,
                        nodes,
                        "calls"
                );

            case CALLERS:
                return generateRelationshipAnswer(
                        entity,
                        nodes,
                        "is called by"
                );

            case SUBCLASSES:
                return generateRelationshipAnswer(
                        entity,
                        nodes,
                        "has subclasses"
                );

            case SUPERCLASS:
                return generateRelationshipAnswer(
                        entity,
                        nodes,
                        "has superclass"
                );

            case IMPLEMENTED_INTERFACES:
                return generateRelationshipAnswer(
                        entity,
                        nodes,
                        "implements"
                );

            case IMPLEMENTATIONS:
                return generateRelationshipAnswer(
                        entity,
                        nodes,
                        "has implementations"
                );

            case IMPACT:
                return generateRelationshipAnswer(
                        entity,
                        nodes,
                        "may affect"
                );

            case REACHABILITY:
                return generateRelationshipAnswer(
                        entity,
                        nodes,
                        "can reach"
                );

            case ARCHITECTURE:
                return generateRelationshipAnswer(
                        entity,
                        nodes,
                        "has architecture information"
                );

            case UNKNOWN:
            default:
                return "I could not determine the requested information.";
        }
    }

    private String generateRelationshipAnswer(
            GraphNode entity,
            List<GraphNode> nodes,
            String relationship) {

        if (nodes.isEmpty()) {
            return entity.getName()
                    + " "
                    + relationship
                    + " no known entities.";
        }

        String names = nodes.stream()
                .map(GraphNode::getName)
                .collect(Collectors.joining(", "));

        return entity.getName()
                + " "
                + relationship
                + " "
                + names
                + ".";
    }
}