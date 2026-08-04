package com.softwaredna.parser;

import com.softwaredna.analysis.repository.RepositoryAnalyzer;
import com.softwaredna.knowledge.KnowledgeGraph;
import com.softwaredna.knowledge.KnowledgeGraphBuilder;
import com.softwaredna.knowledge.printer.KnowledgeGraphPrinter;
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