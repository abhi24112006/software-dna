package com.softwaredna.analysis.graph;

import com.softwaredna.model.RelationshipType;

import java.util.Collection;

public class GraphQueryService {

    private final DependencyGraph graph;

    public GraphQueryService(
            DependencyGraph graph) {

        this.graph = graph;

    }

    /*
     * ============================
     * Inheritance Queries
     * ============================
     */

    public Collection<TypedDependency> getParents(
            String classId) {

        return graph.getOutgoingEdges(
                classId,
                RelationshipType.EXTENDS);

    }

    public Collection<TypedDependency> getChildren(
            String classId) {

        return graph.getIncomingEdges(
                classId,
                RelationshipType.EXTENDS);

    }

    /*
     * ============================
     * Coupling Queries
     * ============================
     */

    public Collection<TypedDependency> getFieldDependencies(
            String classId) {

        return graph.getOutgoingEdges(
                classId,
                RelationshipType.FIELD_DEPENDENCY);

    }

    public Collection<TypedDependency> getParameterDependencies(
            String classId) {

        return graph.getOutgoingEdges(
                classId,
                RelationshipType.PARAMETER_DEPENDENCY);

    }

    public Collection<TypedDependency> getReturnDependencies(
            String classId) {

        return graph.getOutgoingEdges(
                classId,
                RelationshipType.RETURN_DEPENDENCY);

    }

    /*
     * ============================
     * General Queries
     * ============================
     */

    public Collection<TypedDependency> getOutgoingDependencies(
            String classId) {

        return graph.getOutgoingEdges(classId);

    }

    public Collection<TypedDependency> getIncomingDependencies(
            String classId) {

        return graph.getIncomingEdges(classId);

    }

}