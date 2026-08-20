package com.softwaredna.graph;

import java.util.List;
import java.util.Set;

import com.softwaredna.knowledge.EdgeType;
import com.softwaredna.knowledge.KnowledgeGraph;

public interface GraphRepository {

    /*
     * =======================================================
     * Graph Persistence
     * =======================================================
     */

    void save(
            KnowledgeGraph graph
    );


    /*
     * =======================================================
     * Graph Queries
     * =======================================================
     */

    List<String> getAllNodes();

    List<String> getDependencies(
            String nodeId
    );

    List<String> getDependents(
            String nodeId
    );

    List<String> getCallees(
            String methodId
    );

    List<String> getCallers(
            String methodId
    );

    List<String> getSubclasses(
            String classId
    );

    List<String> getSuperclass(
            String classId
    );

    List<String> getImplementedInterfaces(
            String classId
    );

    List<String> getImplementations(
            String interfaceId
    );

    


    /*
     * =======================================================
     * Impact Analysis
     * =======================================================
     */

    List<String> getImpact(
            String nodeId
    );

    List<String> getMethodImpact(
            String methodId
    );

    List<String> getContainmentAwareImpact(
            String nodeId
    );


    /*
     * =======================================================
     * Architecture Exploration
     * =======================================================
     */

    List<String> getReachableNodes(
            String nodeId,
            int depth
    );

    List<String> getArchitecturePaths(
            String nodeId,
            int depth
    );

        List<String> getArchitecturePaths(
        String nodeId,
        int depth,
        Set<EdgeType> relationshipTypes);

        List<String> getImpactPaths(
        String nodeId,
        int depth,
        Set<EdgeType> relationshipTypes);

        List<String> getClassNodes();

}