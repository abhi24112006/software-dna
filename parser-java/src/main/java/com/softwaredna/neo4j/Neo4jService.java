package com.softwaredna.neo4j;

import com.softwaredna.knowledge.KnowledgeGraph;

public class Neo4jService implements AutoCloseable {

    private final Neo4jConnection connection;

    private final Neo4jGraphExporter exporter;

    private final Neo4jQueryService queryService;

    private final Neo4jImpactAnalyzer impactAnalyzer;


    public Neo4jService(
            String uri,
            String username,
            String password,
            String database) {

        connection =
                new Neo4jConnection(
                        uri,
                        username,
                        password
                );

        exporter =
                new Neo4jGraphExporter(
                        connection.getDriver()
                );

        queryService =
                new Neo4jQueryService(
                        connection.getDriver(),
                        database
                );

        impactAnalyzer =
                new Neo4jImpactAnalyzer(
                        connection.getDriver(),
                        database
                );

    }


    public void verifyConnection() {

        connection.verifyConnection();

    }


    public void export(
            KnowledgeGraph graph) {

        exporter.export(graph);

    }


    public Neo4jQueryService getQueryService() {

        return queryService;

    }


    public Neo4jImpactAnalyzer getImpactAnalyzer() {

        return impactAnalyzer;

    }


    @Override
    public void close() {

        connection.close();

    }

}