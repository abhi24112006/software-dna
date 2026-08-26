package com.softwaredna.parser;

import java.util.List;

import com.softwaredna.analysis.architecture.ArchitectureAnalyzer;
import com.softwaredna.analysis.architecture.ArchitectureDiff;
import com.softwaredna.analysis.architecture.ArchitectureDiffAnalyzer;
import com.softwaredna.analysis.architecture.ArchitectureDiffPrinter;
import com.softwaredna.analysis.architecture.ArchitectureGraph;
import com.softwaredna.analysis.architecture.ArchitectureGraphAnalyzer;
import com.softwaredna.analysis.architecture.ArchitectureGraphPrinter;
import com.softwaredna.analysis.architecture.ArchitectureHealthAnalyzer;
import com.softwaredna.analysis.architecture.ArchitectureHealthReport;
import com.softwaredna.analysis.architecture.ArchitectureRecommendation;
import com.softwaredna.analysis.architecture.ArchitectureRecommendationAnalyzer;
import com.softwaredna.analysis.architecture.ArchitectureReport;
import com.softwaredna.analysis.architecture.ArchitectureSnapshot;
import com.softwaredna.analysis.architecture.ArchitectureSnapshotLoader;
import com.softwaredna.analysis.architecture.ArchitectureSnapshotStore;
import com.softwaredna.analysis.architecture.ArchitectureTrend;
import com.softwaredna.analysis.architecture.ArchitectureTrendAnalyzer;
import com.softwaredna.analysis.architecture.ArchitectureTrendPrinter;
import com.softwaredna.analysis.repository.RepositoryAnalyzer;
import com.softwaredna.graph.GraphRepository;
import com.softwaredna.graph.Neo4jGraphRepository;
import com.softwaredna.knowledge.EdgeType;
import com.softwaredna.knowledge.GraphNode;
import com.softwaredna.knowledge.KnowledgeGraph;
import com.softwaredna.knowledge.KnowledgeGraphBuilder;
import com.softwaredna.knowledge.printer.KnowledgeGraphPrinter;
import com.softwaredna.knowledge.query.ImpactAnalyzer;
import com.softwaredna.knowledge.query.KnowledgeGraphQuery;
import com.softwaredna.model.RepositoryModel;
import com.softwaredna.neo4j.Neo4jConfig;
import com.softwaredna.neo4j.Neo4jService;
import com.softwaredna.printer.RepositoryPrinter;


public class ParserApplication {

