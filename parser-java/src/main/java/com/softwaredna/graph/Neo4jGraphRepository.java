package com.softwaredna.graph;

import java.util.List;

import com.softwaredna.neo4j.Neo4jImpactAnalyzer;
import com.softwaredna.neo4j.Neo4jQueryService;

public class Neo4jGraphRepository
        implements GraphRepository {

    private final Neo4jQueryService queryService;

    private final Neo4jImpactAnalyzer impactAnalyzer;


    public Neo4jGraphRepository(
            Neo4jQueryService queryService,
            Neo4jImpactAnalyzer impactAnalyzer) {

        this.queryService =
                queryService;

        this.impactAnalyzer =
                impactAnalyzer;

    }


    /*
     * -------------------------------------------------------
     * Graph Queries
     * -------------------------------------------------------
     */

    @Override
    public List<String> getDependencies(
            String nodeId) {

        return queryService.getDependencies(
                nodeId
        );

    }


    @Override
    public List<String> getDependents(
            String nodeId) {

        return queryService.getDependents(
                nodeId
        );

    }


    @Override
    public List<String> getCallees(
            String methodId) {

        return queryService.getCallees(
                methodId
        );

    }


    @Override
    public List<String> getCallers(
            String methodId) {

        return queryService.getCallers(
                methodId
        );

    }


    @Override
    public List<String> getSubclasses(
            String classId) {

        return queryService.getSubclasses(
                classId
        );

    }


    @Override
    public List<String> getSuperclass(
            String classId) {

        return queryService.getSuperclass(
                classId
        );

    }


    @Override
    public List<String> getImplementedInterfaces(
            String classId) {

        return queryService.getImplementedInterfaces(
                classId
        );

    }


    @Override
    public List<String> getImplementations(
            String interfaceId) {

        return queryService.getImplementations(
                interfaceId
        );

    }


    /*
     * -------------------------------------------------------
     * Impact Analysis
     * -------------------------------------------------------
     */

    @Override
    public List<String> getImpact(
            String nodeId) {

        return impactAnalyzer.getImpact(
                nodeId
        );

    }


    @Override
    public List<String> getMethodImpact(
            String methodId) {

        return impactAnalyzer.getMethodImpact(
                methodId
        );

    }


    @Override
    public List<String> getContainmentAwareImpact(
            String nodeId) {

        return impactAnalyzer.getContainmentAwareImpact(
                nodeId
        );

    }

}