package com.softwaredna.parser.nlp;

import java.util.Objects;

import com.softwaredna.knowledge.EdgeType;
import com.softwaredna.knowledge.GraphNode;
import com.softwaredna.model.SourceEvidence;

/**
 * Represents a verified relationship retrieved from the
 * Software DNA knowledge graph for grounded reasoning.
 *
 * <p>This class contains only graph-derived information.
 * It does not infer or generate relationships.</p>
 */
public class GroundedRelationship {

    private final GraphNode source;
    private final GraphNode target;
    private final EdgeType type;
    private final SourceEvidence sourceEvidence;

    /**
     * Creates a grounded relationship without source evidence.
     *
     * <p>Kept for backward compatibility.</p>
     *
     * @param source source graph entity
     * @param target target graph entity
     * @param type graph relationship type
     */
    public GroundedRelationship(
            GraphNode source,
            GraphNode target,
            EdgeType type) {

        this(source, target, type, null);
    }

    /**
     * Creates a grounded relationship with source evidence.
     *
     * @param source source graph entity
     * @param target target graph entity
     * @param type graph relationship type
     * @param sourceEvidence source-level evidence for the relationship
     */
    public GroundedRelationship(
            GraphNode source,
            GraphNode target,
            EdgeType type,
            SourceEvidence sourceEvidence) {

        if (source == null) {
            throw new IllegalArgumentException(
                    "Source graph node cannot be null.");
        }

        if (target == null) {
            throw new IllegalArgumentException(
                    "Target graph node cannot be null.");
        }

        if (type == null) {
            throw new IllegalArgumentException(
                    "Edge type cannot be null.");
        }

        this.source = source;
        this.target = target;
        this.type = type;
        this.sourceEvidence = sourceEvidence;
    }

    public GraphNode getSource() {
        return source;
    }

    public GraphNode getTarget() {
        return target;
    }

    public EdgeType getType() {
        return type;
    }

    /**
     * Returns the source-level evidence associated with this relationship.
     *
     * @return source evidence, or null when evidence is unavailable
     */
    public SourceEvidence getSourceEvidence() {
        return sourceEvidence;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof GroundedRelationship)) {
            return false;
        }

        GroundedRelationship other =
                (GroundedRelationship) obj;

        return Objects.equals(
                        source.getId(),
                        other.source.getId())
                && Objects.equals(
                        target.getId(),
                        other.target.getId())
                && type == other.type;
    }

    @Override
    public int hashCode() {

        return Objects.hash(
                source.getId(),
                target.getId(),
                type);
    }
}