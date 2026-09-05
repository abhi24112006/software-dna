package com.softwaredna.parser;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

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
import com.softwaredna.graph.GraphRepository;
import com.softwaredna.graph.Neo4jGraphRepository;
import com.softwaredna.knowledge.EdgeType;
import com.softwaredna.knowledge.GraphNode;
import com.softwaredna.knowledge.KnowledgeGraph;
import com.softwaredna.knowledge.KnowledgeGraphBuilder;
import com.softwaredna.knowledge.printer.KnowledgeGraphPrinter;
import com.softwaredna.knowledge.query.ImpactAnalyzer;
import com.softwaredna.knowledge.query.KnowledgeGraphQuery;
import com.softwaredna.language.Language;
import com.softwaredna.language.LanguageDetector;
import com.softwaredna.language.LanguageReport;
import com.softwaredna.model.RepositoryModel;
import com.softwaredna.neo4j.Neo4jConfig;
import com.softwaredna.neo4j.Neo4jService;
import com.softwaredna.parser.language.LanguageParser;
import com.softwaredna.parser.language.ParserFactory;
import com.softwaredna.parser.nlp.NaturalLanguageQueryEngine;
import com.softwaredna.parser.nlp.QueryResult;
import com.softwaredna.printer.RepositoryPrinter;

public class ParserApplication {

