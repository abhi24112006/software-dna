package com.softwaredna.parser;

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
import com.softwaredna.neo4j.Neo4jService;
import com.softwaredna.printer.RepositoryPrinter;

public class ParserApplication {

    public static void main(String[] args) {

        try {

            /*
             * -------------------------------------------------
             * Parse Repository
             * -------------------------------------------------
             */

            RepositoryParser parser =
                    new RepositoryParser();

            RepositoryModel repository =
                    parser.parseRepository("../sample_projects");


            /*
             * -------------------------------------------------
             * Run Repository Analyses
             * -------------------------------------------------
             */

            RepositoryAnalyzer analyzer =
                    new RepositoryAnalyzer();

            analyzer.analyze(repository);


            /*
             * -------------------------------------------------
             * Build Knowledge Graph
             * -------------------------------------------------
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

            String neo4jUri =
                    "bolt://localhost:7687";

            String neo4jUsername =
                    "neo4j";

            String neo4jPassword =
                    System.getenv("NEO4J_PASSWORD");

            String neo4jDatabase =
                    "neo4j";


            try (
                    Neo4jService neo4j =
                            new Neo4jService(
                                    neo4jUri,
                                    neo4jUsername,
                                    neo4jPassword,
                                    neo4jDatabase
                            )
            ) {

                /*
                 * -------------------------------------------------
                 * Verify Neo4j Connection
                 * -------------------------------------------------
                 */

                neo4j.verifyConnection();


                /*
                 * -------------------------------------------------
                 * Export Knowledge Graph to Neo4j
                 * -------------------------------------------------
                 */

                neo4j.export(graph);


                /*
                 * =================================================
                 * Graph Repository
                 * =================================================
                 *
                 * ParserApplication now talks to the
                 * GraphRepository abstraction instead of
                 * directly talking to Neo4jQueryService and
                 * Neo4jImpactAnalyzer.
                 */

                GraphRepository graphRepository =
                        new Neo4jGraphRepository(
                                neo4j.getQueryService(),
                                neo4j.getImpactAnalyzer()
                        );


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
                 * Query 1
                 * Dependencies of StudentService
                 * -------------------------------------------------
                 */

                System.out.println();
                System.out.println(
                        "Dependencies of StudentService:"
                );

                for (String name :
                        graphRepository.getDependencies(
                                "Default Package.StudentService")) {

                    System.out.println(
                            "  -> " + name
                    );

                }


                /*
                 * -------------------------------------------------
                 * Query 2
                 * Dependents of Student
                 * -------------------------------------------------
                 */

                System.out.println();
                System.out.println(
                        "Dependents of Student:"
                );

                for (String name :
                        graphRepository.getDependents(
                                "Default Package.Student")) {

                    System.out.println(
                            "  -> " + name
                    );

                }


                /*
                 * -------------------------------------------------
                 * Query 3
                 * Callees of Student.study()
                 * -------------------------------------------------
                 */

                System.out.println();
                System.out.println(
                        "Callees of Student.study():"
                );

                for (String name :
                        graphRepository.getCallees(
                                "Default Package.Student#study()")) {

                    System.out.println(
                            "  -> " + name
                    );

                }


                /*
                 * -------------------------------------------------
                 * Query 4
                 * Callers of Teacher.teach()
                 * -------------------------------------------------
                 */

                System.out.println();
                System.out.println(
                        "Callers of Teacher.teach():"
                );

                for (String name :
                        graphRepository.getCallers(
                                "Default Package.Teacher#teach()")) {

                    System.out.println(
                            "  -> " + name
                    );

                }


                /*
                 * -------------------------------------------------
                 * Query 5
                 * Subclasses of Animal
                 * -------------------------------------------------
                 */

                System.out.println();
                System.out.println(
                        "Subclasses of Animal:"
                );

                for (String name :
                        graphRepository.getSubclasses(
                                "Default Package.Animal")) {

                    System.out.println(
                            "  -> " + name
                    );

                }


                /*
                 * -------------------------------------------------
                 * Query 6
                 * Superclass of Mammal
                 * -------------------------------------------------
                 */

                System.out.println();
                System.out.println(
                        "Superclass of Mammal:"
                );

                for (String name :
                        graphRepository.getSuperclass(
                                "Default Package.Mammal")) {

                    System.out.println(
                            "  -> " + name
                    );

                }


                /*
                 * -------------------------------------------------
                 * Query 7
                 * Interfaces implemented by Report
                 * -------------------------------------------------
                 */

                System.out.println();
                System.out.println(
                        "Interfaces implemented by Report:"
                );

                for (String name :
                        graphRepository.getImplementedInterfaces(
                                "demo.Report")) {

                    System.out.println(
                            "  -> " + name
                    );

                }


                /*
                 * -------------------------------------------------
                 * Query 8
                 * Implementations of Printable
                 * -------------------------------------------------
                 */

                System.out.println();
                System.out.println(
                        "Implementations of Printable:"
                );

                for (String name :
                        graphRepository.getImplementations(
                                "com.demo.Printable")) {

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
                 * Impact of changing Student
                 * -------------------------------------------------
                 */

                System.out.println();
                System.out.println(
                        "Impact of changing Student:"
                );

                for (String name :
                        graphRepository.getImpact(
                                "Default Package.Student")) {

                    System.out.println(
                            "  -> " + name
                    );

                }


                /*
                 * -------------------------------------------------
                 * Impact of changing Teacher.teach()
                 * -------------------------------------------------
                 */

                System.out.println();
                System.out.println(
                        "Impact of changing Teacher.teach():"
                );

                for (String name :
                        graphRepository.getMethodImpact(
                                "Default Package.Teacher#teach()")) {

                    System.out.println(
                            "  -> " + name
                    );

                }


                /*
                 * -------------------------------------------------
                 * Containment-Aware Impact Analysis
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
                                "Default Package.Teacher")) {

                    System.out.println(
                            "  -> " + name
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
             * Query 1
             * Dependencies of StudentService
             */

            System.out.println();
            System.out.println(
                    "Dependencies of StudentService:"
            );

            for (GraphNode node :
                    query.getDependencies(
                            "Default Package.StudentService")) {

                System.out.println(
                        "  -> " + node.getName()
                );

            }


            /*
             * Query 2
             * Dependents of Student
             */

            System.out.println();
            System.out.println(
                    "Dependents of Student:"
            );

            for (GraphNode node :
                    query.getDependents(
                            "Default Package.Student")) {

                System.out.println(
                        "  -> " + node.getName()
                );

            }


            /*
             * Query 3
             * Callees of Student.study()
             */

            System.out.println();
            System.out.println(
                    "Callees of Student.study():"
            );

            for (GraphNode node :
                    query.getCallees(
                            "Default Package.Student#study()")) {

                System.out.println(
                        "  -> " + node.getName()
                );

            }


            /*
             * Query 4
             * Callers of Teacher.teach()
             */

            System.out.println();
            System.out.println(
                    "Callers of Teacher.teach():"
            );

            for (GraphNode node :
                    query.getCallers(
                            "Default Package.Teacher#teach()")) {

                System.out.println(
                        "  -> " + node.getName()
                );

            }


            /*
             * Query 5
             * Subclasses of Animal
             */

            System.out.println();
            System.out.println(
                    "Subclasses of Animal:"
            );

            for (GraphNode node :
                    query.getSubclasses(
                            "Default Package.Animal")) {

                System.out.println(
                        "  -> " + node.getName()
                );

            }


            /*
             * Query 6
             * Superclass of Mammal
             */

            System.out.println();
            System.out.println(
                    "Superclass of Mammal:"
            );

            for (GraphNode node :
                    query.getSuperclass(
                            "Default Package.Mammal")) {

                System.out.println(
                        "  -> " + node.getName()
                );

            }


            /*
             * Query 7
             * Interfaces implemented by Report
             */

            System.out.println();
            System.out.println(
                    "Interfaces implemented by Report:"
            );

            for (GraphNode node :
                    query.getImplementedInterfaces(
                            "demo.Report")) {

                System.out.println(
                        "  -> " + node.getName()
                );

            }


            /*
             * Query 8
             * Implementations of Printable
             */

            System.out.println();
            System.out.println(
                    "Implementations of Printable:"
            );

            for (GraphNode node :
                    query.getImplementations(
                            "com.demo.Printable")) {

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
             * Impact of changing Student
             */

            System.out.println();
            System.out.println(
                    "Impact of changing Student:"
            );

            for (GraphNode node :
                    impactAnalyzer.getImpact(
                            "Default Package.Student")) {

                System.out.println(
                        "  -> " + node.getName()
                );

            }


            /*
             * Impact through method calls
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
                            ))) {

                System.out.println(
                        "  -> " + node.getName()
                );

            }


            /*
             * Containment-Aware Impact Analysis
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
                            "Default Package.Teacher")) {

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