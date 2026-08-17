package com.softwaredna.parser;

import com.softwaredna.analysis.repository.RepositoryAnalyzer;
import com.softwaredna.knowledge.EdgeType;
import com.softwaredna.knowledge.GraphNode;
import com.softwaredna.knowledge.KnowledgeGraph;
import com.softwaredna.knowledge.KnowledgeGraphBuilder;
import com.softwaredna.knowledge.printer.KnowledgeGraphPrinter;
import com.softwaredna.knowledge.query.KnowledgeGraphQuery;
import com.softwaredna.model.RepositoryModel;
import com.softwaredna.printer.RepositoryPrinter;

public class ParserApplication {

    public static void main(String[] args) {

        try {

            RepositoryParser parser =
                    new RepositoryParser();

            RepositoryModel repository =
                    parser.parseRepository("../sample_projects");

            /*
             * Run repository analyses
             */
            RepositoryAnalyzer analyzer =
                    new RepositoryAnalyzer();

            analyzer.analyze(repository);

            /*
             * Build Knowledge Graph
             */
            KnowledgeGraphBuilder graphBuilder =
                    new KnowledgeGraphBuilder();

            KnowledgeGraph graph =
                    graphBuilder.build(repository);

            /*
             * -------------------------------------------------
             * Knowledge Graph Queries
             * -------------------------------------------------
             */

            KnowledgeGraphQuery query =
                    new KnowledgeGraphQuery(graph);

            System.out.println();
            System.out.println("======================================");
            System.out.println("Graph Queries");
            System.out.println("======================================");


            /*
             * Query 1
             * -----------------------------------------------
             * What does StudentService depend on?
             */

            System.out.println();
            System.out.println(
                    "Dependencies of StudentService:");

            for (GraphNode node :
                    query.getOutgoingNodes(
                            "Default Package.StudentService",
                            EdgeType.DEPENDS_ON)) {

                System.out.println(
                        "  -> " + node.getName());

            }


            /*
             * Query 2
             * -----------------------------------------------
             * What methods does Student.study() call?
             */

            System.out.println();
            System.out.println(
                    "Methods called by Student.study():");

            for (GraphNode node :
                    query.getOutgoingNodes(
                            "Default Package.Student#study()",
                            EdgeType.CALLS)) {

                System.out.println(
                        "  -> " + node.getName());

            }


            /*
             * Query 3
             * -----------------------------------------------
             * Which classes extend Animal?
             */

            System.out.println();
            System.out.println(
                    "Classes extending Animal:");

            for (GraphNode node :
                    query.getIncomingNodes(
                            "Default Package.Animal",
                            EdgeType.EXTENDS)) {

                System.out.println(
                        "  -> " + node.getName());

            }


            /*
             * Query 4
             * -----------------------------------------------
             * Which interfaces does Report implement?
             */

            System.out.println();
            System.out.println(
                    "Interfaces implemented by Report:");

            for (GraphNode node :
                    query.getOutgoingNodes(
                            "demo.Report",
                            EdgeType.IMPLEMENTS)) {

                System.out.println(
                        "  -> " + node.getName());

            }


            /*
             * Print Knowledge Graph
             */

            KnowledgeGraphPrinter graphPrinter =
                    new KnowledgeGraphPrinter();

            graphPrinter.print(graph);


            /*
             * Print repository details
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