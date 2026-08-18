package com.softwaredna.neo4j;

import com.softwaredna.analysis.repository.RepositoryAnalyzer;
import com.softwaredna.knowledge.KnowledgeGraph;
import com.softwaredna.knowledge.KnowledgeGraphBuilder;
import com.softwaredna.model.RepositoryModel;
import com.softwaredna.parser.RepositoryParser;

public class Neo4jExportTest {

    public static void main(
            String[] args) {

        try {

            /*
             * ---------------------------------------------------
             * Parse Repository
             * ---------------------------------------------------
             */

            RepositoryParser parser =
                    new RepositoryParser();

            RepositoryModel repository =
                    parser.parseRepository(
                            "../sample_projects"
                    );


            /*
             * ---------------------------------------------------
             * Run Repository Analysis
             * ---------------------------------------------------
             */

            RepositoryAnalyzer analyzer =
                    new RepositoryAnalyzer();

            analyzer.analyze(
                    repository
            );


            /*
             * ---------------------------------------------------
             * Build Existing Knowledge Graph
             * ---------------------------------------------------
             */

            KnowledgeGraphBuilder graphBuilder =
                    new KnowledgeGraphBuilder();

            KnowledgeGraph graph =
                    graphBuilder.build(
                            repository
                    );


            /*
             * ---------------------------------------------------
             * Connect to Neo4j
             * ---------------------------------------------------
             */

            String uri =
                    "bolt://localhost:7687";

            String username =
                    "neo4j";

            String password =
                    System.getenv("NEO4J_PASSWORD");


            try (
                    Neo4jConnection connection =
                            new Neo4jConnection(
                                    uri,
                                    username,
                                    password
                            )
            ) {

                /*
                 * Verify connection
                 */

                connection.verifyConnection();


                /*
                 * ------------------------------------------------
                 * Export graph
                 * ------------------------------------------------
                 */

                Neo4jGraphExporter exporter =
                        new Neo4jGraphExporter(
                                connection.getDriver()
                        );

                exporter.export(
                        graph
                );

            }

        }

        catch (Exception e) {

            System.out.println();
            System.out.println(
                    "Neo4j export failed!"
            );
            System.out.println();

            e.printStackTrace();

        }

    }

}