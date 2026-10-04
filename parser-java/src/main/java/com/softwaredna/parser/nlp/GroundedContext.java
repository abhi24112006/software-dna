package com.softwaredna.parser.nlp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.softwaredna.knowledge.GraphEdge;
import com.softwaredna.knowledge.GraphNode;
import com.softwaredna.knowledge.KnowledgeGraph;

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
    private final List<GroundedRelationship> relationships;

    /**
     * Creates grounded context from a graph query result.
     *
     * This constructor preserves the existing behavior and does not
     * include graph relationships.
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
        this.relationships = List.of();
    }

    /**
     * Creates grounded context from a graph query result and the
     * Knowledge Graph.
     *
     * Only relationships whose source and target are both present
     * in the grounded context are included.
     *
     * The grounded context consists of the target entity together
     * with the result nodes.
     *
     * Relationships are taken directly from the Knowledge Graph.
     * No relationships are inferred from node names or query intent.
     *
     * @param result graph-derived query result
     * @param knowledgeGraph source Knowledge Graph
     */
    public GroundedContext(
            QueryResult result,
            KnowledgeGraph knowledgeGraph) {

        if (result == null) {
            throw new IllegalArgumentException(
                    "QueryResult cannot be null."
            );
        }

        if (knowledgeGraph == null) {
            throw new IllegalArgumentException(
                    "KnowledgeGraph cannot be null."
            );
        }

        this.question = result.getOriginalQuestion();
        this.intent = result.getIntent();
        this.entity = result.getEntity();
        this.nodes = List.copyOf(result.getNodes());
        this.relationships = extractRelationships(
                this.entity,
                this.nodes,
                knowledgeGraph
        );
    }

    /**
     * Extracts graph relationships that connect two nodes in the
     * grounded context.
     *
     * The grounded context includes both the target entity and
     * the nodes returned by the query.
     */
    private List<GroundedRelationship> extractRelationships(
            GraphNode entity,
            List<GraphNode> groundedNodes,
            KnowledgeGraph knowledgeGraph) {

        Set<String> groundedNodeIds = new HashSet<>();

        if (entity != null) {
            groundedNodeIds.add(entity.getId());
        }

        for (GraphNode node : groundedNodes) {
            groundedNodeIds.add(node.getId());
        }

        List<GroundedRelationship> result = new ArrayList<>();

        for (GraphEdge edge : knowledgeGraph.getEdges()) {

            String sourceId = edge.getSource().getId();
            String targetId = edge.getTarget().getId();

            if (groundedNodeIds.contains(sourceId)
                    && groundedNodeIds.contains(targetId)) {

                result.add(
                        new GroundedRelationship(
                                edge.getSource(),
                                edge.getTarget(),
                                edge.getType()
                        )
                );
            }
        }

        return List.copyOf(result);
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

    /**
     * Returns graph-derived relationships relevant to the grounded
     * nodes.
     *
     * @return immutable list of grounded relationships
     */
    public List<GroundedRelationship> getRelationships() {
        return Collections.unmodifiableList(relationships);
    }
}