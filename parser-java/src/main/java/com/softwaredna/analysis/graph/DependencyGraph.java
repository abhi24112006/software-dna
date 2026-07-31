package com.softwaredna.analysis.graph;

import com.softwaredna.model.RelationshipType;

import java.util.*;

public class DependencyGraph {

    /*
     * Outgoing dependencies.
     *
     * source class
     *      ↓
     * target class
     *      ↓
     * TypedDependency
     */
    private final Map<String, Map<String, TypedDependency>> outgoingDependencies;

    /*
     * Incoming dependencies.
     */
    private final Map<String, Map<String, TypedDependency>> incomingDependencies;

    public DependencyGraph() {

        outgoingDependencies = new LinkedHashMap<>();
        incomingDependencies = new LinkedHashMap<>();

    }

    public void addDependency(
            String sourceId,
            String targetId,
            RelationshipType relationshipType) {

        if (sourceId == null || targetId == null) {
            return;
        }

        if (sourceId.equals(targetId)) {
            return;
        }

        TypedDependency outgoing =
                outgoingDependencies
                        .computeIfAbsent(
                                sourceId,
                                k -> new LinkedHashMap<>())
                        .computeIfAbsent(
                                targetId,
                                k -> new TypedDependency(sourceId, targetId));

        outgoing.addRelationshipType(relationshipType);

        TypedDependency incoming =
                incomingDependencies
                        .computeIfAbsent(
                                targetId,
                                k -> new LinkedHashMap<>())
                        .computeIfAbsent(
                                sourceId,
                                k -> new TypedDependency(sourceId, targetId));

        incoming.addRelationshipType(relationshipType);

    }

    public Set<String> getOutgoingDependencies(
            String classId) {

        return outgoingDependencies
                .getOrDefault(classId, Collections.emptyMap())
                .keySet();

    }

    public Set<String> getIncomingDependencies(
            String classId) {

        return incomingDependencies
                .getOrDefault(classId, Collections.emptyMap())
                .keySet();

    }

    public Collection<TypedDependency> getOutgoingEdges(
            String classId) {

        return outgoingDependencies
                .getOrDefault(classId, Collections.emptyMap())
                .values();

    }

    public Collection<TypedDependency> getOutgoingEdges(
            String classId,
            RelationshipType relationshipType) {

        List<TypedDependency> result =
                new ArrayList<>();

        for (TypedDependency dependency :
                getOutgoingEdges(classId)) {

            if (dependency.hasRelationshipType(relationshipType)) {

                result.add(dependency);

            }

        }

        return result;

    }

    public Collection<TypedDependency> getIncomingEdges(
            String classId) {

        return incomingDependencies
                .getOrDefault(classId, Collections.emptyMap())
                .values();

    }

    public Collection<TypedDependency> getIncomingEdges(
            String classId,
            RelationshipType relationshipType) {

        List<TypedDependency> result =
                new ArrayList<>();

        for (TypedDependency dependency :
                getIncomingEdges(classId)) {

            if (dependency.hasRelationshipType(relationshipType)) {

                result.add(dependency);

            }

        }

        return result;

    }

    public TypedDependency getDependency(
            String sourceId,
            String targetId) {

        return outgoingDependencies
                .getOrDefault(sourceId, Collections.emptyMap())
                .get(targetId);

    }

    public Set<String> getAllClasses() {

        Set<String> classes = new LinkedHashSet<>();

        classes.addAll(outgoingDependencies.keySet());
        classes.addAll(incomingDependencies.keySet());

        return classes;

    }

}