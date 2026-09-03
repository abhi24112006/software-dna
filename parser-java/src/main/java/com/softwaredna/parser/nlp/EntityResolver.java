package com.softwaredna.parser.nlp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import com.softwaredna.knowledge.GraphNode;
import com.softwaredna.knowledge.KnowledgeGraph;

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
     * 2. Exact fully-qualified/id match
     * 3. Case-insensitive name match
     * 4. Case-insensitive ID match
     * 5. Partial name match
     *
     * No graph node is created by this class.
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
     * Returns whether the graph contains an entity
     * matching the supplied name.
     */
    public boolean exists(String entityName) {

        return !resolve(entityName).isEmpty();
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