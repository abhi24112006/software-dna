package com.softwaredna.knowledge;

import java.util.Objects;

public class GraphNode {

    private final String id;

    private final String name;

    private final NodeType type;

    public GraphNode(
            String id,
            String name,
            NodeType type) {

        this.id = id;
        this.name = name;
        this.type = type;

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public NodeType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;

        if (!(o instanceof GraphNode))
            return false;

        GraphNode node =
                (GraphNode) o;

        return Objects.equals(id, node.id);

    }

    @Override
    public int hashCode() {

        return Objects.hash(id);

    }

}