    public static void main(String[] args) {

        try {

            /*
             * =================================================
             * Parse Repository
             * =================================================
             */

            RepositoryParser parser =
                    new RepositoryParser();
            RepositoryModel repository =
                    parser.parseRepository(
                            "../sample_projects/layered_violation_test"
                    );


            /*
             * =================================================
             * Run Repository Analyses
             * =================================================
             */

            RepositoryAnalyzer analyzer =
                    new RepositoryAnalyzer();

            analyzer.analyze(repository);


            /*
             * =================================================
             * Build Knowledge Graph
             * =================================================
             */

            KnowledgeGraphBuilder graphBuilder =
                    new KnowledgeGraphBuilder();

            KnowledgeGraph graph =
                    graphBuilder.build(repository);


            /*
             * =================================================
             * Neo4j Integration
             * =================================================
             */

            Neo4jConfig neo4jConfig =
                    Neo4jConfig.fromEnvironment();


            try (
                    Neo4jService neo4j =
                            new Neo4jService(
                                    neo4jConfig
                            )
            ) {

                /*
                 * -------------------------------------------------
                 * Verify Neo4j Connection
                 * -------------------------------------------------
                 */

                neo4j.verifyConnection();


                /*
                 * =================================================
                 * Graph Repository
                 * =================================================
                 */

                GraphRepository graphRepository =
                        new Neo4jGraphRepository(
                                neo4j
                        );


                /*
                 * =================================================
                 * Export Knowledge Graph
                 * =================================================
                 */

                graphRepository.save(graph);


                /*
                 * =================================================
                 * Architecture Recovery
                 * =================================================
                 */

                ArchitectureAnalyzer architectureAnalyzer =
                        new ArchitectureAnalyzer(
                                graphRepository
                        );


                System.out.println();



                ArchitectureReport architectureReport =
                        architectureAnalyzer.analyze(
                                graphRepository.getClassNodes()
                        );

        ArchitectureGraphAnalyzer graphAnalyzer =
        new ArchitectureGraphAnalyzer(
                graphRepository
        );

ArchitectureGraph architectureGraph =
        graphAnalyzer.build(
                architectureReport
        );

ArchitectureGraphPrinter graphPrinter =
        new ArchitectureGraphPrinter();

graphPrinter.print(
        architectureGraph
);


                architectureReport.print();

                ArchitectureHealthAnalyzer healthAnalyzer =
        new ArchitectureHealthAnalyzer();

ArchitectureHealthReport healthReport =
        healthAnalyzer.analyze(
                architectureReport
        );

healthReport.print();

ArchitectureSnapshot currentSnapshot =
        new ArchitectureSnapshot(
                architectureReport,
                healthReport,
                graphRepository.getClassNodes()
        );


ArchitectureSnapshotStore architectureSnapshotStore =
        new ArchitectureSnapshotStore();


ArchitectureSnapshotLoader snapshotLoader =
        new ArchitectureSnapshotLoader();


ArchitectureSnapshotLoader.SnapshotData
        previousSnapshotData =
        snapshotLoader.exists()
                ? snapshotLoader.load()
                : null;


architectureSnapshotStore.setCurrent(
        currentSnapshot
);


architectureSnapshotStore.saveCurrentAsPrevious();


if (previousSnapshotData != null) {

    ArchitectureSnapshotLoader.SnapshotData
            currentSnapshotData =
            snapshotLoader.load();


    ArchitectureDiffAnalyzer diffAnalyzer =
            new ArchitectureDiffAnalyzer();


    ArchitectureDiff diff =
            diffAnalyzer.analyze(
                    previousSnapshotData,
                    currentSnapshotData
            );


    /*
     * ===================================================
     * Store Diff For Architecture Trend
     * ===================================================
     */

    architectureSnapshotStore.addDiff(
            diff
    );


    /*
     * ===================================================
     * Architecture Evolution
     * ===================================================
     */

    ArchitectureDiffPrinter diffPrinter =
            new ArchitectureDiffPrinter();


    diffPrinter.print(
            diff
    );


    /*
     * ===================================================
     * Architecture Trend
     * ===================================================
     */

    ArchitectureTrendAnalyzer trendAnalyzer =
            new ArchitectureTrendAnalyzer();


    if (!architectureSnapshotStore
            .getHistory()
            .isEmpty()) {

        ArchitectureDiff firstDiff =
                architectureSnapshotStore
                        .getHistory()
                        .get(0);


        double startingHealth =
                firstDiff.getPreviousHealth();


        ArchitectureTrend trend =
                trendAnalyzer.analyze(
                        architectureSnapshotStore
                                .getHistory(),
                        startingHealth,
                        healthReport.getOverallScore()
                );


        ArchitectureTrendPrinter trendPrinter =
                new ArchitectureTrendPrinter();


        trendPrinter.print(
                trend
        );

    }

}
else {

    System.out.println();

    System.out.println(
            "======================================"
    );

    System.out.println(
            "Architecture Evolution"
    );

    System.out.println(
            "======================================"
    );

    System.out.println();

    System.out.println(
            "No previous architecture snapshot found."
    );

    System.out.println(
            "Current architecture saved as the baseline."
    );

    System.out.println();

}

ArchitectureRecommendationAnalyzer
        recommendationAnalyzer =
        new ArchitectureRecommendationAnalyzer();

List<ArchitectureRecommendation>
        recommendations =
        recommendationAnalyzer.analyze(
                architectureReport
        );

        System.out.println();

System.out.println(
        "======================================"
);

System.out.println(
        "Architecture Recommendations"
);

System.out.println(
        "======================================"
);

for (ArchitectureRecommendation recommendation :
        recommendations) {

    recommendation.print();

}


                /*
                 * =================================================
                 * Neo4j Graph Queries
                 * =================================================
                 */

                System.out.println();

                System.out.println(
                        "======================================"
                );

                System.out.println(
                        "Neo4j Graph Queries"
                );

                System.out.println(
                        "======================================"
                );


                /*
                 * -------------------------------------------------
                 * Dependencies
                 * -------------------------------------------------
                 */

                System.out.println();

                System.out.println(
                        "Dependencies of StudentService:"
                );

                for (String name :
                        graphRepository.getDependencies(
                                "Default Package.StudentService"
                        )) {

                    System.out.println(
                            "  -> " + name
                    );

                }


                /*
                 * -------------------------------------------------
                 * Dependents
                 * -------------------------------------------------
                 */

                System.out.println();

                System.out.println(
                        "Dependents of Student:"
                );

                for (String name :
                        graphRepository.getDependents(
                                "Default Package.Student"
                        )) {

                    System.out.println(
                            "  -> " + name
                    );

                }


                /*
                 * -------------------------------------------------
                 * Callees
                 * -------------------------------------------------
                 */

                System.out.println();

                System.out.println(
                        "Callees of Student.study():"
                );

                for (String name :
                        graphRepository.getCallees(
                                "Default Package.Student#study()"
                        )) {

                    System.out.println(
                            "  -> " + name
                    );

                }


                /*
                 * -------------------------------------------------
                 * Callers
                 * -------------------------------------------------
                 */

                System.out.println();

                System.out.println(
                        "Callers of Teacher.teach():"
                );

                for (String name :
                        graphRepository.getCallers(
                                "Default Package.Teacher#teach()"
                        )) {

                    System.out.println(
                            "  -> " + name
                    );

                }


                /*
                 * -------------------------------------------------
                 * Subclasses
                 * -------------------------------------------------
                 */

                System.out.println();

                System.out.println(
                        "Subclasses of Animal:"
                );

                for (String name :
                        graphRepository.getSubclasses(
                                "Default Package.Animal"
                        )) {

                    System.out.println(
                            "  -> " + name
                    );

                }


                /*
                 * -------------------------------------------------
                 * Superclass
                 * -------------------------------------------------
                 */

                System.out.println();

                System.out.println(
                        "Superclass of Mammal:"
                );

                for (String name :
                        graphRepository.getSuperclass(
                                "Default Package.Mammal"
                        )) {

                    System.out.println(
                            "  -> " + name
                    );

                }


                /*
                 * -------------------------------------------------
                 * Implemented Interfaces
                 * -------------------------------------------------
                 */

                System.out.println();

                System.out.println(
                        "Interfaces implemented by Report:"
                );

                for (String name :
                        graphRepository.getImplementedInterfaces(
                                "demo.Report"
                        )) {

                    System.out.println(
                            "  -> " + name
                    );

                }


                /*
                 * -------------------------------------------------
                 * Implementations
                 * -------------------------------------------------
                 */

                System.out.println();

                System.out.println(
                        "Implementations of Printable:"
                );

                for (String name :
                        graphRepository.getImplementations(
                                "com.demo.Printable"
                        )) {

                    System.out.println(
                            "  -> " + name
                    );

                }


                /*
                 * =================================================
                 * Neo4j Impact Analysis
                 * =================================================
                 */

                System.out.println();

                System.out.println(
                        "======================================"
                );

                System.out.println(
                        "Neo4j Impact Analysis"
                );

                System.out.println(
                        "======================================"
                );


                /*
                 * -------------------------------------------------
                 * Student Impact
                 * -------------------------------------------------
                 */

                System.out.println();

                System.out.println(
                        "Impact of changing Student:"
                );

                for (String name :
                        graphRepository.getImpact(
                                "Default Package.Student"
                        )) {

                    System.out.println(
                            "  -> " + name
                    );

                }


                /*
                 * -------------------------------------------------
                 * Method Impact
                 * -------------------------------------------------
                 */

                System.out.println();

                System.out.println(
                        "Impact of changing Teacher.teach():"
                );

                for (String name :
                        graphRepository.getMethodImpact(
                                "Default Package.Teacher#teach()"
                        )) {

                    System.out.println(
                            "  -> " + name
                    );

                }


                /*
                 * -------------------------------------------------
                 * Containment-Aware Impact
                 * -------------------------------------------------
                 */

                System.out.println();

                System.out.println(
                        "Containment-Aware Impact Analysis"
                );

                System.out.println();

                System.out.println(
                        "Impact of changing Teacher:"
                );

                for (String name :
                        graphRepository.getContainmentAwareImpact(
                                "Default Package.Teacher"
                        )) {

                    System.out.println(
                            "  -> " + name
                    );

                }


                /*
                 * =================================================
                 * Architecture Exploration
                 * =================================================
                 */

                System.out.println();

                System.out.println(
                        "======================================"
                );

                System.out.println(
                        "Architecture Exploration"
                );

                System.out.println(
                        "======================================"
                );


                /*
                 * -------------------------------------------------
                 * Reachable Nodes - Depth 1
                 * -------------------------------------------------
                 */

                System.out.println();

                System.out.println(
                        "Reachable from StudentService (depth 1):"
                );

                for (String name :
                        graphRepository.getReachableNodes(
                                "Default Package.StudentService",
                                1
                        )) {

                    System.out.println(
                            "  -> " + name
                    );

                }


                /*
                 * -------------------------------------------------
                 * Reachable Nodes - Depth 2
                 * -------------------------------------------------
                 */

                System.out.println();

                System.out.println(
                        "Reachable from StudentService (depth 2):"
                );

                for (String name :
                        graphRepository.getReachableNodes(
                                "Default Package.StudentService",
                                2
                        )) {

                    System.out.println(
                            "  -> " + name
                    );

                }


                /*
                 * =================================================
                 * Architecture Paths
                 * =================================================
                 */

                System.out.println();

                System.out.println(
                        "======================================"
                );

                System.out.println(
                        "Architecture Paths"
                );

                System.out.println(
                        "======================================"
                );


                System.out.println();

                System.out.println(
                        "Paths from StudentService (depth 2):"
                );


                for (String path :
                        graphRepository.getArchitecturePaths(
                                "Default Package.StudentService",
                                2
                        )) {

                    System.out.println(
                            "  -> " + path
                    );

                }


                /*
                 * =================================================
                 * Dependency Architecture Paths
                 * =================================================
                 */

                System.out.println();

                System.out.println(
                        "======================================"
                );

                System.out.println(
                        "Dependency Architecture Paths"
                );

                System.out.println(
                        "======================================"
                );


                for (String path :
                        graphRepository.getArchitecturePaths(
                                "Default Package.StudentService",
                                2,
                                java.util.Set.of(
                                        EdgeType.DEPENDS_ON
                                )
                        )) {

                    System.out.println(
                            "  -> " + path
                    );

                }


                /*
                 * =================================================
                 * Explainable Impact Analysis
                 * =================================================
                 */

                System.out.println();

                System.out.println(
                        "======================================"
                );

                System.out.println(
                        "Explainable Impact Analysis"
                );

                System.out.println(
                        "======================================"
                );


                System.out.println();

                System.out.println(
                        "Impact paths of changing Teacher.teach():"
                );


                for (String path :
                        graphRepository.getImpactPaths(
                                "Default Package.Teacher#teach()",
                                2,
                                java.util.Set.of(
                                        EdgeType.CALLS
                                )
                        )) {

                    System.out.println(
                            "  -> " + path
                    );

                }

            }


            /*
             * =================================================
             * Existing In-Memory Knowledge Graph Queries
             * =================================================
             *
             * Kept temporarily as a reference implementation
             * while validating Neo4j results.
             */

            KnowledgeGraphQuery query =
                    new KnowledgeGraphQuery(graph);


            System.out.println();

            System.out.println(
                    "======================================"
            );

            System.out.println(
                    "Graph Queries"
            );

            System.out.println(
                    "======================================"
            );


            /*
             * -------------------------------------------------
             * Dependencies
             * -------------------------------------------------
             */

            System.out.println();

            System.out.println(
                    "Dependencies of StudentService:"
            );

            for (GraphNode node :
                    query.getDependencies(
                            "Default Package.StudentService"
                    )) {

                System.out.println(
                        "  -> " + node.getName()
                );

            }


            /*
             * -------------------------------------------------
             * Dependents
             * -------------------------------------------------
             */

            System.out.println();

            System.out.println(
                    "Dependents of Student:"
            );

            for (GraphNode node :
                    query.getDependents(
                            "Default Package.Student"
                    )) {

                System.out.println(
                        "  -> " + node.getName()
                );

            }


            /*
             * -------------------------------------------------
             * Callees
             * -------------------------------------------------
             */

            System.out.println();

            System.out.println(
                    "Callees of Student.study():"
            );

            for (GraphNode node :
                    query.getCallees(
                            "Default Package.Student#study()"
                    )) {

                System.out.println(
                        "  -> " + node.getName()
                );

            }


            /*
             * -------------------------------------------------
             * Callers
             * -------------------------------------------------
             */

            System.out.println();

            System.out.println(
                    "Callers of Teacher.teach():"
            );

            for (GraphNode node :
                    query.getCallers(
                            "Default Package.Teacher#teach()"
                    )) {

                System.out.println(
                        "  -> " + node.getName()
                );

            }


            /*
             * -------------------------------------------------
             * Subclasses
             * -------------------------------------------------
             */

            System.out.println();

            System.out.println(
                    "Subclasses of Animal:"
            );

            for (GraphNode node :
                    query.getSubclasses(
                            "Default Package.Animal"
                    )) {

                System.out.println(
                        "  -> " + node.getName()
                );

            }


            /*
             * -------------------------------------------------
             * Superclass
             * -------------------------------------------------
             */

            System.out.println();

            System.out.println(
                    "Superclass of Mammal:"
            );

            for (GraphNode node :
                    query.getSuperclass(
                            "Default Package.Mammal"
                    )) {

                System.out.println(
                        "  -> " + node.getName()
                );

            }


            /*
             * -------------------------------------------------
             * Implemented Interfaces
             * -------------------------------------------------
             */

            System.out.println();

            System.out.println(
                    "Interfaces implemented by Report:"
            );

            for (GraphNode node :
                    query.getImplementedInterfaces(
                            "demo.Report"
                    )) {

                System.out.println(
                        "  -> " + node.getName()
                );

            }


            /*
             * -------------------------------------------------
             * Implementations
             * -------------------------------------------------
             */

            System.out.println();

            System.out.println(
                    "Implementations of Printable:"
            );

            for (GraphNode node :
                    query.getImplementations(
                            "com.demo.Printable"
                    )) {

                System.out.println(
                        "  -> " + node.getName()
                );

            }


            /*
             * =================================================
             * Existing In-Memory Impact Analysis
             * =================================================
             */

            ImpactAnalyzer impactAnalyzer =
                    new ImpactAnalyzer(query);


            System.out.println();

            System.out.println(
                    "======================================"
            );

            System.out.println(
                    "Impact Analysis"
            );

            System.out.println(
                    "======================================"
            );


            /*
             * -------------------------------------------------
             * Impact of changing Student
             * -------------------------------------------------
             */

            System.out.println();

            System.out.println(
                    "Impact of changing Student:"
            );

            for (GraphNode node :
                    impactAnalyzer.getImpact(
                            "Default Package.Student"
                    )) {

                System.out.println(
                        "  -> " + node.getName()
                );

            }


            /*
             * -------------------------------------------------
             * Impact through method calls
             * -------------------------------------------------
             */

            System.out.println();

            System.out.println(
                    "Impact of changing Teacher.teach():"
            );

            for (GraphNode node :
                    impactAnalyzer.getImpact(
                            "Default Package.Teacher#teach()",
                            java.util.Set.of(
                                    EdgeType.CALLS
                            )
                    )) {

                System.out.println(
                        "  -> " + node.getName()
                );

            }


            /*
             * -------------------------------------------------
             * Containment-Aware Impact
             * -------------------------------------------------
             */

            System.out.println();

            System.out.println(
                    "Containment-Aware Impact Analysis"
            );

            System.out.println();

            System.out.println(
                    "Impact of changing Teacher:"
            );

            for (GraphNode node :
                    impactAnalyzer.getContainmentAwareImpact(
                            "Default Package.Teacher"
                    )) {

                System.out.println(
                        "  -> " + node.getName()
                );

            }


            /*
             * =================================================
             * Print Knowledge Graph
             * =================================================
             */

            KnowledgeGraphPrinter graphPrinter =
                    new KnowledgeGraphPrinter();

            graphPrinter.print(graph);


            /*
             * =================================================
             * Print Repository Details
             * =================================================
             */

            RepositoryPrinter repositoryPrinter =
                    new RepositoryPrinter();

            repositoryPrinter.print(repository);

        }

        catch (Exception e) {

            e.printStackTrace();

        }

    }
    


}