package com.softwaredna.parser.nlp;

import java.util.Collections;
import java.util.List;

import com.softwaredna.knowledge.GraphNode;

/**
 * Represents factual context retrieved from the Knowledge Graph
 * for use by an LLM.
 *
 * This class contains only graph-derived information.
 * It does not generate or infer facts.
 */
public class GroundedContext {

    private final String question;
    private final QueryIntent intent;
    private final GraphNode entity;
    private final List<GraphNode> nodes;

    /**
     * Creates grounded context from a graph query result.
     *
     * @param result graph-derived query result
     */
    public GroundedContext(QueryResult result) {

        if (result == null) {
            throw new IllegalArgumentException(
                    "QueryResult cannot be null."
            );
        }

        this.question = result.getOriginalQuestion();
        this.intent = result.getIntent();
        this.entity = result.getEntity();
        this.nodes = List.copyOf(result.getNodes());
    }

    public String getQuestion() {
        return question;
    }

    public QueryIntent getIntent() {
        return intent;
    }

    public GraphNode getEntity() {
        return entity;
    }

    public List<GraphNode> getNodes() {
        return Collections.unmodifiableList(nodes);
    }
}