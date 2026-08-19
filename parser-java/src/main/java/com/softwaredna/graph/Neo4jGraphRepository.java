package com.softwaredna.graph;

import java.util.List;

import com.softwaredna.knowledge.KnowledgeGraph;
import com.softwaredna.neo4j.Neo4jService;

public class Neo4jGraphRepository
        implements GraphRepository {

    private final Neo4jService neo4j;


    public Neo4jGraphRepository(
            Neo4jService neo4j) {

        this.neo4j = neo4j;

    }


    /*
     * =======================================================
     * Graph Queries
     * =======================================================
     */

    @Override
    public List<String> getDependencies(
            String nodeId) {

        return neo4j.getQueryService()
                .getDependencies(nodeId);

    }


    @Override
    public List<String> getDependents(
            String nodeId) {

        return neo4j.getQueryService()
                .getDependents(nodeId);

    }


    @Override
    public List<String> getCallees(
            String methodId) {

        return neo4j.getQueryService()
                .getCallees(methodId);

    }


    @Override
    public List<String> getCallers(
            String methodId) {

        return neo4j.getQueryService()
                .getCallers(methodId);

    }


    @Override
    public List<String> getSubclasses(
            String classId) {

        return neo4j.getQueryService()
                .getSubclasses(classId);

    }


    @Override
    public List<String> getSuperclass(
            String classId) {

        return neo4j.getQueryService()
                .getSuperclass(classId);

    }


    @Override
    public List<String> getImplementedInterfaces(
            String classId) {

        return neo4j.getQueryService()
                .getImplementedInterfaces(classId);

    }


    @Override
    public List<String> getImplementations(
            String interfaceId) {

        return neo4j.getQueryService()
                .getImplementations(interfaceId);

    }

    @Override
    public void save(KnowledgeGraph graph) {

        neo4j.export(graph);

}


    /*
     * =======================================================
     * Impact Analysis
     * =======================================================
     */

    @Override
    public List<String> getImpact(
            String nodeId) {

        return neo4j.getImpactAnalyzer()
                .getImpact(nodeId);

    }


    @Override
    public List<String> getMethodImpact(
            String methodId) {

        return neo4j.getImpactAnalyzer()
                .getMethodImpact(methodId);

    }


    @Override
    public List<String> getContainmentAwareImpact(
            String nodeId) {

        return neo4j.getImpactAnalyzer()
                .getContainmentAwareImpact(nodeId);

    }

}