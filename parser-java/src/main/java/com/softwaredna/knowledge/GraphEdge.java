package com.softwaredna.knowledge;

import java.util.Objects;

public class GraphEdge {

    private final GraphNode source;

    private final GraphNode target;

    private final EdgeType type;

    public GraphEdge(
            GraphNode source,
            GraphNode target,
            EdgeType type) {

        this.source = source;
        this.target = target;
        this.type = type;

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

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;

        if (!(obj instanceof GraphEdge))
            return false;

        GraphEdge other =
                (GraphEdge) obj;

        return Objects.equals(source.getId(), other.source.getId())
                && Objects.equals(target.getId(), other.target.getId())
                && type == other.type;

    }

    @Override
    public int hashCode() {

        return Objects.hash(
                source.getId(),
                target.getId(),
                type
        );

    }

}