package com.softwaredna.parser.nlp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import com.softwaredna.knowledge.GraphNode;
import com.softwaredna.knowledge.KnowledgeGraph;
import com.softwaredna.knowledge.NodeType;

/**
 * Resolves an entity mentioned in a natural-language query
 * to one or more nodes in the Knowledge Graph.
 *
 * The resolver is deliberately deterministic.
 * It does not use an LLM and does not invent entities.
 */
public class EntityResolver {

    private final KnowledgeGraph graph;

    public EntityResolver(KnowledgeGraph graph) {

        if (graph == null) {
            throw new IllegalArgumentException(
                    "KnowledgeGraph cannot be null."
            );
        }

        this.graph = graph;
    }

    /**
     * Resolves an entity name to matching graph nodes.
     *
     * Matching priority:
     *
     * 1. Exact node name
     * 2. Exact ID
     * 3. Case-insensitive name or ID
     * 4. Partial name match
     */
    public List<GraphNode> resolve(String entityName) {

        List<GraphNode> exactMatches = new ArrayList<>();
        List<GraphNode> caseInsensitiveMatches = new ArrayList<>();
        List<GraphNode> partialMatches = new ArrayList<>();

        if (entityName == null || entityName.isBlank()) {
            return exactMatches;
        }

        String query = normalize(entityName);

        Collection<GraphNode> nodes = graph.getNodes();

        for (GraphNode node : nodes) {

            String nodeName = normalize(node.getName());
            String nodeId = normalize(node.getId());

            // Exact name match
            if (nodeName.equals(query)) {
                exactMatches.add(node);
                continue;
            }

            // Exact ID match
            if (nodeId.equals(query)) {
                exactMatches.add(node);
                continue;
            }

            // Case-insensitive name or ID match
            if (nodeName.equalsIgnoreCase(query)
                    || nodeId.equalsIgnoreCase(query)) {

                caseInsensitiveMatches.add(node);
                continue;
            }

            // Partial name match
            if (nodeName.contains(query)
                    || query.contains(nodeName)) {

                partialMatches.add(node);
            }
        }

        if (!exactMatches.isEmpty()) {
            return exactMatches;
        }

        if (!caseInsensitiveMatches.isEmpty()) {
            return caseInsensitiveMatches;
        }

        return partialMatches;
    }

    /**
     * Resolves an entity using both its name and the expected node type.
     *
     * This prevents ambiguity when multiple graph entities share
     * the same name, such as a CLASS named "User" and another
     * graph entity with the same name.
     *
     * Matching priority:
     *
     * 1. Exact name/ID + expected type
     * 2. Case-insensitive name/ID + expected type
     * 3. Partial name + expected type
     * 4. Existing untyped resolution as fallback
     */
    public List<GraphNode> resolve(
            String entityName,
            NodeType expectedType) {

        if (expectedType == null) {
            return resolve(entityName);
        }

        List<GraphNode> typedMatches = new ArrayList<>();

        if (entityName == null || entityName.isBlank()) {
            return typedMatches;
        }

        String query = normalize(entityName);

        Collection<GraphNode> nodes = graph.getNodes();

        // First pass: exact name or ID + expected type
        for (GraphNode node : nodes) {

            if (node.getType() != expectedType) {
                continue;
            }

            String nodeName = normalize(node.getName());
            String nodeId = normalize(node.getId());

            if (nodeName.equals(query) || nodeId.equals(query)) {
                typedMatches.add(node);
            }
        }

        if (!typedMatches.isEmpty()) {
            return typedMatches;
        }

        // Second pass: case-insensitive + expected type
        for (GraphNode node : nodes) {

            if (node.getType() != expectedType) {
                continue;
            }

            String nodeName = normalize(node.getName());
            String nodeId = normalize(node.getId());

            if (nodeName.equalsIgnoreCase(query)
                    || nodeId.equalsIgnoreCase(query)) {

                typedMatches.add(node);
            }
        }

        if (!typedMatches.isEmpty()) {
            return typedMatches;
        }

        // Third pass: partial match + expected type
        for (GraphNode node : nodes) {

            if (node.getType() != expectedType) {
                continue;
            }

            String nodeName = normalize(node.getName());

            if (nodeName.contains(query)
                    || query.contains(nodeName)) {

                typedMatches.add(node);
            }
        }

        return typedMatches;
    }

    /**
     * Resolves an entity and returns it only when
     * exactly one node matches.
     *
     * Returns null when:
     *
     * - no entity matches
     * - multiple entities match
     */
    public GraphNode resolveUnique(String entityName) {

        List<GraphNode> matches = resolve(entityName);

        if (matches.size() != 1) {
            return null;
        }

        return matches.get(0);
    }

    /**
     * Resolves an entity using an expected node type and returns it
     * only when exactly one typed node matches.
     */
    public GraphNode resolveUnique(
            String entityName,
            NodeType expectedType) {

        List<GraphNode> matches =
                resolve(entityName, expectedType);

        if (matches.size() != 1) {
            return null;
        }

        return matches.get(0);
    }

    /**
     * Returns whether the graph contains an entity
     * matching the supplied name.
     */
    public boolean exists(String entityName) {

        return !resolve(entityName).isEmpty();
    }

    /**
     * Returns whether the graph contains an entity matching
     * the supplied name and expected node type.
     */
    public boolean exists(
            String entityName,
            NodeType expectedType) {

        return !resolve(entityName, expectedType).isEmpty();
    }

    private String normalize(String value) {

        if (value == null) {
            return "";
        }

        return value
                .trim()
                .toLowerCase(Locale.ROOT);
    }
}