    public static void main(String[] args) {

        try {

            /*
             * =================================================
             * Parse Repository
             * =================================================
             */

            String repositoryPath =
                    "../sample_projects/java_test";

            LanguageDetector languageDetector =
                    new LanguageDetector();

            LanguageReport languageReport =
                    new LanguageReport(
                            languageDetector.detect(
                                    Path.of(repositoryPath)
                            )
                    );

            languageReport.print();

            /*
             * =================================================
             * Run Language-Specific Parser
             * =================================================
             *
             * The repository is parsed exactly once using
             * the parser selected by the detected language.
             */

            Language primaryLanguage =
                    languageReport.getPrimaryLanguage();

            LanguageParser languageParser =
                    ParserFactory.getParser(
                            primaryLanguage
                    );

            RepositoryModel repository =
                    languageParser.parse(
                            repositoryPath
                    );

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
             * Dynamically Select Graph Nodes
             * =================================================
             *
             * IMPORTANT:
             * These variables are declared outside the Neo4j
             * try-block because they are also used by the
             * in-memory graph queries later.
             */

            Collection<GraphNode> graphNodeCollection =
                    graph.getNodes();

            List<GraphNode> graphNodes =
                    new ArrayList<>(
                            graphNodeCollection
                    );

                     KnowledgeGraphQuery query =
                    new KnowledgeGraphQuery(graph);

            /*
             * Select representative nodes from actual graph relationships
             * instead of simply taking the first class/method in the graph.
             * This keeps the demo queries meaningful across repositories.
             */
            GraphNode primaryClass =
                    findNodeWithOutgoingEdge(
                            query, graphNodes, "CLASS", EdgeType.DEPENDS_ON);

            GraphNode secondaryClass =
                    findNodeWithIncomingEdge(
                            query, graphNodes, "CLASS", EdgeType.DEPENDS_ON, primaryClass);

            if (primaryClass == null) {
                primaryClass = findFirstNodeOfType(graphNodes, "CLASS");
            }

            if (secondaryClass == null) {
                secondaryClass = findSecondNodeOfType(graphNodes, "CLASS", primaryClass);
            }

            GraphNode primaryMethod =
                    findNodeWithOutgoingEdge(
                            query, graphNodes, "METHOD", EdgeType.CALLS);

            GraphNode secondaryMethod =
                    findNodeWithIncomingEdge(
                            query, graphNodes, "METHOD", EdgeType.CALLS, primaryMethod);

            if (primaryMethod == null) {
                primaryMethod = findFirstNodeOfType(graphNodes, "METHOD");
            }

            if (secondaryMethod == null) {
                secondaryMethod = findSecondNodeOfType(graphNodes, "METHOD", primaryMethod);
            }

            GraphNode interfaceNode =
                    findNodeWithIncomingEdge(
                            query, graphNodes, "INTERFACE", EdgeType.IMPLEMENTS, null);

            if (interfaceNode == null) {
                interfaceNode = findFirstNodeOfType(graphNodes, "INTERFACE");
            }

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

                /*
                 * =================================================
                 * Architecture Health
                 * =================================================
                 */

                ArchitectureHealthAnalyzer healthAnalyzer =
                        new ArchitectureHealthAnalyzer();

                ArchitectureHealthReport healthReport =
                        healthAnalyzer.analyze(
                                architectureReport
                        );

                healthReport.print();

                /*
                 * =================================================
                 * Architecture Snapshot
                 * =================================================
                 */

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

                /*
                 * =================================================
                 * Architecture Evolution
                 * =================================================
                 */

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

                    architectureSnapshotStore.addDiff(
                            diff
                    );

                    ArchitectureDiffPrinter diffPrinter =
                            new ArchitectureDiffPrinter();

                    diffPrinter.print(
                            diff
                    );

                    /*
                     * =================================================
                     * Architecture Trend
                     * =================================================
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

                } else {

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

                /*
                 * =================================================
                 * Architecture Recommendations
                 * =================================================
                 */

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

                if (primaryClass != null) {

                    printNeo4jResults(
                            "Dependencies of "
                                    + primaryClass.getId()
                                    + ":",
                            graphRepository.getDependencies(
                                    primaryClass.getId()
                            )
                    );

                } else {

                    System.out.println();

                    System.out.println(
                            "No class available for dependency query."
                    );
                }

                /*
                 * -------------------------------------------------
                 * Dependents
                 * -------------------------------------------------
                 */

                if (secondaryClass != null) {

                    printNeo4jResults(
                            "Dependents of "
                                    + secondaryClass.getId()
                                    + ":",
                            graphRepository.getDependents(
                                    secondaryClass.getId()
                            )
                    );

                } else if (primaryClass != null) {

                    printNeo4jResults(
                            "Dependents of "
                                    + primaryClass.getId()
                                    + ":",
                            graphRepository.getDependents(
                                    primaryClass.getId()
                            )
                    );
                }

                /*
                 * -------------------------------------------------
                 * Callees
                 * -------------------------------------------------
                 */

                if (primaryMethod != null) {

                    printNeo4jResults(
                            "Callees of "
                                    + primaryMethod.getId()
                                    + ":",
                            graphRepository.getCallees(
                                    primaryMethod.getId()
                            )
                    );

                } else {

                    System.out.println();

                    System.out.println(
                            "No method available for callee query."
                    );
                }

                /*
                 * -------------------------------------------------
                 * Callers
                 * -------------------------------------------------
                 */

                if (secondaryMethod != null) {

                    printNeo4jResults(
                            "Callers of "
                                    + secondaryMethod.getId()
                                    + ":",
                            graphRepository.getCallers(
                                    secondaryMethod.getId()
                            )
                    );

                } else if (primaryMethod != null) {

                    printNeo4jResults(
                            "Callers of "
                                    + primaryMethod.getId()
                                    + ":",
                            graphRepository.getCallers(
                                    primaryMethod.getId()
                            )
                    );
                }

                /*
                 * -------------------------------------------------
                 * Subclasses
                 * -------------------------------------------------
                 */

                if (primaryClass != null) {

                    printNeo4jResults(
                            "Subclasses of "
                                    + primaryClass.getId()
                                    + ":",
                            graphRepository.getSubclasses(
                                    primaryClass.getId()
                            )
                    );
                }

                /*
                 * -------------------------------------------------
                 * Superclass
                 * -------------------------------------------------
                 */

                if (secondaryClass != null) {

                    printNeo4jResults(
                            "Superclass of "
                                    + secondaryClass.getId()
                                    + ":",
                            graphRepository.getSuperclass(
                                    secondaryClass.getId()
                            )
                    );

                } else if (primaryClass != null) {

                    printNeo4jResults(
                            "Superclass of "
                                    + primaryClass.getId()
                                    + ":",
                            graphRepository.getSuperclass(
                                    primaryClass.getId()
                            )
                    );
                }

                /*
                 * -------------------------------------------------
                 * Implemented Interfaces
                 * -------------------------------------------------
                 */

                if (primaryClass != null) {

                    printNeo4jResults(
                            "Interfaces implemented by "
                                    + primaryClass.getId()
                                    + ":",
                            graphRepository.getImplementedInterfaces(
                                    primaryClass.getId()
                            )
                    );
                }

                /*
                 * -------------------------------------------------
                 * Implementations
                 * -------------------------------------------------
                 */

                if (interfaceNode != null) {

                    printNeo4jResults(
                            "Implementations of "
                                    + interfaceNode.getId()
                                    + ":",
                            graphRepository.getImplementations(
                                    interfaceNode.getId()
                            )
                    );

                } else {

                    System.out.println();

                    System.out.println(
                            "No interface available for implementation query."
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
                 * Class Impact
                 * -------------------------------------------------
                 */

                if (primaryClass != null) {

                    printNeo4jResults(
                            "Impact of changing "
                                    + primaryClass.getId()
                                    + ":",
                            graphRepository.getImpact(
                                    primaryClass.getId()
                            )
                    );
                }

                /*
                 * -------------------------------------------------
                 * Method Impact
                 * -------------------------------------------------
                 */

                if (primaryMethod != null) {

                    printNeo4jResults(
                            "Impact of changing "
                                    + primaryMethod.getId()
                                    + ":",
                            graphRepository.getMethodImpact(
                                    primaryMethod.getId()
                            )
                    );
                }

                /*
                 * -------------------------------------------------
                 * Containment-Aware Impact
                 * -------------------------------------------------
                 */

                if (secondaryClass != null) {

                    System.out.println();

                    System.out.println(
                            "Containment-Aware Impact Analysis"
                    );

                    printNeo4jResults(
                            "Impact of changing "
                                    + secondaryClass.getId()
                                    + ":",
                            graphRepository.getContainmentAwareImpact(
                                    secondaryClass.getId()
                            )
                    );

                } else if (primaryClass != null) {

                    System.out.println();

                    System.out.println(
                            "Containment-Aware Impact Analysis"
                    );

                    printNeo4jResults(
                            "Impact of changing "
                                    + primaryClass.getId()
                                    + ":",
                            graphRepository.getContainmentAwareImpact(
                                    primaryClass.getId()
                            )
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

                if (primaryClass != null) {

                    printNeo4jResults(
                            "Reachable from "
                                    + primaryClass.getId()
                                    + " (depth 1):",
                            graphRepository.getReachableNodes(
                                    primaryClass.getId(),
                                    1
                            )
                    );

                    printNeo4jResults(
                            "Reachable from "
                                    + primaryClass.getId()
                                    + " (depth 2):",
                            graphRepository.getReachableNodes(
                                    primaryClass.getId(),
                                    2
                            )
                    );
                }

                /*
                 * =================================================
                 * Architecture Paths
                 * =================================================
                 */

                if (primaryClass != null) {

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

                    printNeo4jResults(
                            "Paths from "
                                    + primaryClass.getId()
                                    + " (depth 2):",
                            graphRepository.getArchitecturePaths(
                                    primaryClass.getId(),
                                    2
                            )
                    );
                }

                /*
                 * =================================================
                 * Dependency Architecture Paths
                 * =================================================
                 */

                if (primaryClass != null) {

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

                    printNeo4jResults(
                            "Dependency paths from "
                                    + primaryClass.getId()
                                    + " (depth 2):",
                            graphRepository.getArchitecturePaths(
                                    primaryClass.getId(),
                                    2,
                                    Set.of(
                                            EdgeType.DEPENDS_ON
                                    )
                            )
                    );
                }

                /*
                 * =================================================
                 * Explainable Impact Analysis
                 * =================================================
                 */

                if (primaryMethod != null) {

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

                    printNeo4jResults(
                            "Impact paths of changing "
                                    + primaryMethod.getId()
                                    + ":",
                            graphRepository.getImpactPaths(
                                    primaryMethod.getId(),
                                    2,
                                    Set.of(
                                            EdgeType.CALLS
                                    )
                            )
                    );
                }
            }

            /*
             * =================================================
             * In-Memory Knowledge Graph Queries
             * =================================================
             */


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

            if (primaryClass != null) {

                printGraphNodeResults(
                        "Dependencies of "
                                + primaryClass.getId()
                                + ":",
                        query.getDependencies(
                                primaryClass.getId()
                        )
                );
            }

            /*
             * -------------------------------------------------
             * Dependents
             * -------------------------------------------------
             */

            if (secondaryClass != null) {

                printGraphNodeResults(
                        "Dependents of "
                                + secondaryClass.getId()
                                + ":",
                        query.getDependents(
                                secondaryClass.getId()
                        )
                );

            } else if (primaryClass != null) {

                printGraphNodeResults(
                        "Dependents of "
                                + primaryClass.getId()
                                + ":",
                        query.getDependents(
                                primaryClass.getId()
                        )
                );
            }

            /*
             * -------------------------------------------------
             * Callees
             * -------------------------------------------------
             */

            if (primaryMethod != null) {

                printGraphNodeResults(
                        "Callees of "
                                + primaryMethod.getId()
                                + ":",
                        query.getCallees(
                                primaryMethod.getId()
                        )
                );
            }

            /*
             * -------------------------------------------------
             * Callers
             * -------------------------------------------------
             */

            if (secondaryMethod != null) {

                printGraphNodeResults(
                        "Callers of "
                                + secondaryMethod.getId()
                                + ":",
                        query.getCallers(
                                secondaryMethod.getId()
                        )
                );

            } else if (primaryMethod != null) {

                printGraphNodeResults(
                        "Callers of "
                                + primaryMethod.getId()
                                + ":",
                        query.getCallers(
                                primaryMethod.getId()
                        )
                );
            }

            /*
             * -------------------------------------------------
             * Subclasses
             * -------------------------------------------------
             */

            if (primaryClass != null) {

                printGraphNodeResults(
                        "Subclasses of "
                                + primaryClass.getId()
                                + ":",
                        query.getSubclasses(
                                primaryClass.getId()
                        )
                );
            }

            /*
             * -------------------------------------------------
             * Superclass
             * -------------------------------------------------
             */

            if (secondaryClass != null) {

                printGraphNodeResults(
                        "Superclass of "
                                + secondaryClass.getId()
                                + ":",
                        query.getSuperclass(
                                secondaryClass.getId()
                        )
                );

            } else if (primaryClass != null) {

                printGraphNodeResults(
                        "Superclass of "
                                + primaryClass.getId()
                                + ":",
                        query.getSuperclass(
                                primaryClass.getId()
                        )
                );
            }

            /*
             * -------------------------------------------------
             * Implemented Interfaces
             * -------------------------------------------------
             */

            if (primaryClass != null) {

                printGraphNodeResults(
                        "Interfaces implemented by "
                                + primaryClass.getId()
                                + ":",
                        query.getImplementedInterfaces(
                                primaryClass.getId()
                        )
                );
            }

            /*
             * -------------------------------------------------
             * Implementations
             * -------------------------------------------------
             */

            if (interfaceNode != null) {

                printGraphNodeResults(
                        "Implementations of "
                                + interfaceNode.getId()
                                + ":",
                        query.getImplementations(
                                interfaceNode.getId()
                        )
                );
            }

            /*
             * =================================================
             * In-Memory Impact Analysis
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
             * Class Impact
             * -------------------------------------------------
             */

            if (primaryClass != null) {

                printGraphNodeResults(
                        "Impact of changing "
                                + primaryClass.getId()
                                + ":",
                        impactAnalyzer.getImpact(
                                primaryClass.getId()
                        )
                );
            }

            /*
             * -------------------------------------------------
             * Method Call Impact
             * -------------------------------------------------
             */

            if (primaryMethod != null) {

                printGraphNodeResults(
                        "Impact through method calls from "
                                + primaryMethod.getId()
                                + ":",
                        impactAnalyzer.getImpact(
                                primaryMethod.getId(),
                                Set.of(
                                        EdgeType.CALLS
                                )
                        )
                );
            }

            /*
             * -------------------------------------------------
             * Containment-Aware Impact
             * -------------------------------------------------
             */

            if (secondaryClass != null) {

                System.out.println();

                System.out.println(
                        "Containment-Aware Impact Analysis"
                );

                printGraphNodeResults(
                        "Impact of changing "
                                + secondaryClass.getId()
                                + ":",
                        impactAnalyzer.getContainmentAwareImpact(
                                secondaryClass.getId()
                        )
                );

            } else if (primaryClass != null) {

                System.out.println();

                System.out.println(
                        "Containment-Aware Impact Analysis"
                );

                printGraphNodeResults(
                        "Impact of changing "
                                + primaryClass.getId()
                                + ":",
                        impactAnalyzer.getContainmentAwareImpact(
                                primaryClass.getId()
                        )
                );
            }

            /*
             * =================================================
             * Natural Language Query Engine
             * =================================================
             *
             * Verify the deterministic NLP pipeline against the
             * actual Knowledge Graph built from this repository.
             */

            NaturalLanguageQueryEngine naturalLanguageQueryEngine =
                    new NaturalLanguageQueryEngine(graph);

            System.out.println();
            System.out.println("======================================");
            System.out.println("Natural Language Query Engine");
            System.out.println("======================================");

            if (primaryClass != null) {
                runNaturalLanguageQuery(
                        naturalLanguageQueryEngine,
                        "What does " + primaryClass.getName() + " depend on?");
            }

            if (secondaryClass != null) {
                runNaturalLanguageQuery(
                        naturalLanguageQueryEngine,
                        "Who depends on " + secondaryClass.getName() + "?");
            }

            if (primaryClass != null) {
                runNaturalLanguageQuery(
                        naturalLanguageQueryEngine,
                        "What methods does " + primaryClass.getName() + " call?");
            }

            if (secondaryMethod != null) {
                runNaturalLanguageQuery(
                        naturalLanguageQueryEngine,
                        "Who calls " + secondaryMethod.getName() + "?");
            }

            /*
             * =================================================
             * Print Knowledge Graph
             * =================================================
             */

            KnowledgeGraphPrinter knowledgeGraphPrinter =
                    new KnowledgeGraphPrinter();

            knowledgeGraphPrinter.print(graph);

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

    /*
 * =================================================
 * Natural Language Query Helpers
 * =================================================
 */

/**
 * Runs a natural-language question against the
 * Knowledge Graph and prints the resulting graph facts.
 */

private static void runNaturalLanguageQuery(
        NaturalLanguageQueryEngine engine,
        String question) {

    System.out.println();
    System.out.println("Question: " + question);

    try {
        QueryResult result = engine.ask(question);

        System.out.println("Intent: " + result.getIntent());
        System.out.println(
                "Entity: " + result.getEntity().getName()
        );

        String answer =
                engine.askAndAnswer(question);

        System.out.println("Answer:");
        System.out.println("  " + answer);

    } catch (Exception e) {

        System.out.println(
                "  NLP query could not be executed: "
                        + e.getMessage()
        );
    }
}

    /*
     * =================================================
     * Dynamic Node Selection
     * =================================================
     */

    private static GraphNode findFirstNodeOfType(
            List<GraphNode> nodes,
            String typeName) {

        for (GraphNode node : nodes) {

            if (node.getType() != null
                    && node.getType()
                            .name()
                            .equals(typeName)) {

                return node;
            }
        }

        return null;
    }

    private static GraphNode findSecondNodeOfType(
            List<GraphNode> nodes,
            String typeName,
            GraphNode firstNode) {

        boolean firstFound = false;

        for (GraphNode node : nodes) {

            if (node.getType() != null
                    && node.getType()
                            .name()
                            .equals(typeName)) {

                if (firstNode == null) {
                    return node;
                }

                if (!firstFound
                        && node.getId()
                                .equals(firstNode.getId())) {

                    firstFound = true;
                    continue;
                }

                if (firstFound) {
                    return node;
                }
            }
        }

        return null;
    }

    private static GraphNode findNodeWithOutgoingEdge(
            KnowledgeGraphQuery query,
            List<GraphNode> nodes,
            String typeName,
            EdgeType edgeType) {

        for (GraphNode node : nodes) {
            if (!hasType(node, typeName)) {
                continue;
            }

            if (!query.getOutgoingEdges(node.getId(), edgeType).isEmpty()) {
                return node;
            }
        }

        return null;
    }

    private static GraphNode findNodeWithIncomingEdge(
            KnowledgeGraphQuery query,
            List<GraphNode> nodes,
            String typeName,
            EdgeType edgeType,
            GraphNode excludedNode) {

        for (GraphNode node : nodes) {
            if (!hasType(node, typeName)) {
                continue;
            }

            if (excludedNode != null
                    && excludedNode.getId().equals(node.getId())) {
                continue;
            }

            if (!query.getIncomingEdges(node.getId(), edgeType).isEmpty()) {
                return node;
            }
        }

        return null;
    }

    private static boolean hasType(
            GraphNode node,
            String typeName) {

        return node != null
                && node.getType() != null
                && node.getType().name().equals(typeName);
    }

    /*
     * =================================================
     * Output Helpers
     * =================================================
     */

    private static void printNeo4jResults(
            String title,
            List<String> results) {

        System.out.println();

        System.out.println(title);

        if (results == null
                || results.isEmpty()) {

            System.out.println(
                    "  None"
            );

            return;
        }

        for (String result : results) {

            System.out.println(
                    "  -> " + result
            );
        }
    }

    private static void printGraphNodeResults(
            String title,
            List<GraphNode> nodes) {

        System.out.println();

        System.out.println(title);

        if (nodes == null
                || nodes.isEmpty()) {

            System.out.println(
                    "  None"
            );

            return;
        }

        for (GraphNode node : nodes) {

            System.out.println(
                    "  -> " + node.getName()
            );
        }
    }
}