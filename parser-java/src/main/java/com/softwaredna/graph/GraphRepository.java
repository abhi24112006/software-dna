package com.softwaredna.graph;

import java.util.List;

import com.softwaredna.knowledge.KnowledgeGraph;

public interface GraphRepository {

    void save(KnowledgeGraph graph);

    List<String> getDependencies(String nodeId);

    List<String> getDependents(String nodeId);

    List<String> getCallees(String methodId);

    List<String> getCallers(String methodId);

    List<String> getSubclasses(String classId);

    List<String> getSuperclass(String classId);

    List<String> getImplementedInterfaces(String classId);

    List<String> getImplementations(String interfaceId);

    List<String> getImpact(String nodeId);

    List<String> getMethodImpact(String methodId);

    List<String> getContainmentAwareImpact(String nodeId);